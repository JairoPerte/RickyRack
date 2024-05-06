package application.panels;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.App;
import application.cookies.CookieWriter;
import application.cookies.Hash;
import application.database.model.UsuarioDAO;
import application.ventana.VentanaAcercaDe;
import application.ventana.VentanaAutores;
import application.ventana.VentanaAyuda;
import application.ventana.VentanaCambiarContrasena;
import application.ventana.VentanaCambiarImg;
import application.ventana.VentanaCargando;
import application.ventana.VentanaContacto;
import application.ventana.VentanaEliminarCuenta;
import application.ventana.VentanaIniciarSesion;
import application.ventana.VentanaRegistro;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
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
		Productos libros = new Productos(userLog, LIBROS, con, stage);
		Productos peliculas = new Productos(userLog, PELICULAS, con, stage);
		Productos videojuegos = new Productos(userLog, VIDEOJUEGOS, con, stage);

		// Lo asignamos a las pestañas
		tabLibros.setContent(libros);
		tabPeliculas.setContent(peliculas);
		tabVideojuegos.setContent(videojuegos);

		// Lo posicionamos en el centro
		this.setCenter(panelPestanas);

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
			MenuItem iRegistro = new MenuItem("Registrarse...");
			MenuItem iIniciar = new MenuItem("Iniciar Sesión...");
			mSesion.getItems().addAll(iIniciar, iRegistro);
			mUsuario = new Menu("(desconectado)");

			iIniciar.setOnAction(event -> {
				new VentanaIniciarSesion(stage, con);
			});
			iRegistro.setOnAction(event -> {
				new VentanaRegistro(stage, con);
			});
		} else {
			try {
				ResultSet rs = UsuarioDAO.getUsuario(con, userLog);
				rs.next();
				mUsuario = new Menu(rs.getString("nombre"));
			} catch (SQLException e) {
			}
			MenuItem iCerrar = new MenuItem("Cerrar Sesión...");
			MenuItem iEliminarSesion = new MenuItem("Eliminar Sesión...");
			mSesion.getItems().addAll(iCerrar);

			iCerrar.setOnAction(event -> {
				// Desconectado por defecto
				App.userLog = DESCONECTADO;
				// Creamos el PaneDistribucion capado
				BorderPane nuevoPaneDistribution = new PaneDistribucion(App.userLog, stage, con);
				// Obtenemos la escena
				Scene escenaNueva = stage.getScene();
				escenaNueva.setRoot(nuevoPaneDistribution);
			});

			iEliminarSesion.setOnAction(event -> {
				Hash.quitarHash(con, userLog);
				CookieWriter.eliminarCookie(userLog);
			});

			MenuItem iCambiarImg = new MenuItem("Cambiar Imagen");
			MenuItem iCambiarPassword = new MenuItem("Cambiar Contraseña");
			MenuItem iEliminar = new MenuItem("Eliminar Cuenta");

			iCambiarPassword.setOnAction(event -> {
				new VentanaCambiarContrasena(con, stage, userLog);
			});

			iCambiarImg.setOnAction(event -> {
				new VentanaCambiarImg(con, userLog, stage);
			});

			iEliminar.setOnAction(event -> {
				new VentanaEliminarCuenta(con, stage, userLog);
			});

			mConfiguracion.getItems().addAll(iCambiarImg, iCambiarPassword, iEliminar);
		}

		mUsuario.setId("menuUser");

		MenuItem iSalir = new MenuItem("Salir de la Aplicacion");
		mConfiguracion.getItems().addAll(iSalir);

		MenuItem iAcercaDe = new MenuItem("Acerca de RickyRack");
		MenuItem iAutores = new MenuItem("Autores");
		MenuItem iContacto = new MenuItem("Contacta con nosotros");
		MenuItem iAyuda = new MenuItem("Ayuda");

		// Eventos

		// Cuando pulsamos en la opcion de menu salir cerramos la
		// app
		iSalir.setOnAction(event -> {
			stage.close();
		});

		iContacto.setOnAction(event -> {
			new VentanaContacto(stage, con);
		});

		iAutores.setOnAction(event -> {
			new VentanaAutores();
		});

		iAyuda.setOnAction(event -> {
			new VentanaAyuda();
		});

		iAcercaDe.setOnAction(event -> {
			new VentanaAcercaDe();
		});

		// Añadimos los menu items
		mAyuda.getItems().addAll(iAcercaDe, iContacto, iAutores, iAyuda);

		// Añadimos los menu al menubar
		barraMenu.getMenus().addAll(mSesion, mAyuda, mConfiguracion, mUsuario);

		// Lo posicionamos arriba
		this.setTop(barraMenu);

		// Cuando ya ha cargado es cuando la pestaña termina
		Platform.runLater(() -> carga.close());
	}

}
