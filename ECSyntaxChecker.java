package cmsc141.mp1.ec;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ECSyntaxChecker {

	private static String comment = "(\\s*/\\*\\p{Blank}+(.)*\\p{Blank}+\\*/\\s*)*";
	private static String lower_alpha = "[a-z]";
	private static String upper_alpha = "[A-Z]";
	private static String number = "[0-9]";
	private static String string = "'.*'";
	
	private static String constant = "-?" + number + "+(\\." + number + "+)?";
	private static String word = "(@" + lower_alpha + "(" + upper_alpha + "|" + lower_alpha + "|" + number + ")*)";
	private static String term = "(" + constant + "|" + word + ")";//"(" + constant + "|" + string + ")";
	
	private static String hi_order_op = "(/|\\*|%)";
	private static String arith_op = "(\\+|-|" + hi_order_op + ")";
	private static String arithmetic = term + "\\p{Blank}+" + arith_op + "\\p{Blank}+" + term;
	
	private static String print = "(print\\p{Blank}+(" + word + "|" + string + ")(\\s+\\+\\s+" + "(" + word + "|" + string + "))*)";
	private static String print_newline = "(puts\\p{Blank}+(" + word + "|" + string + ")(\\s+\\+\\s+" + "(" + word + "|" + string + "))*)";
	private static String scanner = "scan\\p{Blank}+" + word;
	private static String operation = arithmetic;
	private static String assignment = word + "\\p{Blank}+=\\p{Blank}+(" + operation + "|" + word + "|" + constant + "|" + string + ")";
	private static String sentence = "((" + assignment + "|" + operation + "|" + comment + "|" +
			print + "|" + print_newline + "|" + scanner + ")\\s+)*";

	private static String expression = "(" + word + "|" + constant + "|" + string + ")";
	private static String condition = "(not\\p{Blank}+)?" + expression + "\\p{Blank}+(and|or|==|!=|<|>|<=|>=)\\p{Blank}+" + expression;
	private static String iteration = "((for\\p{Blank}+" + assignment + "\\p{Blank}+;\\p{Blank}+" + condition + "\\p{Blank}+;\\p{Blank}+(" + operation + 
			"|" + assignment + ")*\\s+do\\s+" + sentence + "end\\s+)|(" + 
			"while\\p{Blank}+" + condition +  "\\s+do\\s+" + sentence +  "end\\s+)|(" + 
			"do\\s+" + sentence +  "while\\p{Blank}+" + condition +  "\\p{Blank}+end\\s+))*";	
	private static String conditional = "(if\\p{Blank}+" + condition + "\\s+do\\s+" + sentence +  
			"(else if\\p{Blank}+" + condition + "\\s+do\\s+"+ sentence +")*(else\\s+do\\s+" + sentence + ")?end\\s+)";
	private static String stmt_block = "(" + conditional + "|" + iteration + "|" + comment + ")";
	private static String paragraph = "((" + sentence + "|" + stmt_block + ")*)";
	
	private static String main = "(^\\s*main\\s+do\\s+" + paragraph + "end\\s*$)";
	private static String program  = main;

	private Pattern pattern;
	private Matcher matcher;
	
	public String getMatchedPattern(String testString) {
		String matchedString = "oops";
		while (match(testString)) {
			matchedString += matcher.group(1);
		}
		System.out.println("editedString = "+matchedString);
		return matchedString;
	}
	
	private Pattern compile(String regex) {
		pattern = Pattern.compile(regex);
		return pattern;
	}
	
	public boolean match(String stringPattern) {
		RegularExpressionUtils regularExpressionUtils =
				new RegularExpressionUtils();
		matcher = 
				regularExpressionUtils.createMatcherWithTimeout(
				stringPattern, pattern, 5000);
		return matcher.find();
	}
	
	public ECSyntaxChecker(boolean isTest) {
		pattern = compile(program);
	}

	public boolean hasMatch(String testString) {
		return match(testString);
	}
		
	public ECSyntaxChecker() {
		pattern = compile(program);
	}

	class RegularExpressionUtils {

		public Matcher createMatcherWithTimeout(String stringToMatch, String regularExpression, int timeoutMillis) {
			Pattern pattern = Pattern.compile(regularExpression);
			return createMatcherWithTimeout(stringToMatch, pattern, timeoutMillis);
		}

		public Matcher createMatcherWithTimeout(String stringToMatch, Pattern regularExpressionPattern, int timeoutMillis) {
			CharSequence charSequence = new TimeoutRegexCharSequence(stringToMatch, timeoutMillis, stringToMatch,
					regularExpressionPattern.pattern());
			return regularExpressionPattern.matcher(charSequence);
		}

		private class TimeoutRegexCharSequence implements CharSequence {

			private final CharSequence inner;

			private final int timeoutMillis;

			private final long timeoutTime;

			private final String stringToMatch;

			private final String regularExpression;

			public TimeoutRegexCharSequence(CharSequence inner, int timeoutMillis, String stringToMatch, String regularExpression) {
				super();
				this.inner = inner;
				this.timeoutMillis = timeoutMillis;
				this.stringToMatch = stringToMatch;
				this.regularExpression = regularExpression;
				timeoutTime = System.currentTimeMillis() + timeoutMillis;
			}

			// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			public int indexOfLastMatch(Pattern pattern, String input) {
		        for (int i = input.length(); i > 0; --i) {
		        	Matcher region = matcher.region(0, i);
		            if (region.matches() || region.hitEnd()) {
		            	return i;
		            }
		        }

		        return 0;
			}
			// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			
			@Override
			public char charAt(int index) {
				if (System.currentTimeMillis() > timeoutTime) {
					return 0;
				}
				return inner.charAt(index);
			}

			@Override
			public int length() {
				return inner.length();
			}

			@Override
			public CharSequence subSequence(int start, int end) {
				return new TimeoutRegexCharSequence(
						inner.subSequence(start, end), timeoutMillis, 
						stringToMatch, regularExpression);
			}

			@Override
			public String toString() {
				return inner.toString();
			}
		}
	}
}
