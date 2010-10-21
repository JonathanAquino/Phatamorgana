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
      return
    end
    changeSet = ChangeSet.new
    changeSet.setContents($context.get('currentSourceFile'), newContents)
    $context.get('applicationWindow').execute(changeSet)
  end

  # Returns the text with the selected code extracted, or nil if the extraction
  # could not be performed.
  def extractMethod(text)
    return 'Foo!'
  end
end

$context.get('applicationWindow').addMenu(documentation, ['Refactor', 'PHP', 'Extract Method'], ExtractMethodAction.new)

