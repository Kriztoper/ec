package cmsc141.mp1.ec;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class ECFrame extends JFrame {

	public ECFrame() {
		setTitle("EC");
		setLayout(new BorderLayout());
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
