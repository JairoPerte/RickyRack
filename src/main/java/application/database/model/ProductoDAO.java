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
					"SELECT U.nombre AS usuario, U.imagen AS imagen, U.nivel AS nivel, C.comentario AS comentario, (SELECT COUNT(idlike) FROM `like` L where L.comentario_idcomentario=C.idcomentario and L.dislike=0) AS likes FROM comentario C JOIN usuario U ON U.idusuario=C.usuario_idusuario WHERE producto_idproducto=?");

			pstmt.setInt(1, id);
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
	public static double calificacionMedia(Connection con, int id) {
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT TRUNCATE(AVG(calificacion),1) AS media FROM calificacion WHERE producto_idproducto=?");
			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();
			rs.next();
			return rs.getDouble("media");
		} catch (SQLException e) {
			return 0;
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
			pstmt.setInt(1, categoria);
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
	public static ResultSet obtenerMultiMedia(Connection con, int id) {
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT multimedia.ruta AS ruta, multimedia.tipo AS tipo FROM multimedia JOIN producto ON multimedia.producto_idproducto=producto.idproducto WHERE producto.idproducto=?");
			pstmt.setInt(1, id);
			return pstmt.executeQuery();
		} catch (SQLException e) {
			return null;
		}
	}
}
