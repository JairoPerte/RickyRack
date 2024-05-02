package application.pasados;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import application.panels.PaneContacto;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaContacto extends Stage {
	public VentanaContacto(Stage stage) {
		// añadimos a la escena el panel vertical
		VBox panelVertical = new VBox();

		GridPane panelF = new PaneContacto();
		panelVertical.getChildren().add(panelF);

		Button adjuntarArchivo = new Button("Adjuntar Archivo");
		Button btnConfirmar = new Button("Confirmar");

		adjuntarArchivo.setOnAction(event -> {
			adjuntarArchivos(stage, "C:\\Users\\Ruben\\Downloads");
		});

		btnConfirmar.setOnAction(event -> {
			mostrarConfirmacion(stage);
		});

		// Añadir los botones al panel vertical
		panelVertical.getChildren().addAll(adjuntarArchivo, btnConfirmar);

		// Retornar la escena con el panel vertical
		Scene scene = new Scene(panelVertical);

		this.setTitle("Formulario de Contacto");
		this.initOwner(stage);
		this.initModality(Modality.WINDOW_MODAL);
		this.setScene(scene);
		this.show();
	}

	public void adjuntarArchivos(Stage stage, String filePath) {
		StackPane stackPane = new StackPane();
		Scene scene = new Scene(stackPane, 400, 300);
		// Creamos un nuevo Stage para el StackPane
		Stage stackPaneStage = new Stage();
		// Asignamos la escena al nuevo Stage
		stackPaneStage.setScene(scene);

		// stackPane.getAccessibleText().

		stackPane.setOnDragOver(event -> {
			if (event.getDragboard().hasFiles()) {
				event.acceptTransferModes(TransferMode.COPY);
			} else {
				event.consume();
			}
		});

		stackPane.setOnDragDropped(event -> {
			var raton = event.getDragboard();
			boolean success = false;
			if (raton.hasFiles()) {
				success = true;
				try {
					// Obtener el primer archivo arrastrado y soltado
					Path source = raton.getFiles().get(0).toPath();

					// Construir la ruta de destino utilizando
					// java.nio.file.Paths
					Path destination = Paths.get(filePath, raton.getFiles().get(0).getName());

					Files.copy(source, destination);
					// Cerrar la ventana que contiene el StackPane
					stackPaneStage.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			event.setDropCompleted(success);
			event.consume();
		});

		stackPaneStage.initModality(Modality.WINDOW_MODAL);
		stackPaneStage.initOwner(stage);
		stackPaneStage.setTitle("Arrastre y Suelte la Imagen");
		stackPaneStage.show();
	}

	public void mostrarConfirmacion(Stage stage) {
		Alert infoAlert = new Alert(Alert.AlertType.CONFIRMATION);

		infoAlert.setTitle("Confirmación");
		infoAlert.setContentText(
				"Por favor, asegúrese de que la información proporcionada es relevante, nos tomamos muy en serio nuestro trabajo y por tanto, su informe.");
		infoAlert.setHeaderText("Lea detenidamente");
		infoAlert.initOwner(stage);
		// Comprobacion de alerta -> TRUE crea pdf -> folder
		ButtonType btnPulsado = infoAlert.showAndWait().get();
		if (btnPulsado == ButtonType.CANCEL) {
			stage.close();
		} else {

		}

	}
}
