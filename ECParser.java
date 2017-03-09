package cmsc141.mp1.ec;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ECParser {
	
	private String lower_alpha = "[a-z]";
	private String upper_alpha = "[A-Z]";
	private String number = "[0-9]";
	private String string = "'.*'";
	private String constant = "-?" + number + "+(\\." + number + "+)?";
	private String word = "@" + lower_alpha + "(" + upper_alpha + "|" + lower_alpha + "|" + number + ")*";
	private String term = "(" + constant + "|" + word + ")";//"(" + constant + "|" + string + ")";
	private String hi_order_op = "(/|\\*|%)";
	private String arith_op = "(\\+|-|" + hi_order_op + ")";

	private String iteration = "";
	private String assignment = word + "\\s+=\\s+(" + operation + "|" + word + "|" + constant + "|" + string + ")*";
	private String expression = "(not)?\\s+(" + condition + "|" + word + "|" + constant + "|" + string + ")";
	private String condition = expression + "\\s+(and|or|==|!=|<|>|<=|>=)\\s+" + expression;
	private String conditional = "if\\s+" + condition + "\\s+" + paragraph + "\\s+end";
	private String sentence = "((" + assignment + "|" + operation + ")\\s+)*";
	private String stmt_block = "(" + conditional + "|" + iteration + ")";
	private String func_call = "";
	private String arithmetic = term + "\\s+" + arith_op + "\\s+" + term;
	private String operation = "(" + func_call + "|" + arithmetic + ")";
	private String paragraph = "(" + sentence + "|" + stmt_block + ")*";
	private String main = "^main\\s+do\\s+" + paragraph + "end$";
	private String program  = main;

	private Pattern pattern;
	private Matcher matcher;
	
	private Pattern compile(String regex) {
		return Pattern.compile(regex);
	}
	
	public Matcher match(String stringPattern) {
		return pattern.matcher(stringPattern);
	}
	
	public ECParser(boolean isTest) {
		pattern = compile(program);
	}

	public boolean hasMatch(String testString) {
		matcher = match(testString);
		return matcher.find();
	}
	
	public ECParser() {
		Scanner scanner = new Scanner(System.in);
		String input = "";
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
