package application.ventana;

import java.io.File;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;

import application.database.model.ProductoDAO;
import application.database.model.UsuarioDAO;
import application.exceptions.CampoObligatorios;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaAnadirProducto extends Stage {

	private ArrayList<File> imagenes = new ArrayList<File>();
	private ArrayList<String> tipo = new ArrayList<String>();

	public VentanaAnadirProducto(Stage stage, Connection con, int categoria, int userLog) {
		GridPane anadirProd = new GridPane();

		Label titulo = new Label("Introduzca el título:");
		GridPane.setMargin(titulo, new Insets(10, 5, 10, 5));
		anadirProd.add(titulo, 0, 0);

		TextField txtTitulo = new TextField();
		GridPane.setMargin(txtTitulo, new Insets(10, 5, 10, 5));
		anadirProd.add(txtTitulo, 1, 0);

		TextField duracion = new TextField();
		GridPane.setMargin(duracion, new Insets(10, 5, 10, 5));
		anadirProd.add(duracion, 1, 1);

		ChoiceBox<String> genero = new ChoiceBox<String>();
		GridPane.setMargin(genero, new Insets(10, 5, 10, 5));
		anadirProd.add(genero, 0, 2);

		DatePicker fecha = new DatePicker();
		GridPane.setMargin(fecha, new Insets(10, 5, 10, 5));
		fecha.setValue(LocalDate.now());
		anadirProd.add(fecha, 1, 2);

		Label autor = new Label("Introduzca el autor:");
		GridPane.setMargin(autor, new Insets(10, 5, 10, 5));
		anadirProd.add(autor, 0, 3);

		TextField txtAutor = new TextField();
		GridPane.setMargin(txtAutor, new Insets(10, 5, 10, 5));
		anadirProd.add(txtAutor, 1, 3);

		Label sinopsis = new Label("Introduzca la sinopsis:");
		GridPane.setMargin(sinopsis, new Insets(10, 5, 10, 5));
		anadirProd.add(sinopsis, 0, 4);

		TextArea txtSinopsis = new TextArea();
		GridPane.setMargin(txtSinopsis, new Insets(10, 5, 10, 5));
		anadirProd.add(txtSinopsis, 0, 5, 2, 2);

		Button anadirImg = new Button("Añadir Imagenes");
		GridPane.setMargin(anadirImg, new Insets(10, 5, 10, 5));
		anadirProd.add(anadirImg, 0, 7);

		CheckBox imgPortada = new CheckBox("Marcar como Portada la siguiente imagen");
		GridPane.setMargin(anadirImg, new Insets(10, 5, 10, 5));
		anadirProd.add(imgPortada, 1, 7);

		anadirImg.setOnAction(event -> {
			// Crear un objeto FileChooser
			FileChooser fileChooser = new FileChooser();

			// Configurar título y filtro de archivos
			fileChooser.setTitle("Seleccionar una imagen");
			fileChooser.getExtensionFilters()
					.addAll(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.gif", "*.bmp"));

			// Mostrar el diálogo para seleccionar el archivo
			File archivoSeleccionado = fileChooser.showOpenDialog(stage);

			// Procesar el archivo seleccionado (aquí puedes realizar la
			// operación deseada)
			if (archivoSeleccionado != null) {
				imagenes.add(archivoSeleccionado);
				if (imgPortada.isSelected()) {
					tipo.add("P");
				} else {
					tipo.add("I");
				}
			}
		});

		Button aceptar = new Button("Crear Producto");
		GridPane.setMargin(aceptar, new Insets(10, 5, 10, 5));
		anadirProd.add(aceptar, 0, 8, 2, 1);
		GridPane.setHalignment(aceptar, HPos.CENTER);

		switch (categoria) {
		case 0:
			Label duracionL = new Label("Introduzca número de páginas:");
			genero.getItems().addAll("Thriller", "Fantasía", "Romántica", "Ciencia-ficción", "Suspense", "Terror",
					"Animación");
			GridPane.setMargin(duracionL, new Insets(10, 5, 10, 5));
			anadirProd.add(duracionL, 0, 1);
			break;
		case 1:
			Label duracionP = new Label("Introduzca cantidad de minutos:");
			genero.getItems().addAll("Thriller", "Fantasía", "Romántica", "Ciencia-ficción", "Suspense", "Terror",
					"Animación");
			GridPane.setMargin(duracionP, new Insets(10, 5, 10, 5));
			anadirProd.add(duracionP, 0, 1);
			break;
		case 2:
			Label duracionV = new Label("Introduzca cantidad de horas:");
			genero.getItems().addAll("Aventuras", "Supervivencia", "RPG", "Puzles", "Plataformas");
			GridPane.setMargin(duracionV, new Insets(10, 5, 10, 5));
			anadirProd.add(duracionV, 0, 1);
			break;
		}

		aceptar.setOnMouseClicked(event -> {
			if (txtTitulo.getText().length() != 0 && txtSinopsis.getText().length() != 0
					&& txtAutor.getText().length() != 0 && duracion.getText().length() != 0
					&& genero.getValue() != null) {
				try {
					int durar = Integer.valueOf(duracion.getText());
					int id = ProductoDAO.crearProducto(con, categoria, txtTitulo.getText(), txtSinopsis.getText(),
							genero.getValue(), durar, txtAutor.getText(), fecha.getValue().toString());
					int indice = 0;
					for (File imagen : imagenes) {
						ProductoDAO.insertarMultimedia(con, id, imagen, tipo.get(indice));
						indice++;
					}
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Felicidades has subido el articulo");
					alert.setHeaderText("Acabas de ganar 200 de experiencia");
					UsuarioDAO.amuentarXP(con, userLog, 200);
					alert.show();
					this.close();
				} catch (NumberFormatException e) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Revise sus datos");
					alert.setHeaderText("Porfavor introduzca un numero entero en duración");
					alert.show();
				}
			} else {
				new CampoObligatorios(AlertType.WARNING, this);
			}
		});

		Scene escena = new Scene(anadirProd, 500, 500);
		this.setTitle("Crear Producto");
		this.initOwner(stage);
		this.initModality(Modality.WINDOW_MODAL);
		this.setScene(escena);

		// Mostramos
		this.show();
	}
}
