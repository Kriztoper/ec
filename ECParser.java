package cmsc141.mp1.ec;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ECParser {

	private static String comment = "(\\s*/\\*\\s+(.)*\\s+\\*/\\s*)*";
	private static String lower_alpha = "[a-z]";
	private static String upper_alpha = "[A-Z]";
	private static String number = "[0-9]";
	private static String string = "'.*'";
	private static String constant = "-?" + number + "+(\\." + number + "+)?";
	private static String word = "(@" + lower_alpha + "(" + upper_alpha + "|" + lower_alpha + "|" + number + ")*)";
	private static String term = "(" + constant + "|" + word + ")";//"(" + constant + "|" + string + ")";
	private static String hi_order_op = "(/|\\*|%)";
	private static String arith_op = "(\\+|-|" + hi_order_op + ")";
	private static String arithmetic = term + "\\s+" + arith_op + "\\s+" + term;
	private static String iteration = "";
	private static String func_call = "";
	private static String operation = "(" + func_call + "|" + arithmetic + ")";
	private static String assignment = word + "\\s+=\\s+(" + operation + "|" + word + "|" + constant + "|" + string + ")*";
	private static String sentence = "((" + assignment + "|" + operation + "|" + comment + ")\\s+)*";

	private static String expression = "(" + word + "|" + constant + "|" + string + ")";
	private static String condition = "(not\\s+)?" + expression + "\\s+(and|or|==|!=|<|>|<=|>=)\\s+" + expression;
	
	private static String conditional = "(if\\s+" + condition + "\\s+do\\s+(" + sentence + ")*"
			+ "(else if\\s+" + condition + "\\s+do\\s+("+ sentence +")*)*(else\\s+do\\s+" + sentence + ")?end\\s+)";
	private static String stmt_block = "(" + conditional + "|" + iteration + "|" + comment + ")";
	private static String paragraph = "((" + sentence + "|" + stmt_block + "|" + comment + ")*)";
	
	private static String main = "(^main\\s+do\\s+" + paragraph + "end$)";
	private static String program  = main;

	private Pattern pattern;
	private Matcher matcher;
	
	public String getMatchedPattern(String testString) {
		matcher = match(testString);
		String matchedString = "oops";
		while (matcher.find()) {
			matchedString += matcher.group(1);
		}
		System.out.println("editedString = "+matchedString);
		return matchedString;
	}
	
	private Pattern compile(String regex) {
		return Pattern.compile(regex);
	}
	
	public Matcher match(String stringPattern) {
		return pattern.matcher(stringPattern);
	}
	
	public ECParser(boolean isTest) {
		//initVariables();
		pattern = compile(program);
	}

	public boolean hasMatch(String testString) {
		matcher = match(testString);
		return matcher.find();
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
	
	public ECParser() {
		Scanner scanner = new Scanner(System.in);
		String input = "";
		//initVariables();
		pattern = compile(program);
		
		System.out.println(pattern.pattern());
		while (!input.equals("e")) {
			System.out.println("Enter a sentence (\"e to exit\"): ");
			input = scanner.nextLine();
			matcher = match(input);
			
			if (matcher.find() == true)
				System.out.println("yey!");
			else
				System.out.println("boo!");
		}
	}
	
	public static void main(String[] args) {
		new ECParser();
	}
}
