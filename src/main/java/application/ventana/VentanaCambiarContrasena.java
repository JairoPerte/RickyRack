package application.ventana;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;

import application.database.model.UsuarioDAO;
import application.exceptions.CampoObligatorios;
import application.exceptions.ContrasenaErronea;
import application.exceptions.ContrasenasNoCoincidentes;
import application.exceptions.FaltaInterfaz;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaCambiarContrasena extends Stage {

	private int numFallos = 0;

	/**
	 * Muestra un stage para la cambiar contraseña del id
	 * conectado
	 * 
	 * @param con     conexion a la base de datos
	 * @param stage   stage de la aplicación principal
	 * @param userLog id del usuario conectado
	 */
	public VentanaCambiarContrasena(Connection con, Stage stage, int userLog) {
		GridPane gP = new GridPane();

		Label lblContrasenaAnt = new Label("Introduzca la contraseña antigua: ");
		PasswordField pfContrasenaAnt = new PasswordField();

		Label lblContrasena = new Label("Introduzca la nueva contraseña: ");
		PasswordField pfContrasena = new PasswordField();

		Label lblConfirma = new Label("Confirme la nueva contraseña: ");
		PasswordField pfConfirma = new PasswordField();

		Button btnConfirmar = new Button("Cambiar");

		btnConfirmar.setOnAction(event -> {
			if (pfContrasenaAnt.getText().length() != 0 && pfConfirma.getText().length() != 0
					&& pfContrasenaAnt.getText().length() != 0) {
				if (pfContrasena.getText().equals(pfConfirma.getText())) {
					UsuarioDAO.cambiarContrasena(con, userLog, pfContrasenaAnt.getText(), pfContrasena.getText(), this);

					// Si se sigue mostrando es que ha fallado
					if (this.isShowing()) {
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

		gP.getChildren().addAll(lblContrasenaAnt, pfContrasenaAnt, lblContrasena, pfContrasena, lblConfirma, pfConfirma,
				btnConfirmar);

		GridPane.setConstraints(lblContrasenaAnt, 0, 0);
		GridPane.setConstraints(pfContrasenaAnt, 1, 0);
		GridPane.setConstraints(lblContrasena, 0, 1);
		GridPane.setConstraints(pfContrasena, 1, 1);
		GridPane.setConstraints(lblConfirma, 0, 2);
		GridPane.setConstraints(pfConfirma, 1, 2);
		GridPane.setConstraints(btnConfirmar, 0, 3);

		GridPane.setMargin(lblContrasenaAnt, new Insets(10, 5, 10, 5));
		GridPane.setMargin(pfContrasenaAnt, new Insets(10, 5, 10, 5));
		GridPane.setMargin(lblContrasena, new Insets(10, 5, 10, 5));
		GridPane.setMargin(pfContrasena, new Insets(10, 5, 10, 5));
		GridPane.setMargin(lblConfirma, new Insets(10, 5, 10, 5));
		GridPane.setMargin(pfConfirma, new Insets(10, 5, 10, 5));
		GridPane.setMargin(btnConfirmar, new Insets(10, 5, 10, 5));

		this.initOwner(stage);
		this.initModality(Modality.WINDOW_MODAL);
		this.setTitle("Cambiar Contraseña");
		try {
			this.getIcons().add(new Image(new FileInputStream(".\\media\\img\\interfaz\\candado.png")));
		} catch (FileNotFoundException e) {
			new FaltaInterfaz(AlertType.ERROR, this);
		}
		this.setScene(new Scene(gP, 400, 200));
		this.showAndWait();
	}
}
