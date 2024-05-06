package application.ventana;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import application.exceptions.FaltaInterfaz;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaAutores extends Stage {

	/**
	 * Muestra un Stage que contiene a los autores del proyecto
	 */
	public VentanaAutores() {
		ScrollPane panelScroll = new ScrollPane();
		panelScroll.setFitToHeight(true);
		panelScroll.setFitToWidth(true);

		GridPane gP = new GridPane();
		Label lblDescCorta = new Label("RickyRack™ es una idea original de nuestro grupo");
		lblDescCorta.setStyle("-fx-font-weight: bold;");
		lblDescCorta.setWrapText(true);
		Label lbljairo = new Label("Jairo Pertegal Carrasco");
		lbljairo.setWrapText(true);
		Label lblruben = new Label("Rubén Hidalgo García");
		lblruben.setWrapText(true);
		Label lblmonica = new Label("Mónica Marroquín Ortiz");
		lblmonica.setWrapText(true);

		Label reparto = new Label("Reparto");
		reparto.setStyle("-fx-font-weight: bold;");
		Label repartoJairo = new Label("Jairo: Código y diseño del resto de la aplicación, los inserts Videojuegos");
		repartoJairo.setWrapText(true);
		Label repartoRuben = new Label("Ruben: código VentanaContacto.java, realizar E-R, los inserts Peliculas");
		repartoRuben.setWrapText(true);
		Label repartoMonica = new Label(
				"Mónica: código VentanaCambiarContrasena.java y VentanaEliminarUsuario.java, Realizar el word, realizar E-R, los inserts Libros");
		repartoMonica.setWrapText(true);

		Label mencion = new Label("Menciones honorificas");
		mencion.setStyle("-fx-font-weight: bold;");
		Label mencionAle = new Label("Alejandro, un máquina gracias a el he podido aplicar los css");
		mencionAle.setWrapText(true);
		Label mencionDarthVader = new Label("Darth Vader, ¿Por qué, quién no le gusta Star Wars, verdad?");
		mencionDarthVader.setWrapText(true);
		Label mencionHowLong = new Label(
				"HowLongToBeat, gracias a ellos me acabo de enterar que el tetris tiene final y te lo pasas en 5 horas");
		mencionHowLong.setWrapText(true);
		Label mencionFilmAffinity = new Label("FilmAffinity, para saber las calificaciones de las peliculas");
		mencionFilmAffinity.setWrapText(true);
		Label mecionSubnautica = new Label(
				"Unknown Worlds, por crear el mejor juego de supervivencia, Subnautica (me lo he pasado 7 veces ya)");
		mecionSubnautica.setWrapText(true);
		gP.getChildren().addAll(lblDescCorta, lbljairo, lblruben, lblmonica, new Label(), reparto, repartoJairo,
				repartoRuben, repartoMonica, new Label(), mencion, mencionAle, mencionHowLong, mencionDarthVader,
				mencionFilmAffinity, mecionSubnautica);
		for (int i = 0; i < gP.getChildren().size(); i++) {
			GridPane.setConstraints(gP.getChildren().get(i), 0, i);
			GridPane.setHalignment(gP.getChildren().get(i), HPos.CENTER);
		}
		gP.setAlignment(Pos.BASELINE_CENTER);

		panelScroll.setContent(gP);

		try {
			this.getIcons().add(new Image(new FileInputStream(".\\media\\img\\interfaz\\corazon-like.png")));
		} catch (FileNotFoundException e) {
			new FaltaInterfaz(AlertType.ERROR, this);
		}
		Scene escena = new Scene(panelScroll, 670, 200);
		escena.getStylesheets().add(getClass().getResource("/estilos/iniciosesion.css").toExternalForm());
		this.setTitle("Autores");
		this.initModality(Modality.WINDOW_MODAL);
		this.setScene(escena);

		this.show();
	}
}
