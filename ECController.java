package cmsc141.mp1.ec;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ECController {

    private ECView view;
    
    private ECParser parser;
    
    public ECController(ECView view) {
        this.view = view;
        
        parser = new ECParser();
        
        addListeners();
    }
    
    private void addListeners() {
        
        view.getRunButton().addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
               String input = view.getTextArea().getText();

               if (parser.hasMatch(input))
                   view.getResArea().setText("YEY");
               else
                   view.getResArea().setText("BOO");
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
