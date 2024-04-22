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
	public static ResultSet obtenerComentarios(Connection con, int id) {
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT usuario.nombre AS usuario, usuario.imagen AS imagen, usuario.nivel AS nivel, comentario.comentario AS comentario, comentario.likes FROM comentario JOIN usuario ON usuario.idusuario=comentario.usuario_idusuario WHERE producto_idproducto=?");

			pstmt.setInt(0, id);

			ResultSet rs = pstmt.executeQuery();
			return rs;
		} catch (SQLException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param  con
	 * @param  id
	 * @return
	 */
	public static double calificacionMedia(Connection con, int id) {
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT TRUNCATE(1,AVG(calificacion)) AS media FROM calificacion WHERE producto_idproducto=?");

			pstmt.setInt(0, id);

			return pstmt.executeQuery().getDouble("media");
		} catch (SQLException e) {
			return -1;
		}
	}

	/**
	 * 
	 * @param  con
	 * @param  categoria
	 * @return
	 */
	public static ResultSet obtenerProductos(Connection con, int categoria) {
		try {
			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM producto WHERE categoria=?");

			pstmt.setInt(0, categoria);

			return pstmt.executeQuery();
		} catch (SQLException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param  con
	 * @param  id
	 * @return
	 */
	public static ResultSet obtenerMedia(Connection con, int id) {
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT multimedia.ruta AS ruta, multimedia.tipo AS tipo FROM multimedia JOIN producto ON multimedia.producto_idproducto=producto.idproducto WHERE producto.idproducto=?");

			pstmt.setInt(0, id);

			return pstmt.executeQuery();
		} catch (SQLException e) {
			return null;
		}
	}
}
