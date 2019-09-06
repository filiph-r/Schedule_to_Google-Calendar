package view;

import java.awt.image.BufferedImage;

import actions.BrowseAction;
import actions.SubmitAction;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * Glavni prozor koj prikazuje scenu za unos podataka i loading scenu
 * 
 * <br>
 * createdby: Filip Hadzi-Ristic & Stefan Ginic
 *
 */
public class View extends Form {
	private static View instance = null;
	private GridPane syncPane;
	private GridPane mainPane;
	private Scene mainScene;
	private Scene syncScene;

	public TextField rasporedFile;
	public TextField kalendarFile;
	public Button browseRaspored;
	public Button browseKalendar;
	public Button viewKalendarMeta;
	public Button viewRasporedMeta;
	public Button submit;

	public BrowseAction browseAction1 = new BrowseAction(this, "raspored");
	public BrowseAction browseAction2 = new BrowseAction(this, "kalendar");

	public static View get() {
		if (instance == null)
			instance = new View();
		return instance;
	}

	public View() {
		super("Calendar Synchronizer");

		setWidth(400);
		setHeight(550);
		setResizable(false);

		mainPane = new GridPane();
		mainPane.setAlignment(Pos.CENTER);

		ImageView img = new ImageView("file:ImgFiles/googleCal.png");
		GridPane gp = new GridPane();
		gp.setVgap(15);
		gp.setHgap(15);

		rasporedFile = new TextField();
		rasporedFile.setMinWidth(250);

		browseRaspored = new Button("Browse");
		browseRaspored.setOnAction(browseAction1);

		kalendarFile = new TextField();
		kalendarFile.setMinWidth(250);

		browseKalendar = new Button("Browse");
		browseKalendar.setOnAction(browseAction2);

		viewKalendarMeta = new Button("Open Calender Json MetaScheme");
		viewKalendarMeta.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				new MetaScheme("JsonFiles/Kalendar - Metaschema.json");

			}
		});
		viewRasporedMeta = new Button("Open Schedule Json MetaScheme");
		viewRasporedMeta.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				new MetaScheme("JsonFiles/Raspored - Metaschema.json");

			}
		});

		submit = new Button("Submit");
		submit.setPrefSize(100, 35);
		submit.setOnAction(new SubmitAction());

		gp.add(kalendarFile, 0, 0);
		gp.add(browseKalendar, 1, 0);
		gp.add(viewKalendarMeta, 0, 1);

		gp.add(rasporedFile, 0, 2);
		gp.add(browseRaspored, 1, 2);
		gp.add(viewRasporedMeta, 0, 3);
		gp.setAlignment(Pos.CENTER);

		mainPane.add(img, 0, 0);
		mainPane.add(gp, 0, 1);
		mainPane.add(submit, 0, 2);
		mainPane.setVgap(30);
		mainPane.setHalignment(img, HPos.CENTER);
		mainPane.setHalignment(submit, HPos.CENTER);

		mainPane.setStyle("-fx-background-color: #ffffff;");

		initSyncScreen();
		setMainPane();
	}

	/**
	 * ucitava loading scenu u memoriju sve sa animacijim i ostatkom scene
	 */
	public void initSyncScreen() {
		syncPane = new GridPane();

		Animation ani = new AnimatedGif("ImgFiles\\loading.gif", 1000);
		ani.setCycleCount(Animation.INDEFINITE);
		ani.play();

		syncPane.setStyle("-fx-background-color: #ffffff;");
		syncPane.setAlignment(Pos.CENTER);
		syncPane.add(ani.getView(), 0, 0);

		Label label = new Label("Uploading to your calendar\n    this can take a while..");
		syncPane.add(label, 0, 1);
		label.setAlignment(Pos.CENTER);
		label.setFont(new Font("Arial", 18));
		mainPane.setHalignment(label, HPos.CENTER);
	}

	public void setMainPane() {
		if (mainScene == null)
			mainScene = new Scene(mainPane);

		setScene(mainScene);
		if (!isShowing())
			show();
	}

	public void setSyncPane() {
		if (syncScene == null)
			syncScene = new Scene(syncPane);

		setScene(syncScene);
		if (!isShowing())
			show();
	}

	/**
	 * @author Internet
	 *
	 */
	public class AnimatedGif extends Animation {

		public AnimatedGif(String filename, double durationMs) {

			GifDecoder d = new GifDecoder();
			d.read(filename);

			Image[] sequence = new Image[d.getFrameCount()];
			for (int i = 0; i < d.getFrameCount(); i++) {

				WritableImage wimg = null;
				BufferedImage bimg = d.getFrame(i);
				sequence[i] = SwingFXUtils.toFXImage(bimg, wimg);

			}

			super.init(sequence, durationMs);
		}

	}

	/**
	 * @author Internet
	 *
	 */
	public class Animation extends Transition {

		private ImageView imageView;
		private int count;

		private int lastIndex;

		private Image[] sequence;

		private Animation() {
		}

		public Animation(Image[] sequence, double durationMs) {
			init(sequence, durationMs);
		}

		private void init(Image[] sequence, double durationMs) {
			this.imageView = new ImageView(sequence[0]);
			this.sequence = sequence;
			this.count = sequence.length;

			setCycleCount(1);
			setCycleDuration(Duration.millis(durationMs));
			setInterpolator(Interpolator.LINEAR);

		}

		protected void interpolate(double k) {

			final int index = Math.min((int) Math.floor(k * count), count - 1);
			if (index != lastIndex) {
				imageView.setImage(sequence[index]);
				lastIndex = index;
			}

		}

		public ImageView getView() {
			return imageView;
		}

	}

}
