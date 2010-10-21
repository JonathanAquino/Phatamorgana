documentation = Documentation.new(
  'Creates a new method using the selected code',
  'Jonathan Aquino',
  '0.1'
)

class ExtractMethodAction
  include java.awt.event.ActionListener

  # Handles a click on the menu item.
  def actionPerformed(event)
    newContents = self.extractMethod($context.get('currentSourceFile').getContentsWithSelectionMarkers())
    if newContents.nil?
      $context.get('applicationWindow').showErrorMessage('Extract Method failed')
      return
    end
    changeSet = ChangeSet.new
    changeSet.setContents($context.get('currentSourceFile'), newContents)
    $context.get('applicationWindow').execute(changeSet)
  end

  # Returns the code with the selected code extracted, or nil if the extraction
  # could not be performed.
  def extractMethod(code)
    code = addFunctionBoundaryMarkers(code)
    if code !~ /\{FUNCTION-BOUNDARY\}(\s*)(.*?\n)([^\n]*\{SELECTION-START\}.*?\{SELECTION-END\}[^\n]*(\r?\n))(.*?)\{FUNCTION-BOUNDARY\}?/m
      return nil
    end
    originalFunction = $&
    functionIndent = $1
    codeAbove = $2
    codeInSelection = $3
    lineTerminator = $4
    codeBelow = $5
    static = codeAbove =~ /((public|protected|private|abstract)\s*)*static/
    variablesAbove = extractVariables(codeAbove)
    variablesInSelection = extractVariables(codeInSelection)
    variablesAssignedInSelection = extractAssignedVariables(codeInSelection)
    variablesBelow = extractVariables(codeBelow)
    inputVariables = variablesInSelection & variablesAbove
    outputVariables = variablesBelow & variablesAssignedInSelection
    method = createMethod(static, inputVariables, outputVariables, codeInSelection, lineTerminator, functionIndent)
    code.gsub!(originalFunction, method['definition'] + functionIndent + codeAbove + method['call'] + codeBelow)
    return removeMarkers(code)
  end

  # Adds {FUNCTION-BOUNDARY} at approximate function boundaries.
  def addFunctionBoundaryMarkers(code)
    code = code.gsub(/\n(.*function\s+[0-9a-zA-Z_]+\s*\()/, "\n{FUNCTION-BOUNDARY}\\1")
    code += '{FUNCTION-BOUNDARY}'
  end

  # Removes all Phatamorgana markers from the code
  def removeMarkers(code)
    code.gsub('{FUNCTION-BOUNDARY}', '').gsub('{SELECTION-START}', '').gsub('{SELECTION-END}', '')
  end

  # Returns the variables found in the given code
  def extractVariables(code)
    return code.scan(/\$[0-9A-Za-z_]+/).uniq.select {|item| item != '$this'}
  end

  # Returns the variables assigned in the given code
  def extractAssignedVariables(code)
    return code.scan(/(\$[0-9A-Za-z_]+)(?=
      (\s*|\[[^=]*)    # Any whitespace, or if this is an array item like $a[], match almost anything
      (\+=|&=|=[^=]|\/=|\*=|\|=|%=|<<=|>>=|-=|\^=|\+\+|--)    # PHP assignment operators
    )/x).map {|result| result[0]}.uniq
  end

  # Returns the code for the method definition and method call
  def createMethod(static, inputVariables, outputVariables, codeInSelection, lineTerminator, functionIndent)
    definition = functionIndent + 'protected ' + (static ? 'static ' : '') + 'function newMethod(' + inputVariables.join(', ') + ') {' + lineTerminator
    definition += codeInSelection
    callProper = static ? 'self::newMethod' : '$this->newMethod'
    if outputVariables.size == 0
      call = functionIndent + functionIndent + callProper + '(' + inputVariables.join(', ') + ');' + lineTerminator
    elsif outputVariables.size == 1
      definition += functionIndent + functionIndent + 'return ' + outputVariables[0] + ';' + lineTerminator
      call = functionIndent + functionIndent + outputVariables[0] + ' = ' + callProper + '(' + inputVariables.join(', ') + ');' + lineTerminator
    else
      definition += functionIndent + functionIndent + 'return array(' + outputVariables.join(', ') + ');' + lineTerminator
      call = functionIndent + functionIndent + 'list(' + outputVariables.join(', ') + ') = ' + callProper + '(' + inputVariables.join(', ') + ');' + lineTerminator
    end
    definition += functionIndent + '}' + lineTerminator
    return {'definition' => definition, 'call' => call}
  end
end

$context.get('applicationWindow').addMenu(documentation, ['Refactor', 'PHP', 'Extract Method'], ExtractMethodAction.new)

