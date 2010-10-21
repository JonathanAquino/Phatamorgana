class JRubySmokeTest < UnitTest

	def run
		assertTrue("JRuby smoke test", true)
	end

end

context.get("unitTests").add(JRubySmokeTest.new);