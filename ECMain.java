package cmsc141.mp1.ec;

public class ECMain {

	public static void main(String[] args) {
	
	    ECView view = new ECView();
		ECController controller = new ECController(view);
		controller.show();
	}
}
