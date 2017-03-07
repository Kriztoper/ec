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
	private String term = "(" + constant + "|" + string + ")";
	private String hi_order_op = "(/|\\*|%)";
	private String arith_op = "(\\+|-|" + hi_order_op + ")";
	private String arithmetic = term + arith_op + term;
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

	public ECParser() {
		Scanner scanner = new Scanner(System.in);
		String input = "";
		Pattern pattern = Pattern.compile(program);
		Matcher matcher;
		
		while (!input.equals("e")) {
			System.out.println("Enter a sentence (\"e to exit\"): ");
			input = scanner.nextLine();
			matcher = pattern.matcher(input);
			
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
