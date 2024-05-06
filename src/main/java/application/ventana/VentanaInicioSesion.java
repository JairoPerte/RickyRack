package application.ventana;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.App;
import application.cookies.CookieWriter;
import application.database.model.UsuarioDAO;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaInicioSesion extends Stage {

	private static final int FEMALE_1 = 1;
	private static final int FEMALE_2 = 2;
	private static final int MALE_1 = 3;
	private static final int MALE_2 = 4;

	public VentanaInicioSesion(Connection con) {
		ScrollPane paneScroll = new ScrollPane();
		VBox usuariosIniciados = new VBox();
		// Lo añadimos
		paneScroll.setContent(usuariosIniciados);

		// Para que sea responsiva
		paneScroll.setFitToWidth(true);
		paneScroll.setFitToHeight(true);

		// Sacar lista de id que coincidan cookies
		ArrayList<Integer> idsIniciados = CookieWriter.comprobarCookies(con);

		if (idsIniciados.get(0) == -1) {
			return;
		}

		try {
			for (int i = 0; i < idsIniciados.size(); i++) {
				GridPane usuario = new GridPane();
				usuario.setId("usuario");

				int id = idsIniciados.get(i);

				ResultSet rs = UsuarioDAO.getUsuario(con, id);
				rs.next();

				Label nombre = new Label(rs.getString("nombre"));
				Label nivel = new Label("Nivel " + rs.getInt("nivel"));
				nombre.setId("nombre");
				nivel.setId("nivel");

				// Ponemos la imagen
				switch (rs.getInt("imagen")) {
				case FEMALE_1:
					ImageView imgF1 = new ImageView(
							new Image(new FileInputStream(".\\media\\img\\interfaz\\defaultfemale1.png")));

					// Forzamos la imagen
					imgF1.setFitHeight(60);
					imgF1.setFitWidth(60);

					// La añadimos
					usuario.getChildren().add(imgF1);

					// Lo colocamos en su sitio
					GridPane.setConstraints(imgF1, 0, 0, 2, 1);
					break;
				case FEMALE_2:
					ImageView imgF2 = new ImageView(
							new Image(new FileInputStream(".\\media\\img\\interfaz\\defaultfemale2.png")));

					// Forzamos la imagen
					imgF2.setFitHeight(60);
					imgF2.setFitWidth(60);

					// La añadimos
					usuario.getChildren().add(imgF2);

					// Lo colocamos en su sitio
					GridPane.setConstraints(imgF2, 0, 0, 2, 1);
					break;
				case MALE_1:
					ImageView imgM1 = new ImageView(
							new Image(new FileInputStream(".\\media\\img\\interfaz\\defaultmale1.png")));

					// Forzamos la imagen
					imgM1.setFitHeight(60);
					imgM1.setFitWidth(60);

					// La añadimos
					usuario.getChildren().add(imgM1);

					// Lo colocamos en su sitio
					GridPane.setConstraints(imgM1, 0, 0, 2, 1);
					break;
				case MALE_2:
					ImageView imgM2 = new ImageView(
							new Image(new FileInputStream(".\\media\\img\\interfaz\\defaultmale2.png")));

					// Forzamos la imagen
					imgM2.setFitHeight(60);
					imgM2.setFitWidth(60);

					// La añadimos
					usuario.getChildren().add(imgM2);

					// Lo colocamos en su sitio
					GridPane.setConstraints(imgM2, 0, 0, 2, 1);
				}

				usuario.setOnMouseClicked(e -> {
					App.userLog = id;
					this.close();
				});

				usuario.setOnMouseEntered(event -> {
					this.getScene().setCursor(Cursor.HAND);
					usuario.setId("usuario-hover");
					nombre.setId("nombre-hover");
					nivel.setId("nivel-hover");
				});

				usuario.setOnMouseExited(event -> {
					this.getScene().setCursor(Cursor.DEFAULT);
					usuario.setId("usuario");
					nombre.setId("nombre");
					nivel.setId("nivel");
				});

				// La añadimos
				usuario.getChildren().addAll(nombre, nivel);

				// Lo colocamos en su sitio
				GridPane.setConstraints(nombre, 0, 1);
				GridPane.setConstraints(nivel, 1, 1);
				GridPane.setMargin(nombre, new Insets(10, 10, 10, 10));
				GridPane.setMargin(nivel, new Insets(10, 10, 10, 10));

				usuario.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
						new BorderWidths(2), new Insets(2))));

				usuariosIniciados.getChildren().add(usuario);
			}

			this.getIcons().add(new Image(new FileInputStream(".\\media\\img\\interfaz\\default-user-icon.png")));
			Scene escena = new Scene(paneScroll, 220, 300);
			escena.getStylesheets().add(getClass().getResource("/estilos/iniciosesion.css").toExternalForm());
			this.setTitle("Entrar");
			this.initModality(Modality.WINDOW_MODAL);
			this.setScene(escena);

			this.showAndWait();
		} catch (SQLException e) {
		} catch (FileNotFoundException e) {
			// throws FaltaInterfaz
		}
	}
}
