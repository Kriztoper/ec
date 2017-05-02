package cmsc141.mp1.ec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextArea;

public class ECScanManager {

	private static String input;
	private static boolean enterKeyPressed;
	
	public static String scan(ECInterpreter ecInterpreter, JTextArea console) {
		input = "";
		enterKeyPressed = false;
		
		synchronized (ecInterpreter) {
			console.addKeyListener(new KeyListener() {
				
				@Override
				public void keyTyped(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						enterKeyPressed = true;
					} else {
						input += e.getKeyChar() + "";
					}
				}
				
				@Override
				public void keyReleased(KeyEvent e) {
					
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					
				}
			});
			
			while(!enterKeyPressed);
		}
		
		return input;
	}
}
