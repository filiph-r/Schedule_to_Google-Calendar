package actions;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.View;

/**
 * Sluzi za biranje json fajla
 * 
 * <br>
 * createdby: Filip Hadzi-Ristic & Stefan Ginic
 */
public class BrowseAction implements EventHandler<ActionEvent> {

	private String rezim;
	public FileChooser chooser;
	public String file;
	private Stage parent;

	/**
	 * @param parent
	 *            paretn stage od filechoosera
	 * @param rezim
	 *            moze biti za kalendar ili za raspored
	 */
	public BrowseAction(Stage parent, String rezim) {
		this.parent = parent;
		this.rezim = rezim;

		chooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Json files (*.json)", "*.json");
		chooser.getExtensionFilters().add(extFilter);
		chooser.setTitle("Select Json file");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.event.EventHandler#handle(javafx.event.Event) otvara filechooser
	 * kako bi se izabrao json file za raspored ili za kalendar
	 */
	@Override
	public void handle(ActionEvent event) {
		try {
			file = chooser.showOpenDialog(parent).toString();

			if (rezim.equals("raspored"))
				View.get().rasporedFile.setText(file);
			else {
				View.get().kalendarFile.setText(file);
			}
		} catch (Exception e) {

		}

	}

}
