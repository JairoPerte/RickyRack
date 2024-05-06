package application.database.model;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoDAO {

	public static int crearProducto(Connection con, int categoria, String titulo, String sinopsis, String genero,
			int duracion, String autor, String fecha) {
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"INSERT INTO producto (categoria,titulo,sinopsis,genero,duracion,autor,fecha) VALUE(?,?,?,?,?,?,?)");

			// obtener del paneregistro los datos
			pstmt.setInt(1, categoria);
			pstmt.setString(2, titulo);
			pstmt.setString(3, sinopsis);
			pstmt.setString(4, genero);
			pstmt.setInt(5, duracion);
			pstmt.setString(6, autor);
			pstmt.setString(7, fecha);

			pstmt.executeUpdate();

			PreparedStatement pstmt2 = con.prepareStatement("SELECT idproducto FROM producto where titulo=?");
			pstmt2.setString(1, titulo);
			ResultSet rs = pstmt2.executeQuery();
			rs.next();
			return rs.getInt("idproducto");
		} catch (SQLException e) {
			return -1;
		}
	}

	public static void insertarMultimedia(Connection con, int idproducto, File imagen, String tipo) {
		try {
			PreparedStatement pstmt = con
					.prepareStatement("INSERT INTO multimedia (producto_idproducto,ruta,tipo) VALUE(?,?,?)");

			// obtener del paneregistro los datos
			pstmt.setInt(1, idproducto);
			pstmt.setString(2, imagen.toString());
			pstmt.setString(3, tipo);

			pstmt.executeUpdate();
		} catch (SQLException e) {
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

	public static int usuarioProductoEstrellas(Connection con, int idProducto, int idUsuario) {
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT calificacion FROM calificacion WHERE usuario_idusuario=? and producto_idproducto=?");
			pstmt.setInt(1, idUsuario);
			pstmt.setInt(2, idProducto);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("calificacion");
			} else {
				return 0;
			}
		} catch (SQLException e) {
			return 0;
		}
	}

	public static void insertarCalificacion(Connection con, int idProducto, int idUsuario, int estrellas) {
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"INSERT INTO calificacion (usuario_idusuario,producto_idproducto,calificacion) VALUE(?,?,?)");

			// obtener del paneregistro los datos
			pstmt.setInt(1, idUsuario);
			pstmt.setInt(2, idProducto);
			pstmt.setInt(3, estrellas);

			pstmt.executeUpdate();
		} catch (SQLException e) {
		}
	}

	public static void actualizarCalificacion(Connection con, int idProducto, int idUsuario, int estrellas) {
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"UPDATE calificacion SET calificacion=? WHERE usuario_idusuario=? and producto_idproducto=?");

			pstmt.setInt(1, estrellas);
			pstmt.setInt(2, idUsuario);
			pstmt.setInt(3, idProducto);

			pstmt.executeUpdate();
		} catch (SQLException e) {
		}
	}

	/**
	 * 
	 * @param  con
	 * @param  id
	 * @return
	 */
	public static ResultSet obtenerComentarios(Connection con, int idProducto) {
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT C.idComentario AS idComentario,U.nombre AS usuario, U.imagen AS imagen, U.nivel AS nivel, C.comentario AS comentario, (SELECT COUNT(idlike) FROM `like` L where L.comentario_idcomentario=C.idcomentario and L.dislike=0) AS likes FROM comentario C JOIN usuario U ON U.idusuario=C.usuario_idusuario WHERE producto_idproducto=?");

			pstmt.setInt(1, idProducto);
			return pstmt.executeQuery();
		} catch (SQLException e) {
			return null;
		}
	}

	public static int usuarioComentarioLike(Connection con, int idUsuario, int idComentario) {
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"SELECT dislike FROM `like` WHERE usuario_idusuario=? and comentario_idcomentario=?");
			pstmt.setInt(1, idUsuario);
			pstmt.setInt(2, idComentario);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("dislike");
			} else {
				return -1;
			}
		} catch (SQLException e) {
			return -1;
		}
	}

	public static void escribirComentario(Connection con, int idusuario, int idproducto, String comentario) {
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"INSERT INTO comentario (usuario_idusuario,producto_idproducto,comentario) VALUE(?,?,?)");

			// obtener del paneregistro los datos
			pstmt.setInt(1, idusuario);
			pstmt.setInt(2, idproducto);
			pstmt.setString(3, comentario);

			pstmt.executeUpdate();
		} catch (SQLException e) {
		}
	}

	public static void insertarLike(Connection con, int idusuario, int idcomentario, int dislike) {
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"INSERT INTO `like` (usuario_idusuario,comentario_idcomentario,dislike) VALUE(?,?,?)");

			// obtener del paneregistro los datos
			pstmt.setInt(1, idusuario);
			pstmt.setInt(2, idcomentario);
			pstmt.setInt(3, dislike);

			pstmt.executeUpdate();
		} catch (SQLException e) {
		}
	}

	public static void actualizarLike(Connection con, int idusuario, int idcomentario, int dislike) {
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"UPDATE `like` SET dislike=? WHERE usuario_idusuario=? and comentario_idcomentario=?");

			// obtener del paneregistro los datos
			pstmt.setInt(1, dislike);
			pstmt.setInt(2, idusuario);
			pstmt.setInt(3, idcomentario);

			pstmt.executeUpdate();
		} catch (SQLException e) {
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

	public static ResultSet obtenerProducto(Connection con, int idProducto) {
		try {
			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM producto WHERE idProducto=?");
			pstmt.setInt(1, idProducto);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
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
