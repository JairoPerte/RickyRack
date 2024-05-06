package application.ventana;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class VentanaAyuda extends Stage {

	public VentanaAyuda() {
		Label contacta = new Label("Para más ayuda dirigete a nuestra páigna web");
		contacta.setAlignment(Pos.CENTER);
		this.setTitle("Ayuda");
		this.setScene(new Scene(contacta, 100, 80));
		this.show();
	}

}
