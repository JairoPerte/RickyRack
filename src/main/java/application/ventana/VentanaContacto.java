package application.ventana;

import application.panels.PaneContacto;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaContacto extends Application {
	Label lblInfo;
	Image img;

	@Override
	public void start(Stage stage) {

		// añadimos a la escena el panel vertical
		Scene scene = createScene();
		stage.setTitle("Formulario de Contacto");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Funcion que abre la venta de adjuntar archivo para algun problema
	 */
	public void adjuntarArchivos() {
		Stage adjuntar = new Stage();

		// Cargar la imagen
		ImageView img = new ImageView(
				new Image("file:///c:/Users/Ruben/eclipse-workspace/JavaFXpruebas/img/arrastra.jpg"));

		// Tamaño de la imagen
		img.setFitWidth(300);
		img.setFitHeight(200);

		img.setOnMouseEntered(event -> {
			adjuntar.getScene().setCursor(Cursor.HAND);
		});

		// Restaurar el cursor al salir de la imagen
		img.setOnMouseExited(event -> {
			adjuntar.getScene().setCursor(Cursor.DEFAULT);
		});

		// Colocar la imagen en un StackPane
		StackPane ruta = new StackPane();

		ruta.getChildren().add(img);

		// Crear la escena y configurarla en la ventana
		Scene scene = new Scene(ruta, 300, 300);
		adjuntar.initModality(Modality.WINDOW_MODAL);
		adjuntar.setTitle("Arrastre su imagen aqui");
		adjuntar.setScene(scene);
		adjuntar.showAndWait();
	}

	public void mostrarConfirmacion(AlertType tipoAlerta) {

		if (tipoAlerta == Alert.AlertType.INFORMATION) {

			Alert infoAlert = new Alert(tipoAlerta);

			infoAlert.setTitle("Confirmacion");
			infoAlert.setContentText("Por favor, asegurese de que la informacion proporcionada es\nrelevante"
					+ ", nos tomamos muy en serio nuestro trabajo y por\ntanto, su informe.");
			infoAlert.setHeaderText("Lea detenidamente");
			infoAlert.showAndWait();
		}

	}
	
	public Scene createScene() {
	    VBox panelVertical = new VBox();

	    GridPane panelF = new PaneContacto();
	    panelVertical.getChildren().add(panelF);

	    Button adjuntarArchivo = new Button("Adjuntar Archivo");
	    Button btnVentanaInfo = new Button("Confirmar");

	    adjuntarArchivo.setOnAction(event -> {
	        adjuntarArchivos();
	    });

	    btnVentanaInfo.setOnAction(event -> {
	        mostrarConfirmacion(Alert.AlertType.INFORMATION);
	    });

	    // Añadir los botones al panel vertical
	    panelVertical.getChildren().addAll(adjuntarArchivo, btnVentanaInfo);

	    // Retornar la escena con el panel vertical
	    return new Scene(panelVertical);
	}




}
