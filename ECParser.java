package cmsc141.mp1.ec;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ECParser {
	
	private String lower_alpha = "[a-z]";
	private String upper_alpha = "[A-Z]";
	private String number = "[0-9]";
	private String func_call = "";
	private String string = "'.*'";
	private String constant = "-?" + number + "+(\\." + number + "+)?";
	private String term = constant;//"(" + constant + "|" + string + ")";
	private String hi_order_op = "(/|\\*|%)";
	private String arith_op = "(\\+|-|" + hi_order_op + ")";
	private String arithmetic = term + "\\s*" + arith_op + "\\s*" + term;
	private String word = "@" + lower_alpha + "(" + upper_alpha + "|" + lower_alpha + "|" + number + ")*";
	private String operation = "(" + func_call + "|" + arithmetic + ")";
	private String conditional = "";
	private String iteration = "";
	private String stmt_block = "(" + conditional + "|" + iteration + ")";
	private String assignment = word + "\\s*=\\s*(" + operation + "|" + word + "|" + constant + "|" + string + ")*";
	private String sentence = "((" + assignment + "|" + operation + ")\\s+)*";
	private String paragraph = "(" + sentence + "|" + stmt_block + ")*";
	private String main = "^main\\s+do\\s+" + paragraph + "end$";
	private String program  = main;

	private Pattern pattern;
	private Matcher matcher;
	
	private String testString;
	
	private Pattern compile(String regex) {
		return Pattern.compile(regex);
	}
	
	public Matcher match(String stringPattern) {
		return pattern.matcher(stringPattern);
	}
	
	public ECParser(String testString) {
		this.testString = testString;
		pattern = compile(program);
		matcher = match(this.testString);
	}
	
	public boolean hasMatched() {
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
