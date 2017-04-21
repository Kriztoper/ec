package cmsc141.mp1.ec;

import java.util.Hashtable;

public class ECInterpreter {
	private Hashtable<String, String> variables = new Hashtable();
	private boolean isExec; // is executable
	private boolean condIsNotExec; // conditional is not executable
	
	public ECInterpreter() {
		isExec = true;
		condIsNotExec = false;
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
					lexemes[i+1].equals("=") && isExec) {
				if(isOperator(lexemes[i+3])) {
					int value = 0;
					float floatValue = (float) 0.0;
					String operand1 = getValueOfVariable(lexemes[i+2]);
					String operand2 = getValueOfVariable(lexemes[i+4]);
					String operator = lexemes[i+3];
					String stringValue = "";
					boolean isStringValue = false;
					boolean isFloatValue = false;
					
					if(lexemes[i+2].startsWith("@") && lexemes[i+4].startsWith("@")) {
						if (operand1.contains("'") && operand2.contains("'") && (operator.equals("+") || operator.equals("-"))) {
							operand1 = operand1.replaceAll("'", "");
							operand2 = operand2.replaceAll("'", "");
							stringValue = operateOnString(operand1, operand2, operator);
							isStringValue = true;
						} else {
							if (operand1.contains(".") && operand2.contains(".")) {
								floatValue = operateOnFloat(Float.parseFloat(operand1), Float.parseFloat(operand2), operator);
								isFloatValue = true;
							} else if (operand1.contains(".")) {
								floatValue = operateOnFloat(Float.parseFloat(operand1), Integer.parseInt(operand2), operator);
								isFloatValue = true;
							} else if (operand2.contains(".")) {
								floatValue = operateOnFloat(Float.parseFloat(operand1), Float.parseFloat(operand2), operator);
								isFloatValue = true;
							} else {
								value = operate(Integer.parseInt(operand1), Integer.parseInt(operand2), operator);
							}
						}
					} else if(lexemes[i+2].startsWith("@")) {
						if (operand1.contains("'") && (operator.endsWith("+") || operator.endsWith("-"))) {
							operand1 = operand1.replaceAll("'", "");
							operand2 = lexemes[i+4];
							stringValue = operateOnString(operand1, operand2, operator);
							isStringValue = true;
						} else {
							if (operand1.contains(".") && operand2.contains(".")) {
								floatValue = operateOnFloat(Float.parseFloat(operand1), Float.parseFloat(operand2), operator);
								isFloatValue = true;
							} else if (operand1.contains(".")) {
								floatValue = operateOnFloat(Float.parseFloat(operand1), Integer.parseInt(operand2), operator);
								isFloatValue = true;
							} else if (operand2.contains(".")) {
								floatValue = operateOnFloat(Float.parseFloat(operand1), Float.parseFloat(operand2), operator);
								isFloatValue = true;
							} else {
								value = operate(Integer.parseInt(operand1), Integer.parseInt(operand2), operator);
							}
						}
					} else if(lexemes[i+4].startsWith("@")) {
						if (operand2.contains("'") && (operator.endsWith("+") || operator.endsWith("-"))) {
							operand1 = lexemes[i+2];
							operand2 = operand2.replaceAll("'", "");
							stringValue = operateOnString(operand1, operand2, operator);
							isStringValue = true;
						} else {
							if (operand1.contains(".") && operand2.contains(".")) {
								floatValue = operateOnFloat(Float.parseFloat(operand1), Float.parseFloat(operand2), operator);
								isFloatValue = true;
							} else if (operand1.contains(".")) {
								floatValue = operateOnFloat(Float.parseFloat(operand1), Integer.parseInt(operand2), operator);
								isFloatValue = true;
							} else if (operand2.contains(".")) {
								floatValue = operateOnFloat(Float.parseFloat(operand1), Float.parseFloat(operand2), operator);
								isFloatValue = true;
							} else {
								value = operate(Integer.parseInt(operand1), Integer.parseInt(operand2), operator);
							}
						}
					} else {
						if (operand1.contains(".") && operand2.contains(".")) {
							floatValue = operateOnFloat(Float.parseFloat(operand1), Float.parseFloat(operand2), operator);
							isFloatValue = true;
						} else if (operand1.contains(".")) {
							floatValue = operateOnFloat(Float.parseFloat(operand1), Integer.parseInt(operand2), operator);
							isFloatValue = true;
						} else if (operand2.contains(".")) {
							floatValue = operateOnFloat(Float.parseFloat(operand1), Float.parseFloat(operand2), operator);
							isFloatValue = true;
						} else {
							value = operate(Integer.parseInt(operand1), Integer.parseInt(operand2), operator);
						}
					}	
					
					if (variables.containsKey(lexemes[i])) {
						if (isStringValue) {
							variables.replace(lexemes[i], stringValue);
						} else if (isFloatValue) {
							variables.replace(lexemes[i], Float.toString(floatValue));
						} else {
							variables.replace(lexemes[i], Integer.toString(value));
						}
					} else {
						if (isStringValue) {
							variables.put(lexemes[i], stringValue);
						} else if (isFloatValue) {
							variables.put(lexemes[i], Float.toString(floatValue));
						} else {
							variables.put(lexemes[i], Integer.toString(value));
						}
					}
				} else {
					if (variables.containsKey(lexemes[i])) {
						variables.replace(lexemes[i], lexemes[i+2]);
					} else {
						variables.put(lexemes[i], lexemes[i+2]);
					}
				}
				
			} else if (lexemes[i].equals("if") && 
					!lexemes[i+1].equals("not") &&
					!condIsNotExec) { // if without a not in condition
				isExec = true;
				System.out.println("Found an if without a not");
				String leftExpr = lexemes[i+1]; // left expression
				String relOptr = lexemes[i+2]; // relational operator
				String rightExpr = lexemes[i+3]; // right expression
				
				if (checkCondition(leftExpr, relOptr, rightExpr)) {
					System.out.println("Condition is satisfied");
					condIsNotExec = true;
					i += 3;
					continue;
				} else {
					System.out.println("Condition is not satisfied");
					condIsNotExec = false;
					i += 3;
					continue;
				}
			} else if (lexemes[i].equals("if") && 
					lexemes[i+1].equals("not") &&
					!condIsNotExec) { // if with a not in condition
				System.out.println("Found an if with a not");
				String leftExpr = lexemes[i+2]; // left expression
				String relOptr = lexemes[i+3]; // relational operator
				String rightExpr = lexemes[i+4]; // right expression
				
				if (!checkCondition(leftExpr, relOptr, rightExpr)) {
					System.out.println("Condition is satisfied");
					condIsNotExec = true;
					i += 4;
					continue;
				} else {
					System.out.println("Condition is not satisfied");
					condIsNotExec = false;
					i += 4;
					continue;
				}
			} else if (lexemes[i].equals("else ")) {
				condIsNotExec = false;
			} else if (lexemes[i].equals("print") && isExec) {
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
			} else if(lexemes[i].equals("scan") && isExec) {
				
			}
			
			if (lexemes[i].equals("end")) {
				System.out.println("Found an end");
				condIsNotExec = false;
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
				addVariable(lexemes[i], lexemes[i+2]);
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
	
	public float operateOnFloat(float operand1, float operand2, String operator) {
		if(operator.equals("+")) {
			return operand1 + operand2;}
		else if(operator.equals("-")) 
			return operand1 - operand2;
		else if(operator.equals("*"))
			return operand1 * operand2;
		else if(operator.equals("/"))
			return operand1 / operand2;
		else if(operator.equals("%"))
			return operand1 % operand2;
		else
			return 0;
	}
	
	public int operate(int operand1, int operand2, String operator) {
		if(operator.equals("+"))
			return operand1 + operand2;
		else if(operator.equals("-")) 
			return operand1 - operand2;
		else if(operator.equals("*"))
			return operand1 * operand2;
		else if(operator.equals("/"))
			return operand1 / operand2;
		else if(operator.equals("%"))
			return operand1 % operand2;
		else
			return 0;
	}

	public String operateOnString(String operand1, String operand2, String operator) {
		String stringValue = "";
		if (operator.equals("+")) {
			stringValue = operand1 + operand2;
		} else if (operator.equals("-")) {
			stringValue = operand1.replaceAll(operand2, "");
		}
		
		return stringValue;
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
	
	public void replaceValueOfVariable(String variableName, String newValue) {
		variables.replace(variableName, newValue);
	}
	
	public void addVariable(String variableName, String value) {
		variables.put(variableName, value);
	}
	
	public boolean isOperator(String symbol) {
		if("+-/*%".contains(symbol))
			return true;
		else
			return false;
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
