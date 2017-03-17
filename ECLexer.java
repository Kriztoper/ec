package cmsc141.mp1.ec;

import java.util.Hashtable;

public class ECLexer {
	private Hashtable<String, String> variables = new Hashtable();
	
	public ECLexer(String program) {
		readProgram(program);
	}
	
	public void readProgram(String program) {
		String[] words = new String[1000000];
		
		words = program.split(" ");
		findVariables(words);
		printVariable(words);
	}
	
	public void findVariables(String[] words) {
		for(int i=0; i<words.length-2; i++) {
			if(words[i].contains("@")&&words[i+1].equals("=")) {
				System.out.println(words[i] + words[i+2]);
				addVariables(words[i], words[i+2]);
			}
		}
		
		for(int i=0; i<words.length-4; i++) {
			if(words[i+3].equals("+")){
				if(variables.get(words[i]).charAt(0)=='\'') {
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
