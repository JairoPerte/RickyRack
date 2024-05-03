package application.pasados;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CambiarImgPerfil2 extends Application {

	private int idusuario;

	// Connection con=UtilsBD.conectarBD();
	// ResulSet rs=UsuarioDAO.
	// Rs= getUsuario(con,idUsuario) import usuarioDAO

	// ventana nueva que muestre 4 imagenes (1,2,3,4)
	// evento img1(ruta) -> int valor=1
	// Confirmar (botón en el abajo en el medio) ->
	// cambiarImagen(con,valor);

	private static int imagenSeleccionada = 1;

	private final static int FEMALE_1 = 1;
	private final static int FEMALE_2 = 2;
	private final static int MALE_1 = 3;
	private final static int MALE_2 = 4;

	public void start(Stage primaryStage) {
		cambiarImagen(1);
	}

	public void cambiarImagen(int id) {
		Stage seleccion = new Stage();
		// Añadimos espacio entre imágenes
		GridPane paneTotal = new GridPane();

		Button confirmar = new Button("Confirmar");
		VBox imagenes = new VBox();

		try {
			ImageView female1 = new ImageView(new Image(new FileInputStream("/media/img/interfaz/defaultfemale1.png")));
			ImageView female2 = new ImageView(new Image(new FileInputStream("/media/img/interfaz/defaultfemale2.png")));
			ImageView male1 = new ImageView(new Image(new FileInputStream("/media/img/interfaz/defaultmale1.png")));
			ImageView male2 = new ImageView(new Image(new FileInputStream("/media/img/interfaz/defaultmale2.png")));

			female1.setFitHeight(100);
			female1.setFitWidth(100);
			female2.setFitHeight(100);
			female2.setFitWidth(100);
			male1.setFitHeight(100);
			male1.setFitWidth(100);
			male2.setFitHeight(100);
			male2.setFitWidth(100);

			imagenes.getChildren().addAll(female1, female2, male1, male2);
			paneTotal.getChildren().addAll(imagenes, confirmar);
			GridPane.setConstraints(imagenes, 0, 0);
			GridPane.setConstraints(confirmar, 0, 1);

			// Eventos
			female1.setOnMouseClicked(e -> {
				imagenSeleccionada = FEMALE_1;
			});
			female2.setOnMouseClicked(e -> {
				imagenSeleccionada = FEMALE_2;
			});
			male1.setOnMouseClicked(e -> {
				imagenSeleccionada = MALE_1;
			});
			male2.setOnMouseClicked(e -> {
				imagenSeleccionada = MALE_2;
			});
		} catch (FileNotFoundException e) {
			// throws FaltaInterfaz
		}

		confirmar.setOnMouseClicked(e -> {
			// cambiar imagen
			seleccion.close();
		});

		seleccion.initModality(Modality.WINDOW_MODAL);
		seleccion.setTitle("Seleccionar nueva imagen de perfil");
		seleccion.setScene(new Scene(imagenes, 500, 500));
		seleccion.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
