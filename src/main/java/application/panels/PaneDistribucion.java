package application.panels;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.App;
import application.database.model.UsuarioDAO;
import application.ventana.VentanaCargando;
import application.ventana.VentanaContacto;
import application.ventana.VentanaIniciarSesion;
import application.ventana.VentanaInicioSesion;
import javafx.application.Platform;
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

	public PaneDistribucion(int userLog, Stage stage, Connection con) {
		// Mientras que va cargando
		VentanaCargando carga = new VentanaCargando();

		// ZONA SUPERIOR MENU

		// Barra de menus principal
		MenuBar barraMenu = new MenuBar();

		// Menus que van en la barra de menus
		Menu mSesion = new Menu("Sesión");
		Menu mAyuda = new Menu("Ayuda");
		Menu mConfiguracion = new Menu("Configuración");
		Menu mUsuario = new Menu();

		// Items menu
		if (userLog == DESCONECTADO) {
			MenuItem iRegristro = new MenuItem("Registrarse...");
			MenuItem iIniciar = new MenuItem("Iniciar Sesión...");
			mSesion.getItems().addAll(iIniciar, iRegristro);
			mUsuario = new Menu("(desconectado)");

			iIniciar.setOnAction(event -> {
				new VentanaIniciarSesion(stage, con);
			});
		} else {
			try {
				ResultSet rs = UsuarioDAO.getUsuario(con, userLog);
				rs.next();
				mUsuario = new Menu(rs.getString("nombre"));
			} catch (SQLException e) {
			}
			MenuItem iCerrar = new MenuItem("Cerrar Sesión...");
			mSesion.getItems().addAll(iCerrar);

			iCerrar.setOnAction(event -> {
				// Desconectado por defecto
				App.userLog = DESCONECTADO;
				// Lo cerramos
				stage.close();
				// Mostramos la ventan de inicio de sesión de cookies
				// guardadas (por si quiere iniciar sesión con otra cuenta o
				// no iniciar sesión)
				new VentanaInicioSesion(con);
				// Para que cargen de nuevo los cambios
				Scene escenaNueva = new Scene(new PaneDistribucion(App.userLog, stage, con), 900, 700);
				escenaNueva.getStylesheets().add(getClass().getResource("/estilos/application.css").toExternalForm());
				stage.setScene(escenaNueva);
				stage.show();
			});

			MenuItem iCambiarImg = new MenuItem("Cambiar Imagen");
			MenuItem iCambiarPassword = new MenuItem("Cambiar Contraseña");
			MenuItem iEliminar = new MenuItem("Eliminar Cuenta");

			mConfiguracion.getItems().addAll(iCambiarImg, iCambiarPassword, iEliminar);
		}

		mUsuario.setId("menuUser");

		MenuItem iSalir = new MenuItem("Salir de la Aplicacion");
		mConfiguracion.getItems().addAll(iSalir);

		MenuItem iAcercaDe = new MenuItem("Acerca de RickyRack");
		MenuItem iAutores = new MenuItem("Autores");
		MenuItem iContacto = new MenuItem("Contacta con nosotros");
		MenuItem iAyuda = new MenuItem("Ayuda");

		// Añadimos los menu items
		mAyuda.getItems().addAll(iAcercaDe, iContacto, iAutores, iAyuda);

		// Añadimos los menu al menubar
		barraMenu.getMenus().addAll(mSesion, mAyuda, mConfiguracion, mUsuario);

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
		ScrollPane libros = new Productos(userLog, LIBROS, con);
		ScrollPane peliculas = new Productos(userLog, PELICULAS, con);
		ScrollPane videojuegos = new Productos(userLog, VIDEOJUEGOS, con);

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
		iSalir.setOnAction(event -> {
			stage.close();
		});

		// Cuando ya ha cargado es cuando la pestaña termina
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
