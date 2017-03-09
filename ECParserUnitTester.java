package cmsc141.mp1.ec;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ECParserUnitTester {

	@Test
	public void test() {
		// strings should match (syntactically correct) -- true
		assertTrue(new ECParser("main do @var = 2 end").hasMatched());
		
		// strings should not match (syntactically incorrect) -- false
		assertFalse(new ECParser("main d o @var = 2 end").hasMatched());
	}

}
