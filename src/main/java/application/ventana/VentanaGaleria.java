package application.ventana;

import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaGaleria extends Stage {

	private int posicion = 0;

	public VentanaGaleria(Stage stage, ArrayList<Image> galeria) {
		GridPane imagenes = new GridPane();

		ImageView imgMostrada = new ImageView(galeria.get(posicion));
		Button atras = new Button("Anterior");
		Button adelante = new Button("Siguiente");

		adelante.setOnMouseClicked(event -> {
			if (posicion != galeria.size() - 1) {
				posicion++;
				ImageView imgView = (ImageView) imagenes.getChildren().get(0);
				imgView.setImage(galeria.get(posicion));
			}
		});

		atras.setOnMouseClicked(event -> {
			if (posicion != 0) {
				posicion--;
				ImageView imgView = (ImageView) imagenes.getChildren().get(0);
				imgView.setImage(galeria.get(posicion));
			}
		});

		imgMostrada.setPreserveRatio(true);
		imgMostrada.setFitHeight(200);

		imagenes.add(imgMostrada, 0, 0, 2, 1);
		imagenes.add(adelante, 1, 1);
		imagenes.add(atras, 0, 1);

		this.setTitle("Galería");
		Scene escena = new Scene(imagenes, 600, 300);
		this.initModality(Modality.WINDOW_MODAL);
		this.initOwner(stage);
		this.setScene(escena);
		this.show();
	}
}
