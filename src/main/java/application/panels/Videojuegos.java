package application.panels;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.database.model.ProductoDAO;
import application.database.utils.UtilsBD;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

				gP.setMaxSize(300, 300);
				gP.setMinSize(300, 300);
				gP.setPadding(new Insets(10));

				// Buscamos el título
				Label titulo = new Label(rs.getString("titulo"));

				// Obtenemos la media
				double mediaCal = ProductoDAO.calificacionMedia(con, rs.getInt("idproducto"));

				try {
					Image imgEstrella = new Image(new FileInputStream(""));
					ImageView imgVEstrella = new ImageView(imgEstrella);
				} catch (FileNotFoundException e) {
					// throws FaltaInterfaz
				}

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
				while (!tieneFoto && multimedias.next()) {
					if (multimedias.getString("tipo").equals("I")) {
						tieneFoto = true;
					}
				}

				// Si no existe el ficchero/no se encuentra
				try {
					Image imgProd = new Image(new FileInputStream(multimedias.getString("ruta")));
					ImageView imgVProd = new ImageView(imgProd);
				} catch (FileNotFoundException e) {
					Label img = new Label("No hay imagen para este artículo");
				}
			}
		} catch (SQLException e) {
			// Throws ConexionFallida
		}
	}
}
