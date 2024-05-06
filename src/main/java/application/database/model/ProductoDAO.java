package application.database.model;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoDAO {

	/**
	 * Crea un producto con todos sus datos
	 * 
	 * @param  con       conexión a la base de datos
	 * @param  categoria categoria del producto
	 *                   (libro,pelicula,videojuego)
	 * @param  titulo    titulo del producto
	 * @param  sinopsis  sinopsis del producto
	 * @param  genero    genero del producto
	 * @param  duracion  duracion del producto
	 * @param  autor     autor del producto
	 * @param  fecha     fecha de creación
	 * @return           devuelve el id del producto creado para
	 *                   luego ponerles las multimedias si ha
	 *                   insertado alguna
	 */
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
			ResultSet rs = obtenerProducto(con, titulo);
			rs.next();
			return rs.getInt("idproducto");
		} catch (SQLException e) {
			return -1;
		}
	}

	/**
	 * Inserta un archivo de un producto en la base de dato
	 * 
	 * @param con        conexión a la Base de Datos
	 * @param idproducto id del producto a insertar multimedia
	 * @param imagen     archivo de la imagen
	 * @param tipo       tipo de la imagen P es de la portada o
	 *                   I sino
	 */
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
	 * Obtine la media de un producto con un decimal
	 * 
	 * @param  con conexión a la BD
	 * @param  id  id del producto a hacer la media
	 * @return     devuelve la media con solo un decimal de un
	 *             producto
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
	 * Obtiene las estrellas que ha insertado un usuario a un
	 * producto, esta función solo se llama para cuando el
	 * usuario clicka en el producto mostrarles las estrellas
	 * que ya tenias insertadas y también para saber si cuando
	 * el usuario ponga una calificación si el usuario tiene que
	 * actualizar la que tenia o insertar una totalmente nueva
	 * sino tenía
	 * 
	 * @param  con        conexión a la base de datos
	 * @param  idProducto id del producto a comprobar
	 * @param  idUsuario  id del usuario a comprobar
	 * @return            devuelve 0 si no ha insertado nada
	 *                    sino devuelve la cantidad insertada
	 */
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

	/**
	 * Inserta una calificación del usuario a un producto
	 * 
	 * @param con        conexión a la BD
	 * @param idProducto id del producto a insertar la cal
	 * @param idUsuario  id del usuario a insertar la cal
	 * @param estrellas  numero de estrellas a insertar (1-5)
	 */
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

	/**
	 * Actualiza una calificación del usuario a un producto, que
	 * ya está puesta
	 * 
	 * @param con        conexión a la BD
	 * @param idProducto id del producto a insertar la cal
	 * @param idUsuario  id del usuario a insertar la cal
	 * @param estrellas  numero de estrellas a insertar (1-5)
	 */
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
	 * Obtiene todos los comentarios + los datos del usuario que
	 * lo escrió para poder mostrarlas en el producto. Además
	 * devuelve el número de likes del comentario (solo cuenta
	 * la cantidad de likes sin los dislikes);
	 * 
	 * @param  con conexión a la BD
	 * @param  id  id del producto a sacar todos los comentarios
	 * @return     devuelve el ResultSet con todos los datos de
	 *             los comentarios
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

	/**
	 * Inserta un nuevo comentario a un producto de un
	 * determinado usuario
	 * 
	 * @param con        conexión a la BD
	 * @param idusuario  id del usuario
	 * @param idproducto id del producto
	 * @param comentario texto del comentario
	 */
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

	/**
	 * Obtiene el tipo de like (0 si es like 1 si es dislike) o
	 * -1 si no ha insertado nada pues devuelve -1 para saber si
	 * tiene que insertar o actualizar
	 * 
	 * @param  con
	 * @param  idUsuario
	 * @param  idComentario
	 * @return
	 */
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

	/**
	 * Inserta un like si el usuario no ha dado like
	 * anteriormente
	 * 
	 * @param con          conexión a la BD
	 * @param idusuario    id del usuario a insertarle el like
	 * @param idcomentario id del comentario a insertar el like
	 * @param dislike      para saber si insertar 0 like o -1
	 *                     dislike
	 */
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

	/**
	 * Actualiza el like si el usuario ya le había dado like
	 * anteriormente
	 * 
	 * @param con
	 * @param idusuario
	 * @param idcomentario
	 * @param dislike
	 */
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
	 * Obtiene todos los productos de una determinada categoria
	 * (0,1,2) (Libros,Películas,Videojuegos)
	 * 
	 * @param  con       conexión a la BD
	 * @param  categoria categoria a buscar
	 * @return           el ResultSet con todos los datos de los
	 *                   productos que están en esa categoría
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
	 * Obtiene el producto a través del id
	 * 
	 * @param  con        conexión a la BD
	 * @param  idProducto id del producto a seleccionar
	 * @return
	 */
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
	 * Obtiene el id producto a través del titulo ya que es
	 * único
	 * 
	 * @param  con    conexión a la base de datos
	 * @param  titulo titulo a buscar
	 * @return        devuelve un resultset que contiene el id
	 *                del producto cuyo titulo es el indicado
	 */
	public static ResultSet obtenerProducto(Connection con, String titulo) {
		try {
			PreparedStatement pstmt = con.prepareStatement("SELECT idproducto FROM producto WHERE titulo=?");
			pstmt.setString(1, titulo);
			ResultSet rs = pstmt.executeQuery();
			return rs;
		} catch (SQLException e) {
			return null;
		}
	}

	/**
	 * Obtiene la multimedia de un determinado producto
	 * 
	 * @param  con conexión a la base de datos
	 * @param  id  id del producto a que pertenece la
	 *             mmultimedia
	 * @return     devuelve un resultset con la ruta y el tipo
	 *             de todas las multimedias del producto
	 */
	public static ResultSet obtenerMultiMedia(Connection con, int id) {
		try {
			PreparedStatement pstmt = con
					.prepareStatement("SELECT ruta, tipo FROM multimedia WHERE producto_idproducto=?");
			pstmt.setInt(1, id);
			return pstmt.executeQuery();
		} catch (SQLException e) {
			return null;
		}
	}
}
