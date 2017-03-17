package cmsc141.mp1.ec;

import java.awt.*;

import javax.swing.*;

public class ECFrame extends JFrame {

	private JPanel buttonsPane, textPanel;

	private JButton runButton;
	private JTextArea textArea;
	
	public ECFrame() {
		setTitle("EC");
		setLayout(new BorderLayout());
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		initComponents();
	}
	
	private void initComponents() {
		textPanel = new JPanel();

		buttonsPane = new JPanel();
		buttonsPane.setLayout(new FlowLayout());
		buttonsPane.setSize(this.getWidth(), 50);
		
		textArea = new JTextArea(30, 75);
		runButton = new JButton("Run");

		textPanel.add(new JScrollPane(textArea));
		buttonsPane.add(runButton);
		
		add(buttonsPane, BorderLayout.NORTH);
		add(textPanel, BorderLayout.CENTER);
	}

	public JButton getRubButton() {
		return runButton;
	}
	
	public JTextArea geTextArea() {
		return textArea;
	}
}
