package cmsc141.mp1.ec;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.events.Comment;

public class ECParser {
	
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
	
	private static String print = "print\\s+(" + word + "|" + string + ")(\\s+\\+\\s+" + "(" + word + "|" + string + "))*";
	private static String print_newline = "puts\\s+(" + word + "|" + string + ")(\\s+\\+\\s+" + "(" + word + "|" + string + "))*";
	private static String scanner = "scan\\s+" + word;
	private static String comment = "/\\*\\s+.*\\s+\\*/";
	private static String operation = arithmetic;
	private static String assignment = word + "\\s+=\\s+(" + operation + "|" + word + "|" + constant + "|" + string + ")*";
	private static String sentence = "((" + assignment + "|" + operation + ")\\s+)*";

	private static String expression = "(" + word + "|" + constant + "|" + string + ")";
	private static String condition = "(not\\s+)?" + expression + "\\s+(and|or|==|!=|<|>|<=|>=)\\s+" + expression;
	private static String iteration = "((for\\s+" + assignment + "\\s*;\\s+" + condition + "\\s*;\\s+" + operation + "\\s+do\\s+" + getParagraph() + "end\\s+)|("
			+  "while\\s+" + condition +  "\\s+do\\s+" + getParagraph() +  "\\s+end\\s+)|("
			+  "do\\s+" + getParagraph() +  "\\s+while\\s+" + condition +  "\\s+end\\s+))*";	
	private static String conditional = "(if\\s+" + condition + "\\s+do\\s+(" + sentence + ")*end\\s+)";
	private static String stmt_block = "(" + conditional + "|" + iteration + ")";
	private static String paragraph = "((" + sentence + "|" + stmt_block + ")*)";
	
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
	
	public static String getExpression() {
		return expression;
	}
	
	public static String getParagraph() {
		return paragraph;
	}
	
	private Pattern compile(String regex) {
		return Pattern.compile(regex);
	}
	
	public Matcher match(String stringPattern) {
		return pattern.matcher(stringPattern);
	}
	
	public ECParser(boolean isTest) {
		initVariables();
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
		initVariables();
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
