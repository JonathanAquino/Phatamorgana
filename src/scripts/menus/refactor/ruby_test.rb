# Name: JRuby Smoke Test
# Description: Smoke test that checks that JRuby unit tests are working
# Author: Jonathan Aquino

class JRubySmokeTest < UnitTest

	def run
		assertTrue("JRuby smoke test", true)
	end

end

context.get("unitTests").add(JRubySmokeTest.new);