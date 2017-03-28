package cmsc141.mp1.ec;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class ECFrame extends JFrame {
    

	public ECFrame() {
		setTitle("EC Text Editor and Interpreter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(Constants.WIDTH, Constants.HEIGHT);
	}
	
}