package cmsc141.mp1.ec;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ECUnitTester {

	@Test
	public void test() {
		boolean isTest = true;
		ECSyntaxChecker ecParser = new ECSyntaxChecker(isTest);
				
		// strings should match (syntactically correct) -- true
		// Assignments
		assertTrue(ecParser.hasMatch("main do @var = 1 end"));
		assertTrue(ecParser.hasMatch("main do @var = '1' end"));
		assertTrue(ecParser.hasMatch("main do @var = 'a' end"));
		assertTrue(ecParser.hasMatch("main do @var = '123' end"));
		assertTrue(ecParser.hasMatch("main do @var = 'abc' end"));
		assertTrue(ecParser.hasMatch("main do @var = 'a1b2c3' end"));
		
		// Assignments with Operations
		assertTrue(ecParser.hasMatch("main do 1 + 1 end"));
		assertTrue(ecParser.hasMatch("main do 1 - 1 end"));
		assertTrue(ecParser.hasMatch("main do 1 * 1 end"));
		assertTrue(ecParser.hasMatch("main do 1 / 1 end"));
		assertTrue(ecParser.hasMatch("main do @var = 1 + 1 end"));
		assertTrue(ecParser.hasMatch("main do @var = 1 - 1 end"));
		assertTrue(ecParser.hasMatch("main do @var = 1 * 1 end"));
		assertTrue(ecParser.hasMatch("main do @var = 1 / 1 end"));
		assertTrue(ecParser.hasMatch("main do @var = 2 @res = @var + 1 end"));
		assertTrue(ecParser.hasMatch("main do @var = 2 @res = @var - 1 end"));
		assertTrue(ecParser.hasMatch("main do @var = 2 @res = @var * 1 end"));
		assertTrue(ecParser.hasMatch("main do @var = 2 @res = @var / 1 end"));
		
		// Selection statements
		assertTrue(ecParser.hasMatch("main do if 1 == 1 do  end end"));
		assertTrue(ecParser.hasMatch("main do if 1 != 1 do @var = 1 end end"));
		assertTrue(ecParser.hasMatch("main do if 1 <= 1 do @var = '1' end end"));
		assertTrue(ecParser.hasMatch("main do if 1 < 1 do @var = 'a' end end"));
		assertTrue(ecParser.hasMatch("main do if 1 >= 1 do @var = 'abc' end end"));
		assertTrue(ecParser.hasMatch("main do if 1 > 1 do @var = 'abc123' end end"));
		assertTrue(ecParser.hasMatch("main do if 1 == 1 do  end end"));
		assertTrue(ecParser.hasMatch("main do @var = 'a' if @var == 'a' do end end"));
		assertTrue(ecParser.hasMatch("main do @var = 'a' if @var == 'a' do else do end end"));
		assertTrue(ecParser.hasMatch("main do @var = 'a' if @var == 'a' do "
				+ "else if @var <= 'b' do end end"));
		assertTrue(ecParser.hasMatch("main do @var = 'a' if @var == 'a' do "
				+ "else if @var <= 'b' do else do end end"));
		assertTrue(ecParser.hasMatch("main do @var = 'a' if @var == 'a' do "
				+ "else if @var <= 'b' do @var = 'c' end end"));
		
		// IO
		assertTrue(ecParser.hasMatch("main do scan @var end"));
		assertTrue(ecParser.hasMatch("main do scan @var print @var end"));
		assertTrue(ecParser.hasMatch("main do @var = 1 print @var end"));
		assertTrue(ecParser.hasMatch("main do @var = 1 print @var + 'hello' end"));
		assertTrue(ecParser.hasMatch("main do @var = 1 @var2 = 'a' "
				+ "print @var + @var2 + 'b' end"));
		assertTrue(ecParser.hasMatch("main do @var = 1 @var2 = 'a' "
				+ "print @var + @var2 + '2' end"));
		assertTrue(ecParser.hasMatch("main do scan @var print 'Hello' + @var end"));
		assertTrue(ecParser.hasMatch("main do puts 'Hello' end"));
		assertTrue(ecParser.hasMatch("main do scan @var puts @var end"));
		assertTrue(ecParser.hasMatch("main do @var = 1 puts @var end"));
		assertTrue(ecParser.hasMatch("main do @var = 1 puts @var + 'hello' end"));
		assertTrue(ecParser.hasMatch("main do @var = 1 @var2 = 'a' "
				+ "puts @var + @var2 + 'b' end"));
		assertTrue(ecParser.hasMatch("main do @var = 1 @var2 = 'a' "
				+ "puts @var + @var2 + '2' end"));
		assertTrue(ecParser.hasMatch("main do scan @var puts 'Hello' + @var end"));
		
		// Comment
		assertTrue(ecParser.hasMatch("main do /* This is a comment */ end"));
		assertTrue(ecParser.hasMatch("main do /* This is a comment */ print 'Hello' end"));
		assertTrue(ecParser.hasMatch("main do for @var = 1 ; @var < 3 ; @var = @var + 1 do "
				+ "/* This is a comment */ print 'Hello' end end"));
		assertTrue(ecParser.hasMatch("main do @var = 1 while @var < 3 do "
				+ "/* This is a comment */ print 'Hello' end end"));
		assertTrue(ecParser.hasMatch("main do @var = 1 do /* This is a comment */ "
				+ "print 'Hello' while @var < 3 end end"));
		assertTrue(ecParser.hasMatch("main do @var = 1 if @var == 1 do /* This is a comment */ print 'Hello' end end"));
		assertTrue(ecParser.hasMatch("main do @var = 1 if @var == 1 do /* This is a comment */ "
				+ "print 'Hello' else /* This is a comment too */ end /* Comment again... */ end"));
		assertTrue(ecParser.hasMatch("main do @var = 1 if @var == 1 do /* This is a comment */ "
				+ "print 'Hello' else if @var <= 1 do /* Am a comment */ print 'Hi' "
				+ "else /* This is a comment too */ end /* Comment again... */ end"));
		
		// Iterative statements
		assertTrue(ecParser.hasMatch("main do for @var = 1 ; @var < 3 ; @var = @var + 1 do "
				+ "print 'Hello' end end"));
		assertTrue(ecParser.hasMatch("main do for @var = 1 ; @var < 3 ; @var = @var + 1 do "
				+ "print 'Hello' @var = @var + 2 end end"));
		assertTrue(ecParser.hasMatch("main do @var = 1 while @var < 3 do print 'Hello' "
				+ "@var = @var + 1 end end"));
		assertTrue(ecParser.hasMatch("main do @var = 0 do print 'Hello' @var = @var + 1 "
				+ "while @var < 3 end end"));
		
		// Combinations of all syntax features
		assertTrue(ecParser.hasMatch("main do @var = 2 end"));
		assertTrue(ecParser.hasMatch("main do @var = 2 + 1 end"));
		assertTrue(ecParser.hasMatch("main do @var = 2 + 12 end"));
		assertTrue(ecParser.hasMatch("main do @var = @vAr23a end"));
		assertTrue(ecParser.hasMatch("main do @var = @ve2 - @vA end"));
		assertTrue(ecParser.hasMatch("main do @var = @vAr / @ht end"));
		assertTrue(ecParser.hasMatch("main do @var = @vA * @gf \n@br = 1 + @vd end"));
		assertTrue(ecParser.hasMatch("main do @var = @vA * @gf 1 + @vd end"));
		assertTrue(ecParser.hasMatch("main do if @a and @b do @var = @vA * @gf 1 + @vd end end"));
		assertTrue(ecParser.hasMatch("main do /* 482aha7343 /n jsdhf */ end"));
		assertTrue(ecParser.hasMatch("main do print 'HELL' end"));
		assertTrue(ecParser.hasMatch("main do print @var end"));
		assertTrue(ecParser.hasMatch("main do print @var + 'is yeah.' + @var1 end"));
		assertTrue(ecParser.hasMatch("main do puts 'HELL' end"));
		assertTrue(ecParser.hasMatch("main do puts @var end"));
		assertTrue(ecParser.hasMatch("main do puts @var + 'is yeah.' + @var1 end"));
		assertTrue(ecParser.hasMatch("main do scan @var end"));
		assertTrue(ecParser.hasMatch("main do for @var = 1 ; @var < 1 ; @var + 1 do end end"));
		assertTrue(ecParser.hasMatch("main do while @var == 1 do @var + 1 end end"));
		assertTrue(ecParser.hasMatch("main do while @var == 1 do @var / 1 @var + 1 end end"));
		assertTrue(ecParser.hasMatch("main do do @var * 1 @var + 1 while @var == 1 end end"));
		assertTrue(ecParser.hasMatch("main do if @a <= @b do end end"));
		assertTrue(ecParser.hasMatch("main do if '@a' <= '@b' do end end"));
		assertTrue(ecParser.hasMatch("main do if -12.3 <= 40.1 do end end"));
		assertTrue(ecParser.hasMatch("main do if not -12.3 <= 40.1 do end @d * 23 end"));
		assertTrue(ecParser.hasMatch("main do if not @d != '40.1' do  end end"));
		assertTrue(ecParser.hasMatch("main do if not '12.3' and 40.1 do @a + 1 end end"));
		assertTrue(ecParser.hasMatch("main do if not @ds and 40.1 do @we = '12' end 1 + 2 end"));
		assertTrue(ecParser.hasMatch("main do if not '2' >= 40.1 do @g = 1 + @b @ge = 23 end @s2 + @f end"));
		assertTrue(ecParser.hasMatch("main do if not 12 < @fd do @g = 1 + @b @ge = 23 end @a = 2 end"));
		assertTrue(ecParser.hasMatch("main do if not 12 < @fd do @g = 1 + @b else if @d != @b do @ge = 23 @a = 2 end @a = 2 end"));
		assertTrue(ecParser.hasMatch("main do if not 12 < @fd do @g = 1 + @b else if @d != @b do @ge = 23 end @a = 2 end"));
		assertTrue(ecParser.hasMatch("main do if not 12 < @fd do @g = 1 + @b else if @d != @b do @ge = 23 else do 12 + 3 end @a = 2 end"));
		assertTrue(ecParser.hasMatch("main do if not 12 < @fd do @g = 1 + @b else if @d != @b do @ge = 23 else do 12 + 3 @d = 12 + 2 end @a = 2 end"));
		assertTrue(ecParser.hasMatch("main do if not 12 < @fd do @g = 1 + @b else if @d != @b do @ge = 23 else do 12 + 3 @d = 12 + 2 end @a = 2 23 / 2 end"));
		assertTrue(ecParser.hasMatch("main do if not 12 < @fd do @g = 1 + @b else if @d != @b do @ge = 23 else do 12 + 3 @d = 12 + 2 end @a = 2 23 / 2 "
				+ "/* this is a comment */ end"));
		assertTrue(ecParser.hasMatch("main do if not 12 < @fd do /* awre2r;|@e */ @g = 1 + @b else if @d != @b do @ge = 23 else do /* d2bgs */ 12 + 3 @d = 12 + 2 end @a = 2 23 / 2 "
				+ "/* this is a comment */ end"));
		assertTrue(ecParser.hasMatch("main do do @var * 1 @var + 1 /* Hi po */ while @var == 1 end end"));
		assertTrue(ecParser.hasMatch("main do do @var * 1 @var + 1 /* Hi po */ while @var == 1 end end"));
//		assertTrue(ecParser.hasMatch("main do do @var * 1 @var + 1 /* Hi po */ "
//				+ "if @a < @b do 1 + 1 end while @var == 1 end end"));
//		assertTrue(ecParser.hasMatch("main do if @a >= @b do for @c = 0; @c < 10;  do "
//				+ "1 + 1 end end end"));
		assertTrue(ecParser.hasMatch("main do if @a < @b do print 'Hello po!' end end"));
		assertTrue(ecParser.hasMatch("main do"
				+ "	puts 'Addition program'"
				+ "	print 'Enter num1 = '"
				+ "	scan @num1"
				+ "	print 'Enter num2 = '"
				+ "	scan @num2"
				+ "	@sum = @num1 + @num2"
				+ "	puts 'Sum is ' + @sum "
				+ "end"));
		
		
		// strings should not match (syntactically incorrect) -- false
		assertFalse(ecParser.hasMatch("main d o @var = 2 end")); // must be do
		assertFalse(ecParser.hasMatch("main do @var = 1 + 'as' end")); // strings are not allowed for arithmetic operations
		assertFalse(ecParser.hasMatch("main /* as */ do if not 12 < @fd do @g = 1 + @b else if @d != @b do @ge = 23 else do 12 + 3 @d = 12 + 2 end @a = 2 23 / 2 "
				+ "/* this is a comment */end")); // there should be atleast 1 space before end after comment /*  */
		assertFalse(ecParser.hasMatch("main do /* sd * / end")); // closing commenet tag should be */
		assertFalse(ecParser.hasMatch(" main do /* sd * / end")); // no spaces before main
		assertFalse(ecParser.hasMatch("main do /* sd * / end ")); // no spaces after end of program code or token end of main
	}

}
