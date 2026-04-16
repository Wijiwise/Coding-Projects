package view;

import java.util.ArrayList;
import model.*;

public interface EnthusiastView {
	Enthusiast createEnthusiast();
	void showEnthusiast(Enthusiast enthusiast);
	void showAllEnthusiast(ArrayList<Enthusiast> enthusiast);
	Enthusiast showUpdate();
	int deleteEnthusiast();
	boolean showDelete(Enthusiast enthusiast);
}
