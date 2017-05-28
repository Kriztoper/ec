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
		
		for (int i = 0; i < program.length(); i++) {
			char currentChar = program.charAt(i);
			if (Character.isWhitespace(currentChar) &&
					!entity.equals("") && !hasStartCommentTag &&
					!hasStartStringTag) {
				lexemes.add(entity);
				entity = "";
			} else if (Character.isWhitespace(currentChar) &&
					!entity.equals("") && !hasStartCommentTag &&
					hasStartStringTag) {
				entity += currentChar + "";
			} else if (currentChar == '\'' && 
					!hasStartStringTag && !hasStartCommentTag) {
				entity += currentChar + "";
				hasStartStringTag = true;
			} else if (currentChar == '\'' && 
					hasStartStringTag && !hasStartCommentTag) {
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
				entity += currentChar + "";
			}
		}
		lexemes.remove(0);
		lexemes.remove(0);

		// add 'end' at the end
		lexemes.add(entity);
				
		System.out.println("Tokenized lexemes\n" + lexemes);
		
		String[] lexemesArr = lexemes.toArray(new String[lexemes.size()]);

		return lexemesArr;
	}
}
