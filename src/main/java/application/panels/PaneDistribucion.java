package application.panels;

import java.sql.Connection;

import application.ventana.VentanaCargando;
import application.ventana.VentanaContacto;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PaneDistribucion extends BorderPane {

	// CATEGORIAS
	private static final int LIBROS = 0;
	private static final int PELICULAS = 1;
	private static final int VIDEOJUEGOS = 2;

	private static final int DESCONECTADO = -1;

	public PaneDistribucion(int userConectado, Stage stage, Connection con) {
		// Mientras que va cargando
		VentanaCargando carga = new VentanaCargando();

		// ZONA SUPERIOR MENU

		// Barra de menus principal
		MenuBar barraMenu = new MenuBar();

		// Menus que van en la barra de menus
		Menu mSesion = new Menu("Sesión");
		Menu mAyuda = new Menu("Ayuda");
		Menu mConfiguracion = new Menu("Configuración");

		// Items menu
		if (userConectado == DESCONECTADO) {
			MenuItem iRegristro = new MenuItem("Registrarse...");
			MenuItem iIniciar = new MenuItem("Iniciar Sesión...");
			mSesion.getItems().addAll(iIniciar, iRegristro);
		} else {
			MenuItem iCerrar = new MenuItem("Cerrar Sesión...");
			mSesion.getItems().addAll(iCerrar);

			MenuItem iCambiarImg = new MenuItem("Cambiar Imagen");
			MenuItem iCambiarPassword = new MenuItem("Cambiar Contraseña");
			MenuItem iEliminar = new MenuItem("Eliminar Cuenta");

			mConfiguracion.getItems().addAll(iCambiarImg, iCambiarPassword, iEliminar);
		}

		MenuItem iSalir = new MenuItem("Salir de la Aplicacion");
		mConfiguracion.getItems().addAll(iSalir);

		MenuItem iAcercaDe = new MenuItem("Acerca de RickyRack");
		MenuItem iAutores = new MenuItem("Autores");
		MenuItem iContacto = new MenuItem("Contacta con nosotros");
		MenuItem iAyuda = new MenuItem("Ayuda");

		// Añadimos los menu items
		mAyuda.getItems().addAll(iAcercaDe, iContacto, iAutores, iAyuda);

		// Añadimos los menu al menubar
		barraMenu.getMenus().addAll(mSesion, mAyuda, mConfiguracion);

		// Lo posicionamos arriba
		this.setTop(barraMenu);

		// ZONA CENTRAL PESTAÑAS

		// Panel de pestañas
		TabPane panelPestanas = new TabPane();

		// las tres pestañas
		Tab tabLibros = new Tab("Libros");
		Tab tabPeliculas = new Tab("Peliculas");
		Tab tabVideojuegos = new Tab("Videojuegos");

		// Las añadimos y despues la escribiremos
		panelPestanas.getTabs().addAll(tabLibros, tabPeliculas, tabVideojuegos);

		// Ponemos que no se puedan cerrar
		tabLibros.setClosable(false);
		tabPeliculas.setClosable(false);
		tabVideojuegos.setClosable(false);

		// Creamos todos los scrollPane que necesitamos
		ScrollPane libros = new Productos(userConectado, LIBROS, con);
		ScrollPane peliculas = new Productos(userConectado, PELICULAS, con);
		ScrollPane videojuegos = new Productos(userConectado, VIDEOJUEGOS, con);

		// Lo asignamos a las pestañas
		tabLibros.setContent(libros);
		tabPeliculas.setContent(peliculas);
		tabVideojuegos.setContent(videojuegos);

		// Lo posicionamos en el centro
		this.setCenter(panelPestanas);

		// Eventos

		iContacto.setOnAction(event -> {
			abrirFormularioContacto();
		});

		// Cuando pulsamos en la opcion de menu salir cerramos la
		// app
		iSalir.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stage.close();
			}
		});

		// Cuando ya ha cargado
		Platform.runLater(() -> carga.close());
	}

	private void abrirFormularioContacto() {

		Stage formularioStage = new Stage();
		VentanaContacto testInforma = new VentanaContacto();
		Scene testScene = testInforma.createScene();

		// formulario de contacto
		VBox vbox = new VBox();
		vbox.getChildren().addAll(testScene.getRoot());
		formularioStage.initModality(Modality.WINDOW_MODAL);

		Scene scene = new Scene(vbox);
		formularioStage.setScene(scene);
		formularioStage.showAndWait();
	}

}
