package cmsc141.mp1.ec;

import java.util.Hashtable;

public class ECInterpreter {
	private Hashtable<String, String> variables = new Hashtable();
	private boolean isExecutable;
	
	public ECInterpreter() {
		isExecutable = true;
	}
	
	public String interpret(String program) {
		ECLexer ecLexer = new ECLexer();
		String[] lexemes = ecLexer.tokenize(program);
//		String[] words = program.split(" ");
		//findVariables(lexemes);
		String output = analyzeLexemes(lexemes);
		//printVariable(lexemes);
		return output;
	}
	
	public String analyzeLexemes(String[] lexemes) {
		String output = "";
		for (int i = 0; i < lexemes.length; i++) {
			
			if (lexemes[i].startsWith("@") && 
					lexemes[i+1].equals("=")) {
				if (variables.containsKey(lexemes[i])) {
					variables.replace(lexemes[i], lexemes[i+2]);
				} else {
					variables.put(lexemes[i], lexemes[i+2]);
				}
			} else if (lexemes[i].equals("if") && 
					!lexemes[i+1].equals("not")) { // if without a not in condition
				System.out.println("Found an if without a not");
				String leftExpr = lexemes[i+1]; // left expression
				String relOptr = lexemes[i+2]; // relational operator
				String rightExpr = lexemes[i+3]; // right expression
				
				if (checkCondition(leftExpr, relOptr, rightExpr)) {
					System.out.println("Condition is satisfied");
					isExecutable = true;
					i += 3;
					continue;
				} else {
					System.out.println("Condition is not satisfied");
					isExecutable = false;
					i += 3;
					continue;
				}
			} else if (lexemes[i].equals("if") && 
					lexemes[i+1].equals("not")) { // if with a not in condition
				System.out.println("Found an if with a not");
				String leftExpr = lexemes[i+2]; // left expression
				String relOptr = lexemes[i+3]; // relational operator
				String rightExpr = lexemes[i+4]; // right expression
				
				if (!checkCondition(leftExpr, relOptr, rightExpr)) {
					System.out.println("Condition is satisfied");
					isExecutable = true;
					i += 4;
					continue;
				} else {
					System.out.println("Condition is not satisfied");
					isExecutable = false;
					i += 4;
					continue;
				}
			} else if (lexemes[i].equals("else ")) {
				
			} else if (lexemes[i].equals("print")) {
				int offsetToAdd = 0;
				String stringToPrint = "";
				
				if (lexemes[i+1].contains("'")) {
					String string = lexemes[i+1];
					string = string.substring(1, string.length()-1);
					stringToPrint += string;
					offsetToAdd++;
				} else if (lexemes[i+1].startsWith("@")) {
					String string = getValueOfVariable(lexemes[i+1]);
					if (string.contains("'")) {
						string = string.substring(1, string.length()-1);
					}
					stringToPrint += string;
					offsetToAdd++;
				}
				
				for (int j = 2; (i+j) < lexemes.length; j++) {
					if (lexemes[i+j].equals("+")) {
						j++;
						if (lexemes[i+j].contains("'")) {
							String string = lexemes[i+j];
							string = string.substring(1, string.length()-1);
							stringToPrint += string;
							offsetToAdd += 2;
						} else if (lexemes[i+j].startsWith("@")) {
							String string = getValueOfVariable(lexemes[i+j]);
							if (string.contains("'")) {
								string = string.substring(1, string.length()-1);
							}
							stringToPrint += string;
							offsetToAdd += 2;
						}
					} else {
						break;
					}
				}
//					System.out.println("amma print");
				output += stringToPrint;
				i += offsetToAdd;
				continue;
			}
			
			if (lexemes[i].equals("end")) {
				System.out.println("Found an end");
				isExecutable = true;
			}
		}
		
		return output;
	}
	
	public boolean checkCondition(String leftExpr, String relOptr, String rightExpr) {
		if (leftExpr.startsWith("@") && rightExpr.startsWith("@")) {
			System.out.println("Both left and right expressions are "
					+ "variables");
			if (getValueOfVariable(leftExpr).contains("'") &&
					getValueOfVariable(rightExpr).contains("'")) {
				System.out.println("Both left and right expressions are "
						+ "string variables");
				if (relOptr.equals("==")) {
					System.out.println("operator is ==");
					if (getValueOfVariable(leftExpr).equals(
							getValueOfVariable(rightExpr))) {
						System.out.println("Both" + 
								" are equal");
						return true;
					} else {
						System.out.println("Both" + 
								" are not equal");
						return false;
					}
				} else if (relOptr.equals("!=")) {
					if (!getValueOfVariable(leftExpr).equals(
							getValueOfVariable(rightExpr))) {
						return true;
					} else {
						return false;
					}
				} else if (relOptr.equals("<")) {
					if (getValueOfVariable(leftExpr).compareTo(
							getValueOfVariable(rightExpr)) == -1) {
						return true;
					} else {
						return false;
					}
				} else if (relOptr.equals("<=")) {
					if (getValueOfVariable(leftExpr).compareTo(
							getValueOfVariable(rightExpr)) == -1 ||
							getValueOfVariable(leftExpr).compareTo(
									getValueOfVariable(rightExpr)) == 0) {
						return true;
					} else {
						return false;
					}
				} else if (relOptr.equals(">")) {
					if (getValueOfVariable(leftExpr).compareTo(
							getValueOfVariable(rightExpr)) == 1) {
						return true;
					} else {
						return false;
					}
				} else if (relOptr.equals(">=")) {
					if (getValueOfVariable(leftExpr).compareTo(
							getValueOfVariable(rightExpr)) == 1 ||
							getValueOfVariable(leftExpr).compareTo(
									getValueOfVariable(rightExpr)) == 0) {
						return true;
					} else {
						return false;
					}
				} else if (relOptr.equals("and")) {
					if (!getValueOfVariable(leftExpr).isEmpty() && 
							!getValueOfVariable(rightExpr).isEmpty()) {
						return true;
					} else {
						return false;
					}
				} else if (relOptr.equals("or")) {
					if (!getValueOfVariable(leftExpr).isEmpty() || 
							!getValueOfVariable(rightExpr).isEmpty()) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
		
		return false;
	}
	
	public String getValueOfVariable(String nameOfVariable) {
		return variables.get(nameOfVariable);
	}
	
/*	public String[] tokenize(String program) {
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
	}*/
	
	public void findVariables(String[] lexemes) {
		for(int i = 0; i < lexemes.length - 2; i++) {
			if(lexemes[i].startsWith("@") && 
					lexemes[i+1].equals("=")) {
				System.out.println(lexemes[i] + " = " + lexemes[i+2]);
				addVariables(lexemes[i], lexemes[i+2]);
			}
		}
		
		/*for(int i = 0; i < lexemes.length - 4; i++) {
			if(lexemes[i + 3].equals("+")){
				if(variables.get(lexemes[i]).charAt(0) == '\'') {
					variables.put(lexemes[i], 
						(variables.get(
							lexemes[i])+lexemes[i+4]).replace("''", ""));
				}
			}
		}*/
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
	
	class ECConditionalBlock {
		private int startIndex;
		private int endIndex;
		private String[] lexemesBlock;
		
		public ECConditionalBlock(int startIndex, int endIndex, String[] lexemesBlock) {
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.lexemesBlock = lexemesBlock;
		}
		
		
	}
}
