package view;

import java.io.File;
import java.util.Scanner;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/**
 * Prikazuje tekst json Metasema koja je prosledjena
 * 
 * <br>
 * createdby: Filip Hadzi-Ristic & Stefan Ginic
 *
 */
public class MetaScheme extends Form {

	public MetaScheme(String path) {
		super("Json Metascheme");
		setWidth(800);
		setHeight(800);
		setResizable(false);

		VBox vbox = new VBox();

		TextArea txt = new TextArea();
		txt.setMinHeight(770);
		txt.setMinWidth(790);
		txt.setEditable(false);

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(txt);
		scrollPane.setMinHeight(790);
		scrollPane.setMinWidth(790);

		try {
			StringBuilder sb = new StringBuilder();
			Scanner sc = new Scanner(new File(path));
			while (sc.hasNextLine())
				sb.append(sc.nextLine() + "\n");

			txt.setText(sb.toString());
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		vbox.getChildren().add(scrollPane);
		setPane(vbox);
	}

}
