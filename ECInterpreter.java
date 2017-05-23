package cmsc141.mp1.ec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Hashtable;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ECInterpreter implements KeyListener{
	private JTextArea console;
	private int key = 0;
	private boolean isEnter = false;
	private String consoleInput = "";
	private Hashtable<String, String> variables;
	private boolean isExec; // is executable
	private boolean condHasExec; // conditional has executed
	private boolean isLoop;
	
	public ECInterpreter() {
		isExec = true;
		condHasExec = false;
		isLoop = false;
	}
	
	public String interpret(String program, JTextArea console) {
		this.console = console;
		variables = new Hashtable();
		console.addKeyListener(this);
		
		ECLexer ecLexer = new ECLexer();
		String[] lexemes = ecLexer.tokenize(program);
//		String[] words = program.split(" ");
		//findVariables(lexemes);
		String output = analyzeLexemes(0, lexemes);
		//printVariable(lexemes);
		return output;
	}
	
	public String analyzeLexemes(int i, String[] lexemes) {
		String output = "";
		
		lexemesLabel:
		for (; i < lexemes.length; i++) {
			
			if (lexemes[i].startsWith("@") && (i+1 < lexemes.length) && 
					lexemes[i+1].equals("=") && isExec) {
				addVar(i, lexemes);
				
			} else if (lexemes[i].equals("if") && 
					!lexemes[i+1].equals("not") && !condHasExec){
				System.out.println("Found an if without a not");
				String leftExpr = lexemes[i+1]; // left expression
				String relOptr = lexemes[i+2]; // relational operator
				String rightExpr = lexemes[i+3]; // right expression
				
				if (checkCondition(leftExpr, relOptr, rightExpr)) {
					System.out.println("Condition is satisfied");
					isExec = true;
					condHasExec = true;
					i += 3;
					continue;
				} else {
					System.out.println("Condition is not satisfied");
					isExec = false;
					i += 3;
					continue;
				}
			} else if (lexemes[i].equals("if") && 
					lexemes[i+1].equals("not") && !condHasExec){
				System.out.println("Found an if with a not");
				String leftExpr = lexemes[i+2]; // left expression
				String relOptr = lexemes[i+3]; // relational operator
				String rightExpr = lexemes[i+4]; // right expression
				
				if (!checkCondition(leftExpr, relOptr, rightExpr)) {
					System.out.println("Condition is satisfied");
					isExec = true;
					condHasExec = true;
					i += 4;
					continue;
				} else {
					System.out.println("Condition is not satisfied");
					isExec = false;
					i += 4;
					continue;
				}
			} else if (lexemes[i].equals("else")) {
				if (condHasExec) {
					isExec = false;
				} else {
					isExec = true;
				}
			} else if ((lexemes[i].equals("print") || lexemes[i].equals("puts")) && isExec) {
				boolean isPuts = false;
				int offsetToAdd = 0;
				String stringToPrint = "";

				if (lexemes[i].equals("puts")) {
					isPuts = true;
				}
				
				if (lexemes[i+1].startsWith("'") && lexemes[i+1].endsWith("'")) {
					String string = lexemes[i+1];
					string = string.substring(1, string.length()-1);
					stringToPrint += string;
					offsetToAdd++;
				} else if (lexemes[i+1].startsWith("@")) {
					String string = getValueOfVariable(lexemes[i+1]);
					if (string.startsWith("'") && string.endsWith("'")) {
						string = string.substring(1, string.length()-1);
					}
					stringToPrint += string;
					offsetToAdd++;
				}
				
				for (int j = 2; (i+j) < lexemes.length; j++) {
					if (lexemes[i+j].equals("+")) {
						j++;
						if (lexemes[i+j].startsWith("'") && lexemes[i+j].endsWith("'")) {
							String string = lexemes[i+j];
							string = string.substring(1, string.length()-1);
							stringToPrint += string;
							offsetToAdd += 2;
						} else if (lexemes[i+j].startsWith("@")) {
							String string = getValueOfVariable(lexemes[i+j]);
							if (string.startsWith("'") && string.endsWith("'")) {
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
				if (isPuts) {
					stringToPrint += '\n';
				}
				console.append(stringToPrint);
				output += stringToPrint;
				
				i += offsetToAdd;
				continue;
			} else if(lexemes[i].equals("scan") && isExec) {
				//Scanner scan = new Scanner(System.in);
				char letter = 0;
				//console.setEditable(true);	
				
	
				/*do {
					consoleInput = JOptionPane.showInputDialog(null, "Scanning: ");
				} while(consoleInput.equals(""));
				console.append(consoleInput+"\n");*/
				
				String[] options = {"OK"};
				JPanel panel = new JPanel();
				JTextField txt = new JTextField(20);
				panel.add(txt);
				int selectedOption = JOptionPane.showOptionDialog(null, panel, "Input", JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
				
				if(selectedOption == 0)
				{
				    consoleInput = txt.getText();
				}
				System.out.println(consoleInput);

				
				//consoleInput = JOptionPane.showInputDialog(null, "Scanning: ");
				if (null == consoleInput) {
					consoleInput = "";
				}
				console.append(consoleInput+"\n");
				//consoleInput = ECScanManager.scan(this, console);
				
				/*while(true) {
					try {
						letter = (char) System.in.read();
						consoleInput += letter;
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					if (letter == 13)
						break;
				}*/
				
				//console.setEditable(false);
				variables.put(lexemes[i+1], consoleInput);
				
				i++;
			} else if(lexemes[i].equals("for")) {
				isLoop = true;
				forLoop(i, lexemes);
				
				while(!lexemes[i].equals("end")) {
					i++;
				}
				isLoop = false;
			} else if(lexemes[i].equals("while") && lexemes[i+4].equals("do")) {
				isLoop = true;
				whileLoop(i, lexemes);
				
				while(!lexemes[i].equals("end")) {
					i++;
				}
				isLoop = false;
			} else if(lexemes[i].equals("do")) {
				isLoop = true;
				doWhileLoop(i, lexemes);
				
				while(!lexemes[i].equals("end")) {
					i++;
				}
				isLoop = false;
			}
			
			if (lexemes[i].equals("end")) {
				System.out.println("Found an end");
				
				isExec = true;
				condHasExec = false;
				if (isLoop) {
					break;
				}
			}
		}
		
		return output;
	}

	public void whileLoop(int i, String[] lexemes) {
		int j=i;
		
		while(checkCondition(lexemes[j+1], lexemes[j+2], lexemes[j+3])) {
			analyzeLexemes(j+5, lexemes);
		};
	}
	
	public void forLoop(int i, String[] lexemes) {
		int j=i;
		
		variables.put(lexemes[j+1], lexemes[j+3]);
		
		while(checkCondition(lexemes[j+5], lexemes[j+6], lexemes[j+7])) {
			analyzeLexemes(j+15, lexemes);
			addVar(j+9, lexemes);
		};
	}
	
	public void doWhileLoop(int i, String[] lexemes) {
		int j = i;
		String leftExpr = "";
		String operator = "";
		String rightExpr = "";
		
		// get the while condition
		for (int k = j + 1; k < lexemes.length; k++) {
			if (lexemes[k].equals("while")){
				leftExpr = lexemes[k+1];
				operator = lexemes[k+2];
				rightExpr = lexemes[k+3];
				break;
			}
		}
				
		do {
			analyzeLexemes(j+1, lexemes);
		} while(checkCondition(leftExpr, operator, rightExpr));
	}
	
	public boolean isString(String text) {
		String regex = "([0-9]+|-[0-9]+|[0-9]+.[0-9]+|-[0-9]+.[0-9]+)";
		return !text.matches(regex);
	}
	
	public void addVar(int i, String[] lexemes) {
		// check to avoid out of bounds exception
		if (i + 3 < lexemes.length) {
			int value = 0;
			float floatValue = (float) 0.0;
			String operand1 = getValueOfVariable(lexemes[i+2]);
			String operand2 = getValueOfVariable(lexemes[i+4]);
			String operator = lexemes[i+3];
			String stringValue = "";
			boolean isStringValue = false;
			boolean isFloatValue = false;
			
			if(isOperator(lexemes[i+3])) {
				if(lexemes[i+2].startsWith("@") && lexemes[i+4].startsWith("@")) {
	
					if ((isString(operand1)) && (isString(operand2)) && (operator.equals("+") || operator.equals("-"))) {
						operand1 = removeQuotes(operand1);
						operand2 = removeQuotes(operand2);
						stringValue = operateOnString(operand1, operand2, operator);
						isStringValue = true;
					} else {
						if ((operand1.contains(".") && !isString(operand1)) && (operand2.contains(".") && !isString(operand2))) {
							floatValue = operateOnFloat(Float.parseFloat(operand1), Float.parseFloat(operand2), operator);
							isFloatValue = true;
						} else if ((operand1.contains(".") && !isString(operand1)) && !isString(operand2)) {
							floatValue = operateOnFloat(Float.parseFloat(operand1), Integer.parseInt(operand2), operator);
							isFloatValue = true;
						} else if ((operand2.contains(".") && !isString(operand2)) && !isString(operand1)) {
							floatValue = operateOnFloat(Float.parseFloat(operand1), Float.parseFloat(operand2), operator);
							isFloatValue = true;
						} else if ((!isString(operand1)) && (!isString(operand2))) {
							value = operate(Integer.parseInt(operand1), Integer.parseInt(operand2), operator);
						} else {
							console.append("********************\n"
		            	    		+ "Invalid Operation!\n"
		            	    		+ "********************\n");
						}
					}
				} else if(lexemes[i+2].startsWith("@")) {
					operand2 = lexemes[i+4];
	
					if (isString(operand1) && (operator.endsWith("+") || operator.endsWith("-"))) {
						operand1 = removeQuotes(operand1);
						stringValue = operateOnString(operand1, operand2, operator);
						isStringValue = true;
					} else {
						if ((operand1.contains(".") && !isString(operand1)) && operand2.contains(".")) {
							floatValue = operateOnFloat(Float.parseFloat(operand1), Float.parseFloat(operand2), operator);
							isFloatValue = true;
						} else if ((operand1.contains(".") && !isString(operand1))) {
							floatValue = operateOnFloat(Float.parseFloat(operand1), Integer.parseInt(operand2), operator);
							isFloatValue = true;
						} else if (operand2.contains(".") && !isString(operand1)) {
							floatValue = operateOnFloat(Float.parseFloat(operand1), Float.parseFloat(operand2), operator);
							isFloatValue = true;
						} else if (!isString(operand1)) {
							value = operate(Integer.parseInt(operand1), Integer.parseInt(operand2), operator);
						} else {
							console.append("********************\n"
		            	    		+ "Invalid Operation!\n"
		            	    		+ "********************\n");
						}
					}
				} else if(lexemes[i+4].startsWith("@")) {
					operand1 = lexemes[i+2];
	
					if (isString(operand2) && (operator.endsWith("+") || operator.endsWith("-"))) {
						operand2 = removeQuotes(operand2);
						
						stringValue = operateOnString(operand1, operand2, operator);
						isStringValue = true;
					} else {
						if (operand1.contains(".") && (operand2.contains(".") && !isString(operand2))) {
							floatValue = operateOnFloat(Float.parseFloat(operand1), Float.parseFloat(operand2), operator);
							isFloatValue = true;
						} else if (operand1.contains(".") && !isString(operand2)) {
							floatValue = operateOnFloat(Float.parseFloat(operand1), Integer.parseInt(operand2), operator);
							isFloatValue = true;
						} else if (operand2.contains(".") && !isString(operand2)) {
							floatValue = operateOnFloat(Float.parseFloat(operand1), Float.parseFloat(operand2), operator);
							isFloatValue = true;
						} else if (!isString(operand2)) {
							value = operate(Integer.parseInt(operand1), Integer.parseInt(operand2), operator);
						} else {
							console.append("********************\n"
		            	    		+ "Invalid Operation!\n"
		            	    		+ "********************\n");
						}
					}
				} else {
					operand1 = lexemes[i+2];
					operand2 = lexemes[i+4];
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
					if(lexemes[i+2].startsWith("@")) {
						variables.replace(lexemes[i], getValueOfVariable(lexemes[i+2]));
					} else {
						variables.replace(lexemes[i], lexemes[i+2]);
					}
				} else {
					if(lexemes[i+2].startsWith("@")) {
						variables.put(lexemes[i], getValueOfVariable(lexemes[i+2]));
					} else {
						variables.put(lexemes[i], lexemes[i+2]);
					}
				}
			}
		}
	}
	
	private String removeQuotes(String string) {
	    String str = "";
	    
	    if (string.startsWith("'") && string.endsWith("'")) {
		    for (int i = 1; i < string.length()-1; i++) {
		        str += string.charAt(i);
		    }
	    } else {
	    	str = string;
	    }
	    
	    return str;
	}
	
	public boolean checkCondition(String leftExpr, String relOptr, String rightExpr) {
		if (leftExpr.startsWith("@") && rightExpr.startsWith("@")) {
			System.out.println("Both left and right expressions are "
					+ "variables");
			if (isString(getValueOfVariable(leftExpr)) &&
					isString(getValueOfVariable(rightExpr))) {
				leftExpr = getValueOfVariable(leftExpr);
				rightExpr = getValueOfVariable(rightExpr);
				leftExpr = removeQuotes(leftExpr);
				rightExpr = removeQuotes(rightExpr);
				System.out.println("left expr  = " + leftExpr);
				System.out.println("right expr  = " + rightExpr);
				System.out.println("Both left and right expressions are "
						+ "string variables");
				if (relOptr.equals("==")) {
					System.out.println("operator is ==");
					if (leftExpr.equals(
							rightExpr)) {
						System.out.println("Both" + 
								" are equal");
						return true;
					} else {
						System.out.println("Both" + 
								" are not equal");
						return false;
					}
				} else if (relOptr.equals("!=")) {
					if (!leftExpr.equals(
							rightExpr)) {
						return true;
					} else {
						return false;
					}
				} else if (relOptr.equals("<")) {
					if (leftExpr.compareTo(
							rightExpr) == -1) {
						return true;
					} else {
						return false;
					}
				} else if (relOptr.equals("<=")) {
					if (leftExpr.compareTo(
							rightExpr) == -1 ||
							leftExpr.compareTo(
									rightExpr) == 0) {
						return true;
					} else {
						return false;
					}
				} else if (relOptr.equals(">")) {
					if (leftExpr.compareTo(
							rightExpr) == 1) {
						return true;
					} else {
						return false;
					}
				} else if (relOptr.equals(">=")) {
					if (leftExpr.compareTo(
							rightExpr) == 1 ||
							leftExpr.compareTo(
									rightExpr) == 0) {
						return true;
					} else {
						return false;
					}
				} else if (relOptr.equals("and")) {
					if (!leftExpr.isEmpty() && 
							!rightExpr.isEmpty()) {
						return true;
					} else {
						return false;
					}
				} else if (relOptr.equals("or")) {
					if (!leftExpr.isEmpty() || 
							!rightExpr.isEmpty()) {
						return true;
					} else {
						return false;
					}
				}
			} else if (!isString(getValueOfVariable(leftExpr)) &&
					!isString(getValueOfVariable(rightExpr))){
				leftExpr = getValueOfVariable(leftExpr);
				rightExpr = getValueOfVariable(rightExpr);
				leftExpr = removeQuotes(leftExpr);
				rightExpr = removeQuotes(rightExpr);
				System.out.println("Both left and right expressions are "
						+ "constant variables");
				if (relOptr.equals("==")) {
					System.out.println("operator is ==");
					if (leftExpr.equals(
							rightExpr)) {
						System.out.println("Both" + 
								" are equal");
						return true;
					} else {
						System.out.println("Both" + 
								" are not equal");
						return false;
					}
				} else if (relOptr.equals("!=")) {
					if (!leftExpr.equals(
							rightExpr)) {
						return true;
					} else {
						return false;
					}
				} else if (relOptr.equals("<")) {
					if (Float.parseFloat(leftExpr) < Float.parseFloat(rightExpr)) {
//							getValueOfVariable(leftExpr).compareTo(
//							getValueOfVariable(rightExpr)) == -1) {
						return true;
					} else {
						return false;
					}
				} else if (relOptr.equals("<=")) {
					if (Float.parseFloat(leftExpr) <= Float.parseFloat(rightExpr)) {
//							getValueOfVariable(leftExpr).compareTo(
//							getValueOfVariable(rightExpr)) == -1 ||
//							getValueOfVariable(leftExpr).compareTo(
//									getValueOfVariable(rightExpr)) == 0) {
						return true;
					} else {
						return false;
					}
				} else if (relOptr.equals(">")) {
					if (Float.parseFloat(leftExpr) > Float.parseFloat(rightExpr)) {
//							getValueOfVariable(leftExpr).compareTo(
//							getValueOfVariable(rightExpr)) == 1) {
						return true;
					} else {
						return false;
					}
				} else if (relOptr.equals(">=")) {
					if (Float.parseFloat(leftExpr) >= Float.parseFloat(rightExpr)) {
//							getValueOfVariable(leftExpr).compareTo(
//							getValueOfVariable(rightExpr)) == 1 ||
//							getValueOfVariable(leftExpr).compareTo(
//									getValueOfVariable(rightExpr)) == 0) {
						return true;
					} else {
						return false;
					}
				} else if (relOptr.equals("and")) {
					if (Float.parseFloat(leftExpr) > 0 && Float.parseFloat(rightExpr) > 0) {
//							!getValueOfVariable(leftExpr).isEmpty() && 
//							!getValueOfVariable(rightExpr).isEmpty()) {
						return true;
					} else {
						return false;
					}
				} else if (relOptr.equals("or")) {
					if (Float.parseFloat(leftExpr) > 0 || Float.parseFloat(rightExpr) > 0) {
//							!getValueOfVariable(leftExpr).isEmpty() || 
//							!getValueOfVariable(rightExpr).isEmpty()) {
						return true;
					} else {
						return false;
					}
				}
			}
		} else if (leftExpr.startsWith("@") && !rightExpr.startsWith("@") &&
				!isString(getValueOfVariable(leftExpr)) && !isString(rightExpr)) { // const word op const
			leftExpr = getValueOfVariable(leftExpr);
			leftExpr = removeQuotes(leftExpr);
			System.out.println("Left expr is const and"
					+ " right expr is const word");
			if (relOptr.equals("==")) {
				System.out.println("operator is ==");
				if (leftExpr.equals(
						rightExpr)) {
					System.out.println("Both" + 
							" are equal");
					return true;
				} else {
					System.out.println("Both" + 
							" are not equal");
					return false;
				}
			} else if (relOptr.equals("!=")) {
				if (!leftExpr.equals(
						rightExpr)) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("<")) {
				if (Float.parseFloat(leftExpr) < Float.parseFloat(rightExpr)) {
//						getValueOfVariable(leftExpr).compareTo(
//						getValueOfVariable(rightExpr)) == -1) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("<=")) {
				if (Float.parseFloat(leftExpr) <= Float.parseFloat(rightExpr)) {
//						getValueOfVariable(leftExpr).compareTo(
//						getValueOfVariable(rightExpr)) == -1 ||
//						getValueOfVariable(leftExpr).compareTo(
//								getValueOfVariable(rightExpr)) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals(">")) {
				if (Float.parseFloat(leftExpr) > Float.parseFloat(rightExpr)) {
//						getValueOfVariable(leftExpr).compareTo(
//						getValueOfVariable(rightExpr)) == 1) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals(">=")) {
				if (Float.parseFloat(leftExpr) >= Float.parseFloat(rightExpr)) {
//						getValueOfVariable(leftExpr).compareTo(
//						getValueOfVariable(rightExpr)) == 1 ||
//						getValueOfVariable(leftExpr).compareTo(
//								getValueOfVariable(rightExpr)) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("and")) {
				if (Float.parseFloat(leftExpr) > 0 && Float.parseFloat(rightExpr) > 0) {
//						!getValueOfVariable(leftExpr).isEmpty() && 
//						!getValueOfVariable(rightExpr).isEmpty()) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("or")) {
				if (Float.parseFloat(leftExpr) > 0 || Float.parseFloat(rightExpr) > 0) {
//						!getValueOfVariable(leftExpr).isEmpty() || 
//						!getValueOfVariable(rightExpr).isEmpty()) {
					return true;
				} else {
					return false;
				}
			}
		} else if (!leftExpr.startsWith("@") && rightExpr.startsWith("@") &&
				!isString(leftExpr) && !isString(getValueOfVariable(rightExpr))) { // const op const word
			rightExpr = getValueOfVariable(rightExpr);
			rightExpr = removeQuotes(rightExpr);
			System.out.println("Left expr is const word and"
					+ " right expr is const");
			if (relOptr.equals("==")) {
				System.out.println("operator is ==");
				if (leftExpr.equals(
						rightExpr)) {
					System.out.println("Both" + 
							" are equal");
					return true;
				} else {
					System.out.println("Both" + 
							" are not equal");
					return false;
				}
			} else if (relOptr.equals("!=")) {
				if (!leftExpr.equals(
						rightExpr)) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("<")) {
				if (Float.parseFloat(leftExpr) < Float.parseFloat(rightExpr)) {
//						getValueOfVariable(leftExpr).compareTo(
//						getValueOfVariable(rightExpr)) == -1) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("<=")) {
				if (Float.parseFloat(leftExpr) <= Float.parseFloat(rightExpr)) {
//						getValueOfVariable(leftExpr).compareTo(
//						getValueOfVariable(rightExpr)) == -1 ||
//						getValueOfVariable(leftExpr).compareTo(
//								getValueOfVariable(rightExpr)) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals(">")) {
				if (Float.parseFloat(leftExpr) > Float.parseFloat(rightExpr)) {
//						getValueOfVariable(leftExpr).compareTo(
//						getValueOfVariable(rightExpr)) == 1) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals(">=")) {
				if (Float.parseFloat(leftExpr) >= Float.parseFloat(rightExpr)) {
//						getValueOfVariable(leftExpr).compareTo(
//						getValueOfVariable(rightExpr)) == 1 ||
//						getValueOfVariable(leftExpr).compareTo(
//								getValueOfVariable(rightExpr)) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("and")) {
				if (Float.parseFloat(leftExpr) > 0 && Float.parseFloat(rightExpr) > 0) {
//						!getValueOfVariable(leftExpr).isEmpty() && 
//						!getValueOfVariable(rightExpr).isEmpty()) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("or")) {
				if (Float.parseFloat(leftExpr) > 0 || Float.parseFloat(rightExpr) > 0) {
//						!getValueOfVariable(leftExpr).isEmpty() || 
//						!getValueOfVariable(rightExpr).isEmpty()) {
					return true;
				} else {
					return false;
				}
			}
		} else if (leftExpr.startsWith("@") && !rightExpr.startsWith("@") &&
				isString(getValueOfVariable(leftExpr)) && isString(rightExpr)) { // string word op string
			leftExpr = getValueOfVariable(leftExpr);
			leftExpr = removeQuotes(leftExpr);
			System.out.println("Left expr is string word and"
					+ " right expr is string");
			if (relOptr.equals("==")) {
				System.out.println("operator is ==");
				if (leftExpr.equals(
						rightExpr)) {
					System.out.println("Both" + 
							" are equal");
					return true;
				} else {
					System.out.println("Both" + 
							" are not equal");
					return false;
				}
			} else if (relOptr.equals("!=")) {
				if (!leftExpr.equals(
						rightExpr)) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("<")) {
				if (leftExpr.compareTo(
						rightExpr) == -1) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("<=")) {
				if (leftExpr.compareTo(
						rightExpr) == -1 ||
						leftExpr.compareTo(
								rightExpr) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals(">")) {
				if (leftExpr.compareTo(
						rightExpr) == 1) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals(">=")) {
				if (leftExpr.compareTo(
						rightExpr) == 1 ||
						leftExpr.compareTo(
								rightExpr) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("and")) {
				if (!leftExpr.isEmpty() && 
						!rightExpr.isEmpty()) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("or")) {
				if (!leftExpr.isEmpty() || 
						!rightExpr.isEmpty()) {
					return true;
				} else {
					return false;
				}
			}
		} else if (!leftExpr.startsWith("@") && rightExpr.startsWith("@") &&
				isString(leftExpr) && isString(getValueOfVariable(rightExpr))) { // string op string word
			rightExpr = getValueOfVariable(rightExpr);
			rightExpr = removeQuotes(rightExpr);
			System.out.println("Left expr is string and"
					+ " right expr is string word");
			if (relOptr.equals("==")) {
				System.out.println("operator is ==");
				if (leftExpr.equals(
						rightExpr)) {
					System.out.println("Both" + 
							" are equal");
					return true;
				} else {
					System.out.println("Both" + 
							" are not equal");
					return false;
				}
			} else if (relOptr.equals("!=")) {
				if (!leftExpr.equals(
						rightExpr)) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("<")) {
				if (leftExpr.compareTo(
						rightExpr) == -1) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("<=")) {
				if (leftExpr.compareTo(
						rightExpr) == -1 ||
						leftExpr.compareTo(
								rightExpr) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals(">")) {
				if (leftExpr.compareTo(
						rightExpr) == 1) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals(">=")) {
				if (leftExpr.compareTo(
						rightExpr) == 1 ||
						leftExpr.compareTo(
								rightExpr) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("and")) {
				if (!leftExpr.isEmpty() && 
						!rightExpr.isEmpty()) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("or")) {
				if (!leftExpr.isEmpty() || 
						!rightExpr.isEmpty()) {
					return true;
				} else {
					return false;
				}
			}
		} else if (!leftExpr.startsWith("@") && !rightExpr.startsWith("@") &&
				!isString(leftExpr) && !isString(rightExpr)) { // const op const word
			System.out.println("Both expr are const");
			if (relOptr.equals("==")) {
				System.out.println("operator is ==");
				if (leftExpr.equals(
						rightExpr)) {
					System.out.println("Both" + 
							" are equal");
					return true;
				} else {
					System.out.println("Both" + 
							" are not equal");
					return false;
				}
			} else if (relOptr.equals("!=")) {
				if (!leftExpr.equals(
						rightExpr)) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("<")) {
				if (Float.parseFloat(leftExpr) < Float.parseFloat(rightExpr)) {
//						getValueOfVariable(leftExpr).compareTo(
//						getValueOfVariable(rightExpr)) == -1) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("<=")) {
				if (Float.parseFloat(leftExpr) <= Float.parseFloat(rightExpr)) {
//						getValueOfVariable(leftExpr).compareTo(
//						getValueOfVariable(rightExpr)) == -1 ||
//						getValueOfVariable(leftExpr).compareTo(
//								getValueOfVariable(rightExpr)) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals(">")) {
				if (Float.parseFloat(leftExpr) > Float.parseFloat(rightExpr)) {
//						getValueOfVariable(leftExpr).compareTo(
//						getValueOfVariable(rightExpr)) == 1) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals(">=")) {
				if (Float.parseFloat(leftExpr) >= Float.parseFloat(rightExpr)) {
//						getValueOfVariable(leftExpr).compareTo(
//						getValueOfVariable(rightExpr)) == 1 ||
//						getValueOfVariable(leftExpr).compareTo(
//								getValueOfVariable(rightExpr)) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("and")) {
				if (Float.parseFloat(leftExpr) > 0 && Float.parseFloat(rightExpr) > 0) {
//						!getValueOfVariable(leftExpr).isEmpty() && 
//						!getValueOfVariable(rightExpr).isEmpty()) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("or")) {
				if (Float.parseFloat(leftExpr) > 0 || Float.parseFloat(rightExpr) > 0) {
//						!getValueOfVariable(leftExpr).isEmpty() || 
//						!getValueOfVariable(rightExpr).isEmpty()) {
					return true;
				} else {
					return false;
				}
			}
		} else if (!leftExpr.startsWith("@") && !rightExpr.startsWith("@") &&
				isString(leftExpr) && isString(rightExpr)) { // string op string word
			System.out.println("Both expr are string");
			if (relOptr.equals("==")) {
				System.out.println("operator is ==");
				if (leftExpr.equals(
						rightExpr)) {
					System.out.println("Both" + 
							" are equal");
					return true;
				} else {
					System.out.println("Both" + 
							" are not equal");
					return false;
				}
			} else if (relOptr.equals("!=")) {
				if (!leftExpr.equals(
						rightExpr)) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("<")) {
				if (leftExpr.compareTo(
						rightExpr) == -1) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("<=")) {
				if (leftExpr.compareTo(
						rightExpr) == -1 ||
						leftExpr.compareTo(
								rightExpr) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals(">")) {
				if (leftExpr.compareTo(
						rightExpr) == 1) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals(">=")) {
				if (leftExpr.compareTo(
						rightExpr) == 1 ||
						leftExpr.compareTo(
								rightExpr) == 0) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("and")) {
				if (!leftExpr.isEmpty() && 
						!rightExpr.isEmpty()) {
					return true;
				} else {
					return false;
				}
			} else if (relOptr.equals("or")) {
				if (!leftExpr.isEmpty() || 
						!rightExpr.isEmpty()) {
					return true;
				} else {
					return false;
				}
			}
		}
		
		return false;
	}
	
/*	private boolean checkConstantsWithOr(String leftExpr, String rightExpr) {
		if (leftExpr.contains(".") && rightExpr.contains(".")) {
			return Float.parseFloat(leftExpr) > 0 || Float.parseFloat(rightExpr) > 0;
		} else if (leftExpr.contains(".") && !rightExpr.contains(".")) {
			return Float.parseFloat(leftExpr) > 0 || Integer.parseInt(rightExpr) > 0;
		} else if (!leftExpr.contains(".") && rightExpr.contains(".")) {
			return Integer.parseInt(leftExpr) > 0 || Float.parseFloat(rightExpr) > 0;
		} else if (!leftExpr.contains(".") && !rightExpr.contains(".")) {
			return Integer.parseInt(leftExpr) > 0 || Integer.parseInt(rightExpr) > 0;
		}
		
		return false;
	}

	private boolean checkConstantsWithAnd(String leftExpr, String rightExpr) {
		if (leftExpr.contains(".") && rightExpr.contains(".")) {
			return Float.parseFloat(leftExpr) > 0 && Float.parseFloat(rightExpr) > 0;
		} else if (leftExpr.contains(".") && !rightExpr.contains(".")) {
			return Float.parseFloat(leftExpr) > 0 && Integer.parseInt(rightExpr) > 0;
		} else if (!leftExpr.contains(".") && rightExpr.contains(".")) {
			return Integer.parseInt(leftExpr) > 0 && Float.parseFloat(rightExpr) > 0;
		} else if (!leftExpr.contains(".") && !rightExpr.contains(".")) {
			return Integer.parseInt(leftExpr) > 0 && Integer.parseInt(rightExpr) > 0;
		}
		
		return false;
	}

	private boolean checkConstantsIfLessThan(String leftExpr, String rightExpr) {
		if (leftExpr.contains(".") && rightExpr.contains(".")) {
			return Float.parseFloat(leftExpr) < Float.parseFloat(rightExpr);
		} else if (leftExpr.contains(".") && !rightExpr.contains(".")) {
			return Float.parseFloat(leftExpr) < Integer.parseInt(rightExpr);
		} else if (!leftExpr.contains(".") && rightExpr.contains(".")) {
			return Integer.parseInt(leftExpr) < Float.parseFloat(rightExpr);
		} else if (!leftExpr.contains(".") && !rightExpr.contains(".")) {
			return Integer.parseInt(leftExpr) < Integer.parseInt(rightExpr);
		}
		
		return false;
	}
	
	private boolean checkConstantsIfGreaterThan(String leftExpr, String rightExpr) {
		if (leftExpr.contains(".") && rightExpr.contains(".")) {
			return Float.parseFloat(leftExpr) > Float.parseFloat(rightExpr);
		} else if (leftExpr.contains(".") && !rightExpr.contains(".")) {
			return Float.parseFloat(leftExpr) > Integer.parseInt(rightExpr);
		} else if (!leftExpr.contains(".") && rightExpr.contains(".")) {
			return Integer.parseInt(leftExpr) > Float.parseFloat(rightExpr);
		} else if (!leftExpr.contains(".") && !rightExpr.contains(".")) {
			return Integer.parseInt(leftExpr) > Integer.parseInt(rightExpr);
		}
		
		return false;
	}

	private boolean checkConstantsIfLessThanEquals(String leftExpr, String rightExpr) {
		if (leftExpr.contains(".") && rightExpr.contains(".")) {
			return Float.parseFloat(leftExpr) <= Float.parseFloat(rightExpr);
		} else if (leftExpr.contains(".") && !rightExpr.contains(".")) {
			return Float.parseFloat(leftExpr) <= Integer.parseInt(rightExpr);
		} else if (!leftExpr.contains(".") && rightExpr.contains(".")) {
			return Integer.parseInt(leftExpr) <= Float.parseFloat(rightExpr);
		} else if (!leftExpr.contains(".") && !rightExpr.contains(".")) {
			return Integer.parseInt(leftExpr) <= Integer.parseInt(rightExpr);
		}
		
		return false;
	}
	
	private boolean checkConstantsIfGreaterThanEquals(String leftExpr, String rightExpr) {
		if (leftExpr.contains(".") && rightExpr.contains(".")) {
			return Float.parseFloat(leftExpr) >= Float.parseFloat(rightExpr);
		} else if (leftExpr.contains(".") && !rightExpr.contains(".")) {
			return Float.parseFloat(leftExpr) >= Integer.parseInt(rightExpr);
		} else if (!leftExpr.contains(".") && rightExpr.contains(".")) {
			return Integer.parseInt(leftExpr) >= Float.parseFloat(rightExpr);
		} else if (!leftExpr.contains(".") && !rightExpr.contains(".")) {
			return Integer.parseInt(leftExpr) >= Integer.parseInt(rightExpr);
		}
		
		return false;
	}
*/
	
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

	@Override
	public void keyTyped(KeyEvent e) {
		key = e.getKeyCode();
		
		if(key == KeyEvent.VK_ENTER) {
			System.out.println("Enter is pressed!");
			console.setEditable(false);
			isEnter = true;
		} else {
			isEnter = false;
			consoleInput+=e.getKeyChar();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}
