package application.pasados;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class PaneContacto extends GridPane {
	//variables miembro de la clase

	public TextField txtUsuario;
	public ChoiceBox<String> chbCat;
	public TextArea txtObs;
	public Button btnEnviar = new Button();
	public Button btnAdjuntar = new Button();
	

	public PaneContacto() {
		Label lblUsuario = new Label("Usuario");
		Label lblCategoria = new Label("Tema");
		Label lblDescripcion = new Label("Descripcion del problema");

		btnEnviar = new Button("Enviar");		
		//creamos los controles
		txtUsuario = new TextField();
		chbCat = new ChoiceBox<String>();
		txtObs = new TextArea();
		
		//añadimos elementos al choicebox
		chbCat.getItems().add("Perfil");
		chbCat.getItems().add("Sugerencias");
		chbCat.getItems().add("Informar de un error");
		chbCat.getItems().add("Otros");
		
		//posicionamos los elementos en el panel grid
		//como es una clase extendida de GridPane, se puede utilizar directamente this
		this.add(lblUsuario, 0, 0);
		this.add(txtUsuario, 1, 0);
		
		this.add(lblCategoria, 0, 1);
		this.add(chbCat, 1, 1);
		
		this.add(lblDescripcion, 0, 3);
		this.add(txtObs, 0, 4, 4, 4);
		
		
		//añadimos margenes
		GridPane.setMargin(lblUsuario, new Insets(5, 10, 5, 10));
		GridPane.setMargin(txtUsuario, new Insets(5, 10, 5, 10));
		
		GridPane.setMargin(lblCategoria, new Insets(5, 10, 5, 10));
		GridPane.setMargin(chbCat, new Insets(5, 10, 5, 10));
		
		GridPane.setMargin(lblDescripcion, new Insets(20, 10, 5, 10));
		GridPane.setMargin(txtObs, new Insets(5, 10, 20, 10));
				
	}

	
}
