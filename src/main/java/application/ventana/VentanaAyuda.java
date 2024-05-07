package application.ventana;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
		contacta.setAlignment(Pos.CENTER);

		WebView webView = new WebView();
		WebEngine webEngine = webView.getEngine();

		Button javadocPublic = new Button("Métodos Públicos");
		Button javadocPrivate = new Button("Métodos Privados");

		vbox.getChildren().addAll(contacta, javadocPublic, javadocPrivate);

		this.setTitle("Manual de Ayuda");
		this.setScene(new Scene(vbox, 180, 100));
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
