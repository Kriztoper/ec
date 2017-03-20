package cmsc141.mp1.ec;

import java.util.ArrayList;
import java.util.Hashtable;

public class ECInterpreter {
	private Hashtable<String, String> variables = new Hashtable();
	
	public ECInterpreter() {
	
	}
	
	public void interpret(String program) {
		String[] words = tokenize(program);
//		String[] words = program.split(" ");
		findVariables(words);
		printVariable(words);
	}
	
	public String[] tokenize(String program) {
		ArrayList<String> words = new ArrayList<String>();
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
				words.add(entity);
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
		words.remove(0);
		words.remove(0);
		
		System.out.println("Tokenized lexemes\n" + words);
		
		String[] wordsArr = words.toArray(new String[words.size()]);

		return wordsArr;
	}
	
	public void findVariables(String[] words) {
		for(int i = 0; i < words.length - 2; i++) {
			if(words[i].contains("@") && 
					words[i+1].equals("=")) {
				System.out.println(words[i] + words[i+2]);
				addVariables(words[i], words[i+2]);
			}
		}
		
		for(int i = 0; i < words.length - 4; i++) {
			if(words[i + 3].equals("+")){
				if(variables.get(words[i]).charAt(0) == '\'') {
					variables.put(words[i], 
						(variables.get(
							words[i])+words[i+4]).replace("''", ""));
				}
			}
		}
	}
	
	public void printVariable(String[] words) {
		for(int i=0; i<words.length-1; i++) {
			if(words[i].contains("print") && 
					words[i+1].charAt(0)=='@') {
				System.out.println("Print Identified: ");
				System.out.println(variables.get(
					words[i+1]).replace("'", ""));
			} else if (words[i].contains("print") && 
					words[i+1].charAt(0)=='\'') {
				System.out.println("Print Identified: ");
				System.out.println(
					words[i+1].replace("'", ""));
			}
		}
	}
	
	public void addVariables(String key, String value) {
		variables.put(key, value);
	}
}
