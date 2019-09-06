package actions;

import app.JsonParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Kalendar;
import models.Raspored;
import view.View;

/**
 * <br>
 * createdby: Filip Hadzi-Ristic & Stefan Ginic
 *
 */
public class SubmitAction implements EventHandler<ActionEvent> {
	private Alert alert;

	/**
	 * Akcija za Submit button. Parsira Json fajlove u java objekte uz pomoz pomocne
	 * komponente i ukoliko se sve uredu salje dalje tredu za sinhronizaciju
	 * kalendara i rasporeda sa google kalendarom
	 */
	public SubmitAction() {
		alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("An error acoured while parsing your Json files");
		alert.setContentText("Make sure that your Json files are compatible with there json metaschemes");
		Stage st = (Stage) alert.getDialogPane().getScene().getWindow();
		st.getIcons().add(new Image("file:ImgFiles\\icon.png"));
	}

	@Override
	public void handle(ActionEvent event) {
		try {
			JsonParser parser = new JsonParser("JsonFiles/Kalendar - Metaschema.json");
			Kalendar kalendar = (Kalendar) parser.parseJson(View.get().kalendarFile.getText(), Kalendar.class);

			parser.setMetaschema("JsonFiles/Raspored - Metaschema.json");
			Raspored raspored = (Raspored) parser.parseJson(View.get().rasporedFile.getText(), Raspored.class);

			View.get().setSyncPane();
			new Thread(new SyncThread(kalendar, raspored, View.get())).start();

		} catch (Exception e) {
			alert.showAndWait();
		}

	}

}
