package application.panels;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.database.model.ProductoDAO;
import application.database.utils.UtilsBD;
import javafx.scene.layout.GridPane;

public class Videojuegos extends GridPane {

	private static final int VIDEOJUEGO = 2;

	public ArrayList<Integer> listaReferencia;

	public Videojuegos() {
		try {
			Connection con = UtilsBD.conectarBD();
			ResultSet rs = ProductoDAO.obtenerProductos(con, VIDEOJUEGO);

			// Por cada uno
			while (rs.next()) {
				GridPane gP = new GridPane();

			}
		} catch (SQLException e) {

		} catch (FileNotFoundException e) {

		}
	}
}
