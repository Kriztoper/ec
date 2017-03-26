package cmsc141.mp1.ec;

import java.util.ArrayList;

public class ECLexer {

	public ECLexer() {
		
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

		/*if (lexemes.get(lexemes.size()-1).equals("end")) {
			lexemes.remove(lexemes.size()-1);
		}*/
		
		System.out.println("Tokenized lexemes\n" + lexemes);
		
		String[] lexemesArr = lexemes.toArray(new String[lexemes.size()]);

		return lexemesArr;
	}
}
