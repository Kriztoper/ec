package cmsc141.mp1.ec;

import java.util.ArrayList;
import java.util.Hashtable;

public class ECInterpreter {
	private Hashtable<String, String> variables = new Hashtable();
	
	public ECInterpreter() {
	
	}
	
	public void interpret(String program) {
		String[] lexemes = tokenize(program);
//		String[] words = program.split(" ");
		findVariables(lexemes);
		printVariable(lexemes);
	}
	
	public String[] tokenize(String program) {
		ArrayList<String> lexemes = new ArrayList<String>();
		String entity = "";
		boolean hasStartCommentTag = false;
		boolean hasStartStringTag = false;
		
//		System.out.println("Program code:\n" + program);
		
		for (int i = 0; i < program.length(); i++) {
			char currentChar = program.charAt(i);
//			System.out.println("current char = " + currentChar + 
//					", isComment = " + hasStartCommentTag + ", " +
//					"isString = " + hasStartStringTag);
			if (Character.isWhitespace(currentChar) &&
					!entity.equals("") && !hasStartCommentTag &&
					!hasStartStringTag) {
//				System.out.println(currentChar + " is not added");
				lexemes.add(entity);
				entity = "";
			} else if (Character.isWhitespace(currentChar) &&
					!entity.equals("") && !hasStartCommentTag &&
					hasStartStringTag) {
//				System.out.println(currentChar + " is added to entity");
				entity += currentChar + "";
			} else if (currentChar == '\'' && 
					!hasStartStringTag && !hasStartCommentTag) {
//				System.out.println(currentChar + " is encountered");
				entity += currentChar + "";
				hasStartStringTag = true;
			} else if (currentChar == '\'' && 
					hasStartStringTag && !hasStartCommentTag) {
//				System.out.println(currentChar + " is encountered");
				entity += currentChar + "";
				hasStartStringTag = false;
			} else if (currentChar == '/' && 
					program.charAt(i+1) == '*' && 
					!hasStartCommentTag && !hasStartStringTag) {
				hasStartCommentTag = true;
				i++;
			} else if (currentChar == '*' && 
					program.charAt(i+1) == '/' &&
					hasStartCommentTag && !hasStartStringTag) {
				hasStartCommentTag = false;
				i++;
			} else if (!Character.isWhitespace(currentChar) &&
					!hasStartCommentTag) {
//				System.out.println(currentChar + " is added to entity");
				entity += currentChar + "";
			}
		}
		lexemes.remove(0);
		lexemes.remove(0);
		
		System.out.println("Tokenized lexemes\n" + lexemes);
		
		String[] lexemesArr = lexemes.toArray(new String[lexemes.size()]);

		return lexemesArr;
	}
	
	public void findVariables(String[] lexemes) {
		for(int i = 0; i < lexemes.length - 2; i++) {
			if(lexemes[i].contains("@") && 
					lexemes[i+1].equals("=")) {
				System.out.println(lexemes[i] + lexemes[i+2]);
				addVariables(lexemes[i], lexemes[i+2]);
			}
		}
		
		for(int i = 0; i < lexemes.length - 4; i++) {
			if(lexemes[i + 3].equals("+")){
				if(variables.get(lexemes[i]).charAt(0) == '\'') {
					variables.put(lexemes[i], 
						(variables.get(
							lexemes[i])+lexemes[i+4]).replace("''", ""));
				}
			}
		}
	}
	
	public void printVariable(String[] lexemes) {
		for(int i=0; i<lexemes.length-1; i++) {
			if(lexemes[i].contains("print") && 
					lexemes[i+1].charAt(0)=='@') {
				System.out.println("Print Identified: ");
				System.out.println(variables.get(
					lexemes[i+1]).replace("'", ""));
			} else if (lexemes[i].contains("print") && 
					lexemes[i+1].charAt(0)=='\'') {
				System.out.println("Print Identified: ");
				System.out.println(
					lexemes[i+1].replace("'", ""));
			}
		}
	}
	
	public void addVariables(String key, String value) {
		variables.put(key, value);
	}
}
