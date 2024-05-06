package application.ventana;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.database.model.UsuarioDAO;
import javafx.geometry.HPos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaCambiarImg extends Stage {

	private int imagenSel;

	HBox imagenes;

	public VentanaCambiarImg(Connection con, int userLog, Stage stage) {
		try {
			GridPane paneTotal = new GridPane();

			Button confirmar = new Button("Confirmar");
			imagenes = new HBox(5);

			VBox female1 = new VBox();
			ImageView imgFemale1 = new ImageView(
					new Image(new FileInputStream(".\\media\\img\\interfaz\\defaultfemale1.png")));
			imgFemale1.setFitHeight(50);
			imgFemale1.setFitWidth(50);
			female1.getChildren().add(imgFemale1);

			VBox female2 = new VBox();
			ImageView imgFemale2 = new ImageView(
					new Image(new FileInputStream(".\\media\\img\\interfaz\\defaultfemale2.png")));
			imgFemale2.setFitHeight(50);
			imgFemale2.setFitWidth(50);
			female2.getChildren().add(imgFemale2);

			VBox male1 = new VBox();
			ImageView imgMale1 = new ImageView(
					new Image(new FileInputStream(".\\media\\img\\interfaz\\defaultmale1.png")));
			imgMale1.setFitHeight(50);
			imgMale1.setFitWidth(50);
			male1.getChildren().add(imgMale1);

			VBox male2 = new VBox();
			ImageView imgMale2 = new ImageView(
					new Image(new FileInputStream(".\\media\\img\\interfaz\\defaultmale2.png")));
			imgMale2.setFitHeight(50);
			imgMale2.setFitWidth(50);
			male2.getChildren().add(imgMale2);

			imagenes.getChildren().addAll(female1, female2, male1, male2);
			comprobarSeleccionado(userLog, con);
			female1.getStyleClass().add("imagen");
			female2.getStyleClass().add("imagen");
			male1.getStyleClass().add("imagen");
			male2.getStyleClass().add("imagen");

			// FEMALE1
			female1.setOnMouseClicked(event -> {
				borrarSeleccion(imagenSel);
				imagenSel = 1;
				female1.setId("imagen-selec");
			});

			female1.setOnMouseEntered(event -> {
				this.getScene().setCursor(Cursor.HAND);
			});

			female1.setOnMouseExited(event -> {
				this.getScene().setCursor(Cursor.DEFAULT);
			});

			// FEMALE2
			female2.setOnMouseClicked(event -> {
				borrarSeleccion(imagenSel);
				imagenSel = 2;
				female2.setId("imagen-selec");
			});

			female2.setOnMouseEntered(event -> {
				this.getScene().setCursor(Cursor.HAND);
			});

			female2.setOnMouseExited(event -> {
				this.getScene().setCursor(Cursor.DEFAULT);
			});

			// MALE1
			male1.setOnMouseClicked(event -> {
				borrarSeleccion(imagenSel);
				imagenSel = 3;
				male1.setId("imagen-selec");
			});

			male1.setOnMouseEntered(event -> {
				this.getScene().setCursor(Cursor.HAND);
			});

			male1.setOnMouseExited(event -> {
				this.getScene().setCursor(Cursor.DEFAULT);
			});

			// MALE2
			male2.setOnMouseClicked(event -> {
				borrarSeleccion(imagenSel);
				imagenSel = 4;
				male2.setId("imagen-selec");
			});

			male2.setOnMouseEntered(event -> {
				this.getScene().setCursor(Cursor.HAND);
			});

			male2.setOnMouseExited(event -> {
				this.getScene().setCursor(Cursor.DEFAULT);
			});

			confirmar.setOnMouseClicked(e -> {
				UsuarioDAO.cambiarImagen(con, userLog, imagenSel);
				this.close();
			});

			paneTotal.getChildren().addAll(imagenes, confirmar);

			GridPane.setConstraints(imagenes, 0, 0);
			GridPane.setConstraints(confirmar, 0, 1);

			GridPane.setHalignment(imagenes, HPos.CENTER);
			GridPane.setHalignment(confirmar, HPos.CENTER);

			this.initModality(Modality.WINDOW_MODAL);
			this.initOwner(stage);
			this.setTitle("Seleccionar");
			Scene escena = new Scene(paneTotal, 250, 90);
			escena.getStylesheets().add(getClass().getResource("/estilos/cambiarimg.css").toExternalForm());
			this.setScene(escena);
			this.show();
		} catch (FileNotFoundException e) {
			// throws FaltaInterfaz
		}
	}

	private void borrarSeleccion(int imgSel) {
		switch (imgSel) {
		case 1:
			imagenes.getChildren().get(0).setId("1");
			break;
		case 2:
			imagenes.getChildren().get(1).setId("2");
			break;
		case 3:
			imagenes.getChildren().get(2).setId("3");
			break;
		case 4:
			imagenes.getChildren().get(3).setId("4");
			break;
		}
	}

	private void comprobarSeleccionado(int userLog, Connection con) {
		try {
			ResultSet rs = UsuarioDAO.getUsuario(con, userLog);
			rs.next();
			switch (rs.getInt("imagen")) {
			case 1:
				imagenSel = 1;
				imagenes.getChildren().get(0).setId("imagen-selec");
				break;
			case 2:
				imagenSel = 2;
				imagenes.getChildren().get(1).setId("imagen-selec");
				break;
			case 3:
				imagenSel = 3;
				imagenes.getChildren().get(2).setId("imagen-selec");
				break;
			case 4:
				imagenSel = 4;
				imagenes.getChildren().get(3).setId("imagen-selec");
				break;
			}
		} catch (SQLException e) {
		}
	}

}
