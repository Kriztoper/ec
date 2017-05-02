package cmsc141.mp1.ec;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ECView {
    
    private ECFrame frame;
    
    private JPanel buttonsPanel, textPanel, resultsPanel;

    private JButton runButton, newButton;
    private JTextArea textArea, resArea;
    
    public ECView() {
        frame = new ECFrame();
        
        initComponents();
    }
    
    private void initComponents() {
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());

        textPanel = new JPanel();
        textPanel.setBackground(Color.LIGHT_GRAY);
        textPanel.setLayout(new FlowLayout());
        
        resultsPanel = new JPanel();
        resultsPanel.setBackground(Color.GRAY);
        resultsPanel.setLayout(new FlowLayout());
        
        textArea = new JTextArea(Constants.HEIGHT/28, Constants.WIDTH/18);
        textArea.setTabSize(2);
        
        resArea = new JTextArea(Constants.HEIGHT/40, Constants.WIDTH/18);
        resArea.setText("");
        resArea.setEditable(false);
        
        runButton = new JButton("Run");
        newButton = new JButton("New");

        buttonsPanel.add(runButton);
        buttonsPanel.add(newButton);
        textPanel.add(new JScrollPane(textArea));
        resultsPanel.add(new JScrollPane(resArea));
        
        frame.add(buttonsPanel, BorderLayout.NORTH);
        frame.add(textPanel, BorderLayout.CENTER);
        frame.add(resultsPanel, BorderLayout.SOUTH);
    }
    
    public void setJTextArea(String str) {
    	textArea.append(str);
    }

    public JButton getNewButton() {
        return newButton;
    }
    
    public JButton getRunButton() {
        return runButton;
    }
    
    public JTextArea getTextArea() {
        return textArea;
    }

    public JTextArea getResArea() {
        return resArea;
    }
    
    public ECFrame getFrame() {
        return frame;
    }
}
