package cmsc141.mp1.ec;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;

public class ECController {

    private ECView view;
    
    private ECSyntaxChecker ecSyntaxChecker;
    
    public ECController(ECView view) {
        this.view = view;
        
        ecSyntaxChecker = new ECSyntaxChecker();
        
        addListeners();
    }
    
    private void addListeners() {
        
    	view.getRunButton().addActionListener(new ActionListener() {
            
    		@Override
            public void actionPerformed(ActionEvent e) {
            	ECInterpreter ecInterpreter = new ECInterpreter();
            	String input = view.getTextArea().getText();
            	view.getResArea().setText("");
            	
            	if (ecSyntaxChecker.match(input)) {
            		String output = ecInterpreter.interpret(input, view.getResArea());
            	} else {
            	    JTextArea resArea = view.getResArea();
            	    resArea.setText("********************\n"
            	    		+ "Syntax Error!\n"
            	    		+ "********************\n");
            	}
            }
        });
        
        view.getNewButton().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {

                view.getTextArea().setText("");
            }
        });
    }
    
    public void show() {
        view.getFrame().setVisible(true);
    }
}