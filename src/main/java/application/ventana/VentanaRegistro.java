package application.ventana;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

import application.App;
import application.database.model.UsuarioDAO;
import application.exceptions.CampoObligatorios;
import application.exceptions.ContrasenasNoCoincidentes;
import application.exceptions.FaltaInterfaz;
import application.exceptions.UsuarioExistente;
import application.panels.PaneDistribucion;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaRegistro extends Stage {

	private int imagenSel = 1;

	HBox imagenes;

	public VentanaRegistro(Stage stage, Connection con) {
		GridPane registro = new GridPane();
		try {
			Label lblUsuario = new Label("Introduzca el usuario:");
			TextField usuario = new TextField();
			Label lblContrasena = new Label("Introduzca la contraseña:");
			PasswordField contrasena = new PasswordField();
			Label lblConContrasena = new Label("Confirme la contraseña:");
			PasswordField conContrasena = new PasswordField();

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
			female1.getStyleClass().add("imagen");
			female1.setId("imagen-selec");
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

			CheckBox guardarInicioSesion = new CheckBox("Guardar mi inicio de sesión");
			Button registrarse = new Button("Regristrarse");
			registrarse.setId("registrarse");

			registrarse.setOnMouseClicked(event -> {
				contrasena.setId("contrasena");
				usuario.setId("usuario");
				conContrasena.setId("confirmarcon");
				String txtUsuario = usuario.getText();
				String txtContrasena = contrasena.getText();
				try {
					if (!UsuarioDAO.getUsuario(con, txtUsuario).next()) {
						if (txtUsuario.length() != 0 && txtContrasena.length() != 0
								&& conContrasena.getText().length() != 0) {
							if (txtContrasena.equals(conContrasena.getText())) {
								int idGuardado = UsuarioDAO.crearUsuario(con, txtUsuario, txtContrasena, imagenSel,
										guardarInicioSesion.isSelected());
								App.userLog = idGuardado;
								// Creamos el PaneDistribucion con el id
								BorderPane nuevoPaneDistribution = new PaneDistribucion(App.userLog, stage, con);
								// Obtenemos la escena
								Scene escenaNueva = stage.getScene();
								this.close();
								escenaNueva.setRoot(nuevoPaneDistribution);
							} else {
								new ContrasenasNoCoincidentes(AlertType.WARNING, this);
							}
						} else {
							new CampoObligatorios(AlertType.WARNING, this);
						}
					} else {
						new UsuarioExistente(AlertType.WARNING);
					}
				} catch (SQLException e) {
				}
			});

			usuario.setId("usuario-click");

			usuario.setOnMouseClicked(event -> {
				usuario.setId("usuario-click");
				contrasena.setId("contrasena");
				conContrasena.setId("confirmarcon");
			});

			contrasena.setOnMouseClicked(event -> {
				usuario.setId("usuario");
				contrasena.setId("contrasena-click");
				conContrasena.setId("confirmarcon");
			});

			conContrasena.setOnMouseClicked(event -> {
				usuario.setId("usuario");
				contrasena.setId("contrasena");
				conContrasena.setId("confirmarcon-click");
			});

			guardarInicioSesion.setOnMouseClicked(event -> {
				contrasena.setId("contrasena");
				usuario.setId("usuario");
				conContrasena.setId("confirmarcon");
			});

			registrarse.setOnMouseEntered(event -> {
				this.getScene().setCursor(Cursor.HAND);
			});

			registrarse.setOnMouseExited(event -> {
				this.getScene().setCursor(Cursor.DEFAULT);
			});

			registro.getChildren().addAll(lblUsuario, usuario, lblContrasena, contrasena, guardarInicioSesion,
					registrarse, lblConContrasena, conContrasena, imagenes);

			GridPane.setConstraints(lblUsuario, 0, 0);
			GridPane.setConstraints(usuario, 1, 0);
			GridPane.setConstraints(lblContrasena, 0, 1);
			GridPane.setConstraints(contrasena, 1, 1);
			GridPane.setConstraints(lblConContrasena, 0, 2);
			GridPane.setConstraints(conContrasena, 1, 2);
			GridPane.setConstraints(imagenes, 0, 3, 2, 1);
			GridPane.setConstraints(guardarInicioSesion, 0, 4, 2, 1);
			GridPane.setConstraints(registrarse, 0, 5, 2, 1);

			GridPane.setMargin(lblUsuario, new Insets(5, 10, 5, 10));
			GridPane.setMargin(usuario, new Insets(5, 10, 5, 10));
			GridPane.setMargin(lblContrasena, new Insets(5, 10, 5, 10));
			GridPane.setMargin(contrasena, new Insets(5, 10, 5, 10));
			GridPane.setMargin(lblConContrasena, new Insets(5, 10, 5, 10));
			GridPane.setMargin(conContrasena, new Insets(5, 10, 5, 10));
			GridPane.setMargin(guardarInicioSesion, new Insets(5, 10, 5, 10));
			GridPane.setMargin(registrarse, new Insets(5, 10, 5, 10));
			GridPane.setMargin(imagenes, new Insets(5, 10, 5, 10));

			this.getIcons().add(new Image(new FileInputStream(".\\media\\img\\interfaz\\default-user-icon.png")));
		} catch (FileNotFoundException e) {
			new FaltaInterfaz(AlertType.ERROR, this);
		}
		Scene escena = new Scene(registro, 340, 250);
		escena.getStylesheets().add(getClass().getResource("/estilos/registro.css").toExternalForm());
		this.setTitle("Registro");
		this.initOwner(stage);
		this.initModality(Modality.WINDOW_MODAL);
		this.setScene(escena);

		// Mostramos
		this.showAndWait();
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
}
