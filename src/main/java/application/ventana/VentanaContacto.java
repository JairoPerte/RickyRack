package application.ventana;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import application.App;
import application.database.model.UsuarioDAO;
import application.exceptions.CampoObligatorios;
import application.exceptions.FaltaInterfaz;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaContacto extends Stage {

	File archivoAdjunto;
	TextField txtTitulo;
	ChoiceBox<String> chbCat;
	TextArea txtObs;

	private boolean primeraFoto = false;

	public VentanaContacto(Stage stage, Connection con) {
		GridPane contacto = new GridPane();

		Label lblTitulo = new Label("Titulo");
		Label lblCategoria = new Label("Tema");
		Label lblDescripcion = new Label("Descripcion del problema");

		Button btnEnviar = new Button("Crear Reporte");
		btnEnviar.setId("enviar");
		Button btnAdjuntar = new Button("Adjuntar Archivo");
		btnAdjuntar.setId("adjuntar");
		txtTitulo = new TextField();
		chbCat = new ChoiceBox<String>();
		txtObs = new TextArea();

		// añadimos elementos al choicebox
		chbCat.getItems().add("Perfil");
		chbCat.getItems().add("Sugerencias");
		chbCat.getItems().add("Informar de un error");
		chbCat.getItems().add("Otros");

		// posicionamos
		contacto.add(lblTitulo, 0, 0);
		contacto.add(txtTitulo, 1, 0);

		contacto.add(lblCategoria, 0, 1);
		contacto.add(chbCat, 1, 1);

		contacto.add(lblDescripcion, 0, 2);
		contacto.add(txtObs, 0, 3, 2, 2);

		contacto.add(btnAdjuntar, 0, 5);
		contacto.add(btnEnviar, 1, 5);

		// añadimos margenes
		GridPane.setMargin(lblTitulo, new Insets(5, 10, 5, 10));
		GridPane.setMargin(txtTitulo, new Insets(5, 10, 5, 10));

		GridPane.setMargin(lblCategoria, new Insets(5, 10, 5, 10));
		GridPane.setMargin(chbCat, new Insets(5, 10, 5, 10));

		GridPane.setMargin(lblDescripcion, new Insets(20, 10, 5, 10));
		GridPane.setMargin(txtObs, new Insets(5, 10, 20, 10));

		GridPane.setMargin(btnAdjuntar, new Insets(5, 10, 5, 10));
		GridPane.setMargin(btnEnviar, new Insets(5, 10, 5, 10));

		btnAdjuntar.setOnAction(event -> {
			adjuntarArchivos(this);
			if (archivoAdjunto != null) {
				if (primeraFoto) {
					contacto.getChildren().remove(8);
				}
				Label nombreImg = new Label(archivoAdjunto.toString());
				nombreImg.setId("nombre-img");
				nombreImg.setWrapText(true);
				contacto.add(nombreImg, 0, 6, 2, 1);
				GridPane.setMargin(nombreImg, new Insets(5, 10, 5, 10));
				primeraFoto = true;
			}
		});

		btnEnviar.setOnAction(event -> {
			mostrarConfirmacion(this, con);
		});

		Scene scene = new Scene(contacto, 500, 400);
		scene.getStylesheets().add(getClass().getResource("/estilos/contacto.css").toExternalForm());
		try {
			this.getIcons().add(new Image(new FileInputStream(".\\media\\img\\interfaz\\estrella-relleno.png")));
		} catch (FileNotFoundException e) {
			new FaltaInterfaz(AlertType.ERROR, this);
		}
		this.setTitle("Formulario de Contacto");
		this.initOwner(stage);
		this.initModality(Modality.WINDOW_MODAL);
		this.setScene(scene);
		this.show();
	}

	private void adjuntarArchivos(Stage stage) {
		Stage stackPaneStage = new Stage();

		StackPane stackPane = new StackPane();
		stackPane.setId("pane-blanco");

		try {
			ImageView botonAdd = new ImageView(
					new Image(new FileInputStream(".\\media\\img\\interfaz\\boton-add.png")));
			botonAdd.setFitHeight(50);
			botonAdd.setFitWidth(50);
			stackPane.getChildren().add(botonAdd);
		} catch (FileNotFoundException e) {
			new FaltaInterfaz(AlertType.ERROR, this);
		}

		stackPane.setOnDragOver(event -> {
			if (event.getGestureSource() != stackPane && event.getDragboard().hasFiles()) {
				event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
			}
			event.consume();
			stackPane.setId("pane-drag");
		});

		stackPane.setOnDragDropped(event -> {
			stackPane.setId("pane-blanco");
			var raton = event.getDragboard();
			boolean guardadoArchivo = false;
			if (raton.hasFiles() && !guardadoArchivo) {
				// Obtenemos su ruta y su extensión
				String ruta = raton.getFiles().get(0).toPath().toString();
				String extension = FilenameUtils.getExtension(ruta).toLowerCase();

				// Comprobamos la extension si es de imagen
				if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png")
						|| extension.equals("gif") || extension.equals("bmp") || extension.equals("svg")) {
					guardadoArchivo = true;
					archivoAdjunto = new File(ruta);
					stackPaneStage.close();
				} else {
					Alert alertaNoImg = new Alert(Alert.AlertType.ERROR);

					alertaNoImg.setTitle("El archivo adjunto no es una imagen");
					alertaNoImg.setContentText("Porfavor, asegurase que sea una imagen el archivo");
					alertaNoImg.setHeaderText("Error");
					alertaNoImg.initOwner(stackPaneStage);
					alertaNoImg.show();
				}
			}
			event.setDropCompleted(guardadoArchivo);
			event.consume();
		});

		Scene scene = new Scene(stackPane, 400, 300);
		scene.getStylesheets().add(getClass().getResource("/estilos/adjuntarcontacto.css").toExternalForm());
		stackPaneStage.setScene(scene);
		stackPaneStage.initModality(Modality.WINDOW_MODAL);
		stackPaneStage.initOwner(stage);
		stackPaneStage.setTitle("Arrastre y Suelte la Imagen");
		stackPaneStage.showAndWait();
	}

	private void mostrarConfirmacion(Stage stage, Connection con) {
		Alert infoAlert = new Alert(Alert.AlertType.CONFIRMATION);

		infoAlert.setTitle("Confirmación");
		TextFlow textFlow = new TextFlow();
		Text text = new Text(
				"Por favor, Asegúrese de que la información proporcionada es relevante. Ganarás 100 puntos de experiencia tras su reporte, (si ha iniciado sesión)");
		text.wrappingWidthProperty().bind(infoAlert.getDialogPane().widthProperty().subtract(20));

		textFlow.getChildren().add(text);
		infoAlert.getDialogPane().setContent(textFlow);
		infoAlert.setHeaderText("Lea detenidamente");
		infoAlert.initOwner(stage);

		ButtonType btnPulsado = infoAlert.showAndWait().get();
		if (btnPulsado != ButtonType.CANCEL) {
			if (txtTitulo.getText().length() != 0 && txtObs.getText().length() != 0 && chbCat.getValue() != null) {
				// Directorios Reportes
				File directorioReportes = new File(".\\Reportes");
				if (!directorioReportes.exists()) {
					directorioReportes.mkdir();
				}

				String rutaReporte = ".\\Reportes\\Reporte_1.pdf";
				String dirReporte = ".\\Reportes\\Reporte_1";

				for (int i = 1; new File(rutaReporte).exists(); i++) {
					dirReporte = ".\\Reportes\\Reporte_" + i;
					rutaReporte = dirReporte + ".pdf";
				}

				// Hacemos el pdf
				try {
					// Crear un nuevo documento PDF
					PDDocument documento = new PDDocument();
					PDPage pagina = new PDPage();
					documento.addPage(pagina);

					// Crear contenido para la página
					PDPageContentStream contenido = new PDPageContentStream(documento, pagina);

					contenido.beginText();
					contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
					contenido.newLineAtOffset(100, 700);
					contenido.showText("Reporte-" + java.time.LocalDate.now());
					contenido.endText();

					contenido.beginText();
					contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
					contenido.newLineAtOffset(100, 680);
					contenido.showText("Título: " + txtTitulo.getText());
					contenido.endText();

					contenido.beginText();
					contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
					contenido.newLineAtOffset(100, 660);
					contenido.showText("Categoría: " + chbCat.getValue());
					contenido.endText();

					contenido.beginText();
					contenido.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
					contenido.newLineAtOffset(100, 640);
					contenido.showText("Descripción: " + txtObs.getText());
					contenido.endText();

					contenido.close();

					// Guardar el documento en el archivo PDF
					documento.save(new File(rutaReporte));
					documento.close();
				} catch (IOException e) {
					infoAlert.close();
					Alert alertaNoSeGuardar = new Alert(Alert.AlertType.ERROR);

					alertaNoSeGuardar.setTitle("El pdf no se ha podido crear");
					alertaNoSeGuardar.setContentText("Ha habido algún problema al crear el pdf");
					alertaNoSeGuardar.setHeaderText("Error");
					alertaNoSeGuardar.initOwner(stage);
					alertaNoSeGuardar.show();
				}

				if (App.userLog != -1) {
					UsuarioDAO.amuentarXP(con, App.userLog, 100);
				}

				// Si hay un archivo
				if (archivoAdjunto != null) {
					if (archivoAdjunto.exists()) {
						try {
							File crearDirReporte = new File(dirReporte);
							crearDirReporte.mkdir();
							Path rutaArchivo = Paths.get(archivoAdjunto.toString());
							Path destinationPath = Paths.get(dirReporte, rutaArchivo.getFileName().toString());

							// Copia el archivo
							Files.copy(rutaArchivo, destinationPath);
						} catch (IOException e) {
							infoAlert.close();
							Alert alertaNoSePuedeCopiar = new Alert(Alert.AlertType.ERROR);

							alertaNoSePuedeCopiar.setTitle("El archivo no se ha podido copiar");
							alertaNoSePuedeCopiar.setContentText("Ha habido algún problema al copiar el archivo");
							alertaNoSePuedeCopiar.setHeaderText("Error");
							alertaNoSePuedeCopiar.initOwner(stage);
							alertaNoSePuedeCopiar.show();
						}
					} else {
						infoAlert.close();
						Alert alertaArchivoNoExiste = new Alert(Alert.AlertType.ERROR);

						alertaArchivoNoExiste.setTitle("El archivo adjunto no existe");
						alertaArchivoNoExiste.setContentText("Porfavor, asegurase que no lo haya borrado");
						alertaArchivoNoExiste.setHeaderText("Error");
						alertaArchivoNoExiste.initOwner(stage);
						alertaArchivoNoExiste.show();
					}
				}
				stage.close();
			} else {
				new CampoObligatorios(null, this);
			}
		}
	}

}
