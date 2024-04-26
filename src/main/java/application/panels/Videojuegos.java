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

	public Videojuegos(boolean userConectado) {
		try {
			Connection con = UtilsBD.conectarBD();
			ResultSet rs = ProductoDAO.obtenerProductos(con, VIDEOJUEGO);

			// Por cada videojuego encontrado
			while (rs.next()) {

				// Lo guardamos en la lista de Referencia
				listaReferencia.add(rs.getInt("idproducto"));

				// Lo guardaremos en nuestro gridPane para cada videojuego
				GridPane gP = new GridPane();

				gP.setMaxSize(300, 150);
				gP.setMinSize(300, 150);
				gP.setPadding(new Insets(10));

				// Buscamos el título
				Label titulo = new Label(rs.getString("titulo"));

				// Obtenemos la media
				double mediaCalificacion = ProductoDAO.calificacionMedia(con, rs.getInt("idproducto"));

				try {
					Image estrella = new Image(new FileInputStream(".\\media\\img\\interfaz\\estrella-relleno.png"));
					ImageView imgEstrella = new ImageView(estrella);
				} catch (FileNotFoundException e) {
					// throws FaltaInterfaz
				}

				// Si es sinopsis larga la acortamos con (...)
				Label sinopsis = new Label(rs.getString("sinopsis"));

				// Buscamos toda la multimedia relacionada con ese producto
				ResultSet multimedias = ProductoDAO.obtenerMultiMedia(con, rs.getInt("idproducto"));
				boolean tieneFoto = false;

				// Buscamos imagen para ponerla
				while (!tieneFoto && multimedias.next()) {
					if (multimedias.getString("tipo").equals("I")) {
						tieneFoto = true;
					}
				}

				// Si no existe el ficchero/no se encuentra (no existe para
				// ese artículo)
				try {
					Image prod = new Image(new FileInputStream(multimedias.getString("ruta")));
					ImageView imgProd = new ImageView(prod);
				} catch (FileNotFoundException e) {
					Label imgProd = new Label("No hay imagen para este artículo");
				}
			}

			// Aquí el codigo de añadir un nuevo "artículo" que cuando
			// clickes sea para dejar al usuario añadir
			if (userConectado) {

				// Lo guardaremos en nuestro gridPane
				GridPane gP = new GridPane();

				gP.setMaxSize(300, 150);
				gP.setMinSize(300, 150);
				gP.setPadding(new Insets(10));

				try {
					Image botonAdd = new Image(new FileInputStream(".\\media\\img\\interfaz\\boton-add.png"));
					ImageView imgBotonAdd = new ImageView(botonAdd);
				} catch (FileNotFoundException e) {
					// throws FaltaInterfaz
				}

				Label titulo = new Label("Haz click aquí para añadir un nuevo artículo...");

			}
		} catch (SQLException e) {
			// Throws ConexionFallida
		}
	}
}
