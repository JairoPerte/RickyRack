package application.ventana;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class VentanaAyuda extends Stage {

	/**
	 * Muestra un stage que tiene texto ayuda
	 */
	public VentanaAyuda() {
		VBox vbox = new VBox(5);

		Label contacta = new Label("Para más ayuda dirigete a nuestra páigna web");
		contacta.setTextAlignment(TextAlignment.CENTER);

		Label copyright = new Label("Copyright © by RickyRack All rights reserved");
		copyright.setTextAlignment(TextAlignment.CENTER);

		Hyperlink link = new Hyperlink("GitHub público de RickyRack");
		link.setTextAlignment(TextAlignment.CENTER);

		link.setOnMouseClicked(event -> {
			try {
				String os = System.getProperty("os.name").toLowerCase();
				if (os.contains("win")) {
					Runtime.getRuntime().exec("cmd /c start https://github.com/JairoPerte/RickyRack");
				} else if (os.contains("mac")) {
					Runtime.getRuntime().exec("open https://github.com/JairoPerte/RickyRack");
				} else if (os.contains("nix") || os.contains("nux")) {
					Runtime.getRuntime().exec("xdg-open https://github.com/JairoPerte/RickyRack");
				}
			} catch (Exception e) {
			}
		});

		WebView webView = new WebView();
		WebEngine webEngine = webView.getEngine();

		Button javadocPublic = new Button("Métodos Públicos");

		Button javadocPrivate = new Button("Métodos Privados");

		vbox.getChildren().addAll(contacta, copyright, link, javadocPublic, javadocPrivate);

		this.setTitle("Manual de Ayuda");
		this.setScene(new Scene(vbox, 270, 140));
		this.show();

		javadocPublic.setOnMouseClicked(event -> {
			StackPane sP = new StackPane();
			webEngine.load(getClass().getResource("/javadoc/doc-public/index.html").toExternalForm());
			sP.getChildren().add(webView);
			this.setScene(new Scene(sP, 700, 700));
		});

		javadocPrivate.setOnMouseClicked(event -> {
			StackPane sP = new StackPane();
			webEngine.load(getClass().getResource("/javadoc/doc-private/index.html").toExternalForm());
			sP.getChildren().add(webView);
			this.setScene(new Scene(sP, 700, 700));
		});
	}

}
