package application.panels;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.database.model.ProductoDAO;
import application.database.utils.UtilsBD;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class Videojuegos extends GridPane {

	private static final int VIDEOJUEGO = 2;

	public ArrayList<Integer> listaReferencia;

	public Videojuegos() {
		try {
			Connection con = UtilsBD.conectarBD();
			ResultSet rs = ProductoDAO.obtenerProductos(con, VIDEOJUEGO);

			// Por cada videojuego encontrado
			while (rs.next()) {

				// Lo guardamos en la lista de Referencia
				listaReferencia.add(rs.getInt("idproducto"));

				// Lo guardaremos en nuestro gridPane para cada videojuego
				GridPane gP = new GridPane();

				// Buscamos el título
				Label titulo = new Label(rs.getString("titulo"));

				// Obtenemos la media
				double media = ProductoDAO.calificacionMedia(con, rs.getInt("idproducto"));

				// Si es sinopsis larga la acortamos
				try {
					Label sinopsis = new Label(rs.getString("sinopsis").substring(0, 70) + "...");
				} catch (StringIndexOutOfBoundsException e) {
					Label sinopsis = new Label(rs.getString("sinopsis"));
				}

				// Buscamos toda la multimedia relacionada con ese producto
				ResultSet multimedias = ProductoDAO.obtenerMultiMedia(con, rs.getInt("idproducto"));
				boolean tieneFoto = false;

				// Buscamos imagen para ponerla
				while (multimedias.next() && !tieneFoto) {
					if (multimedias.getString("tipo").equals("I")) {
						tieneFoto = true;
					}
				}

				// Si no existe el ficchero/no se encuentra
				try {
					FileReader img = new FileReader(new File(multimedias.getString("ruta")));
				} catch (FileNotFoundException e) {
					Label img = new Label("No hay imagen para este artículo");
				}
			}
		} catch (SQLException e) {

		}
	}
}
