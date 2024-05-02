package application.pasados;

import java.sql.Connection;

import application.database.model.UsuarioDAO;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmarConstrasena extends Stage {

	private static int numFallos = 0;

	// Ventana Introduzca la contraseña por tema de seguridad:
	// textArea
	// Nueva Contraseña: textarea
	// Confirmar Contraseña: textarea
	// if(contraseña.getText().equals(confirmar.getText()){cambiarContraseñas}
	// else throws Contrasenas no coincidentes

	public ConfirmarConstrasena(Connection con, Stage stage, int userlog) {
		// espacio entre párrafos
		VBox vbox = new VBox(10);
		// Creamos los campos
		Label lblContrasena = new Label("Introduzca la contraseña");
		PasswordField pfContrasena = new PasswordField();

		Label lblConfirma = new Label("Confirme su contraseña");
		PasswordField pfConfirma = new PasswordField();

		Button btnConfirmar = new Button("Confirmar");

		// Eventos
		btnConfirmar.setOnAction(event -> {
			if (pfContrasena.getText().equals(pfConfirma.getText())) {
				if (UsuarioDAO.comprobarContrasena(con, userlog, pfContrasena.getText())) {
					// nueva Ventana (new VentanaCambiarContraseña)
					this.close();
				} else {
					// throws ContrasenaErronea
					numFallos++;
					if (numFallos > 3) {
						stage.close();
					}
				}
			} else {
				// throws ContrasenasNoCoincidentes
			}
		});

		vbox.getChildren().addAll(lblContrasena, pfContrasena, lblConfirma, pfConfirma, btnConfirmar);

		this.initOwner(stage);
		this.initModality(Modality.WINDOW_MODAL);
		this.setTitle("Eliminar cuenta");
		this.setScene(new Scene(vbox, 300, 200));
		this.showAndWait();
	}
}
