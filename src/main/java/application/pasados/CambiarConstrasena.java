package application.pasados;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CambiarConstrasena extends Application{

	private int idusuario;
	
	
	//Ventana Introduzca la contraseña por tema de seguridad: textArea
	//Nueva Contraseña: textarea
	//Confirmar Contraseña: textarea
	// if(contraseña.getText().equals(confirmar.getText()){cambiarContraseñas}
	// else throws Contrasenas no coincidentes
	
	    @Override
	    public void start(Stage primaryStage) {
	        borrarCuenta(primaryStage);
	    }

	    public void borrarCuenta(Stage stage) {
	        Stage eliminarCuenta = new Stage();
	        //espacio entre párrafos
	        VBox vbox = new VBox(10); 
	        //Creamos los campos
	        Label lblContrasena = new Label("Introduzca la contraseña");
	        PasswordField pfContrasena = new PasswordField();

	        Label lblConfirma = new Label("Confirme su contraseña");
	        PasswordField pfConfirma = new PasswordField();

	        Button btnEliminar = new Button("Eliminar cuenta");
	        Button btnCancel = new Button("Cancelar");

	        //Eventos
	        btnEliminar.setOnAction(event -> {
	            if (pfContrasena.getText().equals(pfConfirma.getText())) {
	                //Aquí lógica para eliminar la cuenta
	                System.out.println("Cuenta eliminada correctamente.");
	                eliminarCuenta.close();
	            } else {
	                System.out.println("Las contraseñas no coinciden.");
	            }
	        });

	        btnCancel.setOnAction(event -> eliminarCuenta.close());

	        vbox.getChildren().addAll(lblContrasena, pfContrasena, lblConfirma, pfConfirma, btnEliminar, btnCancel);

	        eliminarCuenta.initOwner(stage);
	        eliminarCuenta.initModality(Modality.WINDOW_MODAL);
	        eliminarCuenta.setTitle("Eliminar cuenta");
	        eliminarCuenta.setScene(new Scene(vbox, 300, 200));
	        eliminarCuenta.show();
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }
	}
