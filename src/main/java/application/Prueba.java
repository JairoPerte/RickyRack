package application;

import java.sql.Connection;

import application.database.model.ProductoDAO;
import application.database.utils.UtilsBD;

public class Prueba {

	public static void main(String[] args) {
		Connection con = UtilsBD.conectarBD();
		double media = ProductoDAO.calificacionMedia(con, 15);
		System.out.println(media);
		System.out.println(String.valueOf(-1));
	}
}
