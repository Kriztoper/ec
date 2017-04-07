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
	//private static String stmt_block = "(" + conditional + "|" + iteration + ")";
	//private static String paragraph = "((" + sentence + "|" + stmt_block + ")*)";
	
	private static String main = "(^\\s*main\\s+do\\s+" + paragraph + "end\\s*$)";
	private static String program  = main;

	private String inputString;
	private Pattern pattern;
	private Matcher matcher;
	
	public String getMatchedPattern(String testString) {
		//matcher = match(testString);
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
		inputString = stringPattern;
		RegularExpressionUtils regularExpressionUtils =
				new RegularExpressionUtils();
		matcher = 
				regularExpressionUtils.createMatcherWithTimeout(
				stringPattern, pattern, 5000);
		return matcher.find();
		//return pattern.matcher(stringPattern).find();
	}
	
	public ECSyntaxChecker(boolean isTest) {
		//initVariables();
		pattern = compile(program);
	}

	public boolean hasMatch(String testString) {
		return match(testString);
	}
	
	private void initVariables() {
		/*conditional = "(if\\s+" + condition + "\\s+do\\s+" + 
				"(" + sentence + "|" + stmt_block + ")*" + "end\\s+";
		stmt_block = "(" + conditional + "|" + iteration + ")";
		paragraph = "(" + sentence + "|" + stmt_block + ")*";
		
		main = "(^main\\s+do\\s+" + paragraph + "end$)";
		cond = "(if\\s+@[a-z]\\s+(and|or|==|!=|<|>|<=|>=)\\s+@[a-z]\\s+do\\s+end\\s*)";
		program  = main;*/
	}
	
	public ECSyntaxChecker() {
		//ECInterpreter ecInterpreter = new ECInterpreter();
		//Scanner scanner = new Scanner(System.in);

		pattern = compile(program);

/*		Scanner scanner = new Scanner(System.in);
		String input = "";
		//initVariables();
		pattern = compile(program);
		
		System.out.println(pattern.pattern());
		while (!input.equals("e")) {
			System.out.println("Enter a sentence (\"e to exit\"): ");
			input = scanner.nextLine();
			ecInterpreter.interpret(input);
			matcher = match(input);
			
			if (matcher.find() == true)
				System.out.println("yey!");
			else
				System.out.println("boo!");
		}*/
	}
	
	/*public static void main(String[] args) {
		new ECSyntaxChecker();
	}*/
	
	class RegularExpressionUtils {

		// demonstrates behavior for regular expression running into catastrophic backtracking for given input
		/*public static void main(String[] args) {
			Matcher matcher = createMatcherWithTimeout(
					"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", "(x+x+)+y", 2000);
			System.out.println(matcher.matches());
		}*/

		public /*static*/ Matcher createMatcherWithTimeout(String stringToMatch, String regularExpression, int timeoutMillis) {
			Pattern pattern = Pattern.compile(regularExpression);
			return createMatcherWithTimeout(stringToMatch, pattern, timeoutMillis);
		}

		public /*static*/ Matcher createMatcherWithTimeout(String stringToMatch, Pattern regularExpressionPattern, int timeoutMillis) {
			CharSequence charSequence = new TimeoutRegexCharSequence(stringToMatch, timeoutMillis, stringToMatch,
					regularExpressionPattern.pattern());
			return regularExpressionPattern.matcher(charSequence);
		}

		private /*static*/ class TimeoutRegexCharSequence implements CharSequence {

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
					//System.out.println(inner.charAt(index));
					/*int i = 0;
					while(i < matcher.groupCount()) {
						System.out.println(matcher.group(i));
						i++;
					}*/
					return 0;
//					int indexOfLastMatch = indexOfLastMatch(pattern, inputString);
//					System.out.println(inputString.charAt(indexOfLastMatch));
//					throw new RuntimeException("Timeout occurred after " + 
//							timeoutMillis + "ms while processing regular expression '" + 
//							regularExpression + "' on input '" + stringToMatch + "'!");
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
