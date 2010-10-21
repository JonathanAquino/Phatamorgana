class ExtractMethodActionTest < UnitTest

  def run
    extractMethodAction = ExtractMethodAction.new
    original = <<EOS
class Foo {
  public function b{SELECTION-START}{SELECTION-END}ar() {
  }
  public function baz() {
  }
}
EOS
    assertTrue("extractMethod returns nil if caret is on function name", extractMethodAction.extractMethod(original).nil?)

    original = <<EOS
class Foo {
  public function bar() {
    $this->dude();
    {SELECTION-START}var_dump('hello');{SELECTION-END}
  }
  public function baz() {
  }
}
EOS
    expected = <<EOS
class Foo {
  protected function newMethod() {
    var_dump('hello');
  }
  public function bar() {
    $this->dude();
    $this->newMethod();
  }
  public function baz() {
  }
}
EOS
    assertEquals("extractMethod extracts method with no parameters and no return values", expected, extractMethodAction.extractMethod(original))

    original = <<EOS
class Foo {
  public static function bar() {
    {SELECTION-START}var_dump('hello');{SELECTION-END}
  }
  public function baz() {
  }
}
EOS
    expected = <<EOS
class Foo {
  protected static function newMethod() {
    var_dump('hello');
  }
  public static function bar() {
    self::newMethod();
  }
  public function baz() {
  }
}
EOS
    assertEquals("extractMethod extracts static method with no parameters and no return values", expected, extractMethodAction.extractMethod(original))

    original = <<EOS
class Foo {
  public function bar() {
    $a = $b = $c = $d = 5;
    {SELECTION-START}$a++;
    $b = 6; $f = 'foo';
    var_dump($c);{SELECTION-END}
  }
  public function baz() {
  }
}
EOS
    expected = <<EOS
class Foo {
  protected function newMethod($a, $b, $c) {
    $a++;
    $b = 6; $f = 'foo';
    var_dump($c);
  }
  public function bar() {
    $a = $b = $c = $d = 5;
    $this->newMethod($a, $b, $c);
  }
  public function baz() {
  }
}
EOS
    assertEquals("extractMethod extracts method with some parameters and no return values", expected, extractMethodAction.extractMethod(original))

    original = <<EOS
class Foo {
  public function bar() {
    $a = $b = $c = $d = 5;
    {SELECTION-START}$a++;
    $b = 6;
    var_dump($c);{SELECTION-END}
    var_dump($a, $b);
  }
  public function baz() {
  }
}
EOS
    expected = <<EOS
class Foo {
  protected function newMethod($a, $b, $c) {
    $a++;
    $b = 6;
    var_dump($c);
    return array($a, $b);
  }
  public function bar() {
    $a = $b = $c = $d = 5;
    list($a, $b) = $this->newMethod($a, $b, $c);
    var_dump($a, $b);
  }
  public function baz() {
  }
}
EOS
    assertEquals("extractMethod extracts method with some parameters and some return values", expected, extractMethodAction.extractMethod(original))

    original = <<EOS
class Foo {
  public function bar() {
    $a = $b = $c = $d = 5;
    {SELECTION-START}$a++;
    $b = 6;
    var_dump($c);{SELECTION-END}
    var_dump($a, $k);
  }
  public function baz() {
  }
}
EOS
    expected = <<EOS
class Foo {
  protected function newMethod($a, $b, $c) {
    $a++;
    $b = 6;
    var_dump($c);
    return $a;
  }
  public function bar() {
    $a = $b = $c = $d = 5;
    $a = $this->newMethod($a, $b, $c);
    var_dump($a, $k);
  }
  public function baz() {
  }
}
EOS
    assertEquals("extractMethod extracts method with some parameters and one return value", expected, extractMethodAction.extractMethod(original))

    original = <<EOS
class Foo {
  public function bar() {
    $a = $b = $c = $d = 5;
    {SELECTION-START}$a++;
    $b == 99999;
    var_dump($c);{SELECTION-END}
  }
  public function baz() {
  }
}
EOS
    expected = <<EOS
class Foo {
{FUNCTION-BOUNDARY}  public function bar() {
    $a = $b = $c = $d = 5;
    {SELECTION-START}$a++;
    $b == 99999;
    var_dump($c);{SELECTION-END}
  }
{FUNCTION-BOUNDARY}  public function baz() {
  }
}
{FUNCTION-BOUNDARY}
EOS
    assertEquals("addFunctionBoundaryMarkers inserts tokens", expected.strip, extractMethodAction.addFunctionBoundaryMarkers(original))

    original = <<EOS
class Foo {
{FUNCTION-BOUNDARY}  public function bar() {
    $a = $b = $c = $d = 5;
    {SELECTION-START}$a++;
    $b == 99999;
    var_dump($c);{SELECTION-END}
  }
{FUNCTION-BOUNDARY}  public function baz() {
  }
}
{FUNCTION-BOUNDARY}
EOS
    expected = <<EOS
class Foo {
  public function bar() {
    $a = $b = $c = $d = 5;
    $a++;
    $b == 99999;
    var_dump($c);
  }
  public function baz() {
  }
}

EOS
    assertEquals("removeMarkers removes all tokens", expected, extractMethodAction.removeMarkers(original))

    original = '$a $B_ $this $9 $9'
    expected = ['$a', '$B_', '$9']
    assertEquals("extractVariables finds all variables", expected, extractMethodAction.extractVariables(original))

    original = '$aa; $bb++; $c--; $d += 1; $e-=1; $f=1; $g==1; $h===1; $i&=1; $j|=1; $k*=1; $l/=1; $m%=1; $n<<=1; $o>>=1; $p<<1; $q^=1; $r[]=1; $s["foo"]=1'
    expected = ['$bb', '$c', '$d', '$e', '$f', '$i', '$j', '$k', '$l', '$m', '$n', '$o', '$q', '$r', '$s']
    assertEquals("extractAssignedVariables finds all assigned variables", expected, extractMethodAction.extractAssignedVariables(original))
  end

end

$context.get("unitTests").add(ExtractMethodActionTest.new);
