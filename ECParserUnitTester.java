package cmsc141.mp1.ec;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ECParserUnitTester {

	@Test
	public void test() {
		boolean isTest = true;
		ECParser ecParser = new ECParser(isTest);
		
		// strings should match (syntactically correct) -- true
		assertTrue(ecParser.hasMatch("main do @var = 2 end"));
		assertTrue(ecParser.hasMatch("main do @var = 2 + 1 end"));
		assertTrue(ecParser.hasMatch("main do @var = 2 + 12 end"));
		assertTrue(ecParser.hasMatch("main do @var = @vAr23a end"));
		assertTrue(ecParser.hasMatch("main do @var = @ve2 - @vA end"));
		assertTrue(ecParser.hasMatch("main do @var = @vAr / @ht end"));
		assertTrue(ecParser.hasMatch("main do @var = @vA * @gf @br = 1 + @vd end"));
		assertTrue(ecParser.hasMatch("main do @var = @vA * @gf 1 + @vd end"));
		assertTrue(ecParser.hasMatch("main do if @a <= @b do end end"));
		assertTrue(ecParser.hasMatch("main do if '@a' <= '@b' do end end"));
		assertTrue(ecParser.hasMatch("main do if -12.3 <= 40.1 do end end"));
		assertTrue(ecParser.hasMatch("main do if not -12.3 <= 40.1 do end @d * 23 end"));
		assertTrue(ecParser.hasMatch("main do if not @d != '40.1' do  end end"));
		assertTrue(ecParser.hasMatch("main do if not '12.3' and 40.1 do @a + 1 end end"));
		assertTrue(ecParser.hasMatch("main do if not @ds and 40.1 do @we = '12' end 1 + 2 end"));
		assertTrue(ecParser.hasMatch("main do if not '2' >= 40.1 do @g = 1 + @b @ge = 23 end @s2 + @f end"));
		assertTrue(ecParser.hasMatch("main do if not 12 < @fd do @g = 1 + @b @ge = 23 end @a = 2 end"));
		
		// strings should not match (syntactically incorrect) -- false
		assertFalse(ecParser.hasMatch("main d o @var = 2 end")); // must be do
		assertFalse(ecParser.hasMatch("main do @var = 1 + 'as' end")); // strings are not allowed for arithmetic operations
		
	}

}
