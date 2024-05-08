package application.ventana;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;

import application.App;
import application.cookies.CookieWriter;
import application.database.model.UsuarioDAO;
import application.exceptions.CampoObligatorios;
import application.exceptions.ContrasenaErronea;
import application.exceptions.ContrasenasNoCoincidentes;
import application.exceptions.FaltaInterfaz;
import application.panels.PaneDistribucion;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaEliminarCuenta extends Stage {

	private int numFallos = 0;

	/**
	 * Muestra la venta de elimar cuenta que pide confirmar la
	 * contraseña para luego borrarte la cuenta si es correcta
	 * la contraseña
	 * 
	 * @param con     conexión con la BD
	 * @param stage   stage de la aplicación principal
	 * @param userLog id del usuario conectado (a borrar)
	 */
	public VentanaEliminarCuenta(Connection con, Stage stage, int userLog) {
		GridPane gP = new GridPane();

		Label lblContrasena = new Label("Introduzca la contraseña: ");
		PasswordField pfContrasena = new PasswordField();

		Label lblConfirma = new Label("Confirme la contraseña: ");
		PasswordField pfConfirma = new PasswordField();

		Button btnEliminar = new Button("Eliminar");
		btnEliminar.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-background-color: #ff0000;");

		btnEliminar.setOnAction(event -> {
			if (pfContrasena.getText().length() != 0 && pfConfirma.getText().length() != 0) {
				if (pfContrasena.getText().equals(pfConfirma.getText())) {
					if (UsuarioDAO.comprobarContrasena(con, userLog, pfContrasena.getText())) {

						if (alerta(this)) {
							UsuarioDAO.eliminarUsuario(con, userLog);
							CookieWriter.eliminarCookie(userLog);
							// Desconectado por defecto
							App.userLog = -1;
							// Lo cerramos
							// Mostramos la ventan de inicio de sesión de cookies
							// guardadas (por si quiere iniciar sesión con otra cuenta o
							// no iniciar sesión)
							new VentanaInicioSesion(con);
							this.close();
							stage.close();
							// Para que cargen de nuevo los cambios
							Scene escenaNueva = new Scene(new PaneDistribucion(App.userLog, stage, con), 900, 700);
							escenaNueva.getStylesheets()
									.add(getClass().getResource("/estilos/application.css").toExternalForm());
							stage.setScene(escenaNueva);
							stage.show();
						} else {
							this.close();
						}
					} else {
						numFallos++;
						if (numFallos > 3) {
							stage.close();
						}
						new ContrasenaErronea(AlertType.ERROR, this, numFallos);
					}
				} else {
					new ContrasenasNoCoincidentes(AlertType.WARNING, this);
				}
			} else {
				new CampoObligatorios(AlertType.WARNING, this);
			}
		});

		btnEliminar.setOnMouseEntered(event -> {
			this.getScene().setCursor(Cursor.HAND);
		});

		btnEliminar.setOnMouseExited(event -> {
			this.getScene().setCursor(Cursor.DEFAULT);
		});

		gP.getChildren().addAll(lblContrasena, pfContrasena, lblConfirma, pfConfirma, btnEliminar);

		GridPane.setConstraints(lblContrasena, 0, 0);
		GridPane.setConstraints(pfContrasena, 1, 0);
		GridPane.setConstraints(lblConfirma, 0, 1);
		GridPane.setConstraints(pfConfirma, 1, 1);
		GridPane.setConstraints(btnEliminar, 0, 2, 2, 1);
		GridPane.setHalignment(btnEliminar, HPos.CENTER);

		GridPane.setMargin(lblContrasena, new Insets(10, 5, 10, 5));
		GridPane.setMargin(pfContrasena, new Insets(10, 5, 10, 5));
		GridPane.setMargin(lblConfirma, new Insets(10, 5, 10, 5));
		GridPane.setMargin(pfConfirma, new Insets(10, 5, 10, 5));
		GridPane.setMargin(btnEliminar, new Insets(10, 5, 10, 5));

		this.initOwner(stage);
		this.initModality(Modality.WINDOW_MODAL);
		this.setTitle("Eliminar");
		try {
			this.getIcons().add(new Image(new FileInputStream(".\\media\\img\\interfaz\\cruz-roja.png")));
		} catch (FileNotFoundException e) {
			new FaltaInterfaz(AlertType.ERROR, this);
		}
		this.setScene(new Scene(gP, 330, 150));
		this.showAndWait();
	}

	/**
	 * Alerta que muestra si el usuario quiere borrar la cuenta
	 * o por lo contrario se arrepiente
	 * 
	 * @param  stage stage de VentanaEliminarCuenta(this)
	 * @return       true si quiere borrar la cuenta false sino
	 */
	private boolean alerta(Stage stage) {
		try {
			Alert alerta = new Alert(Alert.AlertType.WARNING);

			alerta.setTitle("Borrar cuenta");
			alerta.setContentText(
					"Multiple Leviathan class creatures detected. Are you certain whatever you're doing is worth it?");
			alerta.setHeaderText("Estas a un click de eliminar tu cuenta");
			alerta.initOwner(stage);

			ButtonType btnPulsado = alerta.showAndWait().get();
			if (btnPulsado == ButtonType.OK) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
}
