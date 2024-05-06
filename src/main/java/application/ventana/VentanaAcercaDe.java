package application.ventana;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VentanaAcercaDe extends Stage {

	public VentanaAcercaDe() {

		VBox vbox = new VBox(10);
		Label realizado = new Label(
				"Trabajo Reaizado por Jairo Pertegal Carrasco, Rubén Hidalgo García y Mónica Marroquín Ortiz, este trabajo se ha realizado para el proyectoFX de 1ºDAW 2024/2025");
		Label libreria = new Label(
				"La librería que hemos escogido para utilizar en nuestro trabajo es: 'Apache Commons IO': página web del repositorio 'https://mvnrepository.com/artifact/commons-io/commons-io'");
		Label descripcion = new Label(
				" DESCRIPCIÓN DEL PROYECTO Nuestro proyecto consisten en una red social donde te puedes crear una cuenta e ir subiendo de nivel, en esta red social puedes calificar muchas cosas como libros, peliculas y videojuegos");

		Label antesde = new Label(
				"ANTES DE EMPEZAR Deberías descargarte la base de datos para que funcione la aplicación, la BD está adjuntada en la tarea del classroom");
		vbox.getChildren().addAll(realizado, libreria, descripcion, antesde);
	}
}
