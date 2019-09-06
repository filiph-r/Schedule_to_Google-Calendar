package actions;

import javafx.application.Platform;
import models.Kalendar;
import models.Raspored;
import view.View;

/**
 * Thred na kom se izvrsava sinhronizacija kalendara i rasporeda sa google
 * kalendarom
 * 
 * <br>
 * createdby: Filip Hadzi-Ristic & Stefan Ginic
 * 
 */
public class SyncThread implements Runnable {

	private Kalendar kalendar;
	private Raspored raspored;
	private View view;

	public SyncThread(Kalendar kalendar, Raspored raspored, View view) {
		this.kalendar = kalendar;
		this.raspored = raspored;
		this.view = view;
	}

	@Override
	public void run() {

		try {

			kalendar.parse();
			GoogleCallendarUploader.upload(kalendar, raspored);

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					view.setMainPane();
				}
			});

		} catch (Exception e) {
			System.out.println("thread failure");
		}

	}

}
