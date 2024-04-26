package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class Pruebas extends Application {

	@Override
	public void start(Stage stage) throws FileNotFoundException {

		// El borderpane a mostrar
		BorderPane pnlDistribucion = new BorderPane();

		// ZONA SUPERIOR MENU

		// Barra de menus principal
		MenuBar barraMenu = new MenuBar();

		// Menus que van en la barra de menus
		Menu mSesion = new Menu("Sesión");
		Menu mAyuda = new Menu("Ayuda");

		// Items menu
		MenuItem iRegristro = new MenuItem("Registrarse...");
		MenuItem iIniciar = new MenuItem("Iniciar Sesión...");

		MenuItem iAcercaDe = new MenuItem("Acerca de RickyRack");
		MenuItem iAutores = new MenuItem("Autores");
		MenuItem iAyuda = new MenuItem("Ayuda");

		// Añadimos los menu items
		mSesion.getItems().addAll(iIniciar, iRegristro);
		mAyuda.getItems().addAll(iAcercaDe, iAutores, iAyuda);

		// Añadimos los menu al menubar
		barraMenu.getMenus().addAll(mSesion, mAyuda);

		// Lo posicionamos arriba
		pnlDistribucion.setTop(barraMenu);

		// ZONA CENTRAL

		// panel de pestañas
		TabPane panelPestanas = new TabPane();

		// las tres pestañas
		Tab libros = new Tab("Libros");
		Tab peliculas = new Tab("Peliculas");
		Tab videojuegos = new Tab("Videojuegos");

		// Las añadimos y despues la escribiremos
		panelPestanas.getTabs().addAll(libros, peliculas, videojuegos);

		// Prueba
		Label prueba1 = new Label("Prueba");
		Label prueba2 = new Label("Prueba");

		// Ponemos que no se puedan cerrar
		libros.setClosable(false);
		peliculas.setClosable(false);
		videojuegos.setClosable(false);

		// GRIDS DE LOS PRODUCTOS
		GridPane gridTotal = new GridPane(); // Este sería Videojuego.java o libros o peliculas
		GridPane gP = new GridPane(); // Estos serian los resultados de productos
		GridPane gP2 = new GridPane();
		GridPane gP3 = new GridPane();
		GridPane gP4 = new GridPane();
		GridPane gP5 = new GridPane();
		GridPane gP6 = new GridPane();

		// Aquí esta todo lo de los gridPane por dentro (TEMPORAL)
		gridPane1(gP);
		gridPane1(gP2);
		gridPane1(gP3);
		gridPane1(gP4);
		gridPane1(gP5);
		gridPane1(gP6);

		// Añadimos los grid del rs.next(); al .java
		gridTotal.getChildren().addAll(gP, gP2, gP3, gP4, gP5, gP6);
		// Esto es un borde no os ralleis
		gridTotal.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

		// Les ponemos las posiciones
		GridPane.setConstraints(gP, 0, 0);
		GridPane.setConstraints(gP2, 0, 1);
		GridPane.setConstraints(gP3, 0, 2);
		GridPane.setConstraints(gP4, 0, 3);
		GridPane.setConstraints(gP5, 0, 4);
		GridPane.setConstraints(gP6, 0, 5);
		GridPane.setMargin(gP, new Insets(10, 10, 10, 10));
		GridPane.setMargin(gP2, new Insets(10, 10, 10, 10));
		GridPane.setMargin(gP3, new Insets(10, 10, 10, 10));
		GridPane.setMargin(gP4, new Insets(10, 10, 10, 10));
		GridPane.setMargin(gP5, new Insets(10, 10, 10, 10));
		GridPane.setMargin(gP6, new Insets(10, 10, 10, 10));

		ScrollPane scrollPane = new ScrollPane(gridTotal);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);

		// añadimos a las pestañas
		libros.setContent(scrollPane);
		peliculas.setContent(prueba1);
		videojuegos.setContent(prueba2);

		// Por último lo colocamos en el centro y ya esta
		pnlDistribucion.setCenter(panelPestanas);

		// Ya esto es lo típico
		Scene scene = new Scene(pnlDistribucion, 900, 700);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * @param  gP
	 * @throws FileNotFoundException
	 */
	private void gridPane1(GridPane gP) throws FileNotFoundException {
		GridPane cuerpo = new GridPane(); // sinopsis+titulo+estrella+media_calificacion

		// Datos random
		Label titulo = new Label("The Legend of Zelda: Ocarina of Time");
		Label sinopsis = new Label(
				"The saga of Link and Zelda spans over many generations and hundreds of years. This is an early chapter in the story that tells of how it all began, during the times when the dark lord Ganon was the mortal king of thieves known as Ganondorf. Called upon by unknown forces, Link, a young man living in the ancient forest of Kokiri, discovers that he has been chosen to save the land. He joins with you young princess Zelda in a race to obtain the three magical jewels, which is the key to open the Door of Time, which has guarded the legendary Master Sword for many years. Zelda and Link already have the Ocarina of Time. With it and the Master Sword, the door to the Sacred Realm can be opened. Inside the sacred realm is the Triforce, the legendary relic of all-power. Whoever touches it can have supreme control. If one with a pure soul touches it, the land will enter eternal prosperity. However, if one of evil touches it, the world will be plummeted into eternal darkness and monsters. Traveling ten years into the future as a grown man and back to boyhood continuously, Link must unravel the mysteries of Hyrule and work with the mystic sages to defeat Ganondorf and incarcerate his soul in the void.");
		Image estrella = new Image(new FileInputStream(".\\media\\img\\interfaz\\estrella-relleno.png"));
		ImageView imgEstrella = new ImageView(estrella);
		imgEstrella.setFitHeight(20);
		imgEstrella.setFitWidth(20);
		double calificaionMedia = 10; // valor de ejemplo
		String mediaCal = String.valueOf(calificaionMedia);
		Label media = new Label(mediaCal);
		Image prod = new Image(new FileInputStream(".\\media\\img\\default\\Libro3.jpg"));
		ImageView imgProd = new ImageView(prod);
		imgProd.setFitHeight(100);
		imgProd.setFitWidth(70);

		// al rs.next() añadimos el cuerpo y la imagen
		gP.getChildren().addAll(cuerpo, imgProd);
		cuerpo.getChildren().addAll(titulo, media, sinopsis, imgEstrella);
		cuerpo.autosize(); // creo que no hace nada

		// Lo colocamos cada uno en su sitio he estado 35 minutos
		// con esto pero mas o menos ya lo tengo no creo q cambie

		// Los de gridPane final
		GridPane.setConstraints(imgProd, 0, 0);
		GridPane.setConstraints(cuerpo, 1, 0);

		// Los gridPane de cuerpo
		GridPane.setConstraints(titulo, 0, 0);
		GridPane.setConstraints(imgEstrella, 1, 0);
		GridPane.setConstraints(media, 2, 0);
		GridPane.setConstraints(sinopsis, 0, 1, 3, 1);

		// Para que salga en mas de una linea la sinopsis
		sinopsis.setWrapText(true);

		// Y aqui va lo TEMPORAl esto es lo que es para cambiar
		// MARGENES
		GridPane.setMargin(titulo, new Insets(0, 0, 0, 10));
		GridPane.setMargin(sinopsis, new Insets(15, 0, 0, 10));
		GridPane.setMargin(imgEstrella, new Insets(0, 0, 0, 20));

		// El borde, esto tambien es temporal
		sinopsis.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
		titulo.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
		media.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

		// La anchura puede cambiar ya que el usuario puede estirar
		// la aplicacion pero la altura no puede cambiar, el limite
		// tambien sino os gusta lo modificamos
		gP.setPrefHeight(120);
		gP.setMaxHeight(120);
		gP.setMinHeight(120);

		// El padding para que no quede tan pegado
		gP.setPadding(new Insets(10));

		// Y un borde que este si que me gusta los otros a lo mejor
		// lo quitamos pero este me gusta
		gP.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
	}

	public static void main(String[] args) {
		launch();
	}

}