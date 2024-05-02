package application.pasados;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

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
		Scene scene = eventosBotones(stage);
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

		//stackPane.getAccessibleText().

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

					// Construir la ruta de destino utilizando java.nio.file.Paths
					Path destination = Paths.get(filePath, raton.getFiles().get(0).getName());
					// Copiar el archivo a la ubicación especificada

					try {
						Document document = new Document();
						FileOutputStream pdFile = new FileOutputStream("fichero.pdf");

						PdfWriter.getInstance(document, pdFile);

						document.open();

						document.add(new Paragraph("aqui va el campo de observaciones"));

						Image image = Image.getInstance(filePath);

						image.scaleAbsolute(200, 200);

						document.add(image);

						document.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
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
			//Comprobacion de alerta -> TRUE crea pdf -> folder
			ButtonType btnPulsado = infoAlert.showAndWait().get();
			if (btnPulsado == ButtonType.CANCEL) {
				stage.close();
			} else {
				try {
					Document document = new Document();

					FileOutputStream pdFile = new FileOutputStream("fichero.pdf");

					PdfWriter.getInstance(document, pdFile);

					document.open();

					document.add(new Paragraph("Mi primer documento pdf"));

					Image image = Image.getInstance("img/Captura.png");

					image.scaleAbsolute(200, 200);

					document.add(image);

					document.close();

					System.out.println("Archivo PDF creado correctamente");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

	}

	public Scene eventosBotones(Stage stage) {
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
		return new Scene(panelVertical);
	}
}
