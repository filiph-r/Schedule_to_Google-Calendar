package app;

import javafx.application.Application;
import javafx.stage.Stage;
import view.View;

/**
 * Glavna klassa za pokretanje programa
 * 
 * <br>
 * createdby: Filip Hadzi-Ristic & Stefan Ginic
 *
 */
public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		View.get();
		System.out.println("Finished..");
	}

}
