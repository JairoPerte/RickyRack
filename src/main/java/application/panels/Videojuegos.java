package application.panels;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.database.model.ProductoDAO;
import application.database.utils.UtilsBD;
import javafx.scene.layout.GridPane;

public class Videojuegos extends GridPane {

	private static final int VIDEOJUEGO = 2;

	public Videojuegos() {
		try {
			Connection con = UtilsBD.conectarBD();
			ResultSet rs = ProductoDAO.obtenerProductos(con, VIDEOJUEGO);
			while (rs.next()) {

			}
		} catch (SQLException e) {

		}
	}
}
