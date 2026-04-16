import view.*;
import model.*;
import controller.*;

public class Main {
	public static void main(String[] args) {
		GUIView view = new GUIView();
	
		MainController app = new MainController(view); 	
		
		view.setListeners(app);	
		view.start();
	}
}