package application.database.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoDAO {

	/**
	 * 
	 * @param  con
	 * @param  id
	 * @return
	 */
	public ResultSet obtenerComentarios(Connection con, int id) {
		PreparedStatement pstmt;
		try {
			pstmt = con.prepareStatement(
					"SELECT nombre,imagen,likes,comentario JOIN para el nombre y numero de imagen WHERE id=id");
			ResultSet rs = pstmt.executeQuery();
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param  con
	 * @param  id
	 * @return
	 */
	public double calificacionMedia(Connection con, int id) {
		double media = 0;
		return media;
	}

	public ResultSet obtenerProductos(Connection con, int categoria) {
		try {
			String query = "SELECT * FROM producto where categoria=?";
			PreparedStatement pstmt = con.prepareStatement(query);

			pstmt.setInt(0, categoria);

			return pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
