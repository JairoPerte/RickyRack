package application.database.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.cookies.Hash;
import application.exceptions.UsuarioExistente;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class UsuarioDAO {

	private static final int ERROR_SQL = -2;
	private static final int USUARIO_EXISTENTE = -1;

	/**
	 * Crea un usuario con los datos introducidos
	 * 
	 * @param  con           conexión a la Base de Datos
	 * @param  nombre        nombre del user
	 * @param  password      contraseña del user
	 * @param  img           numero de la imagen del user
	 * @param  guardarSesion si quiere crear el hash o no
	 * @return               el id de la sesión (del usuario
	 *                       creado)
	 */
	public static int crearUsuario(Connection con, String nombre, String password, int img, boolean guardarSesion) {
		try {
			if (!getUsuario(con, nombre).next()) {
				PreparedStatement pstmt = con.prepareStatement(
						"INSERT INTO usuario (nombre,password,imagen,experiencia,nivel) VALUE(?,?,?,0,1)");

				// obtener del paneregistro los datos
				pstmt.setString(1, nombre);
				pstmt.setString(2, password);
				pstmt.setInt(3, img);

				pstmt.executeUpdate();

				PreparedStatement pstmtId = con.prepareStatement("SELECT idusuario FROM usuario Where nombre=?");
				pstmtId.setString(1, nombre);

				ResultSet rs = pstmtId.executeQuery();
				rs.next();

				// Si selecciona guardar sesión
				if (guardarSesion) {
					Hash.crearHash(con, rs.getInt("idusuario"));
				}

				return rs.getInt("idusuario");
			} else {
				new UsuarioExistente(AlertType.ERROR);
				return USUARIO_EXISTENTE;
			}
		} catch (SQLException e) {
			return ERROR_SQL;
		}
	}

	/**
	 * Obtiene el usuario con un determinado id
	 * 
	 * @param  con conexión a la BD
	 * @param  id  id del usuario
	 * @return     el result set con todos los datos del usuario
	 *             menos contraseña y hash
	 */
	public static ResultSet getUsuario(Connection con, int id) {
		try {
			PreparedStatement pstmt = con
					.prepareStatement("SELECT nombre,imagen,nivel,experiencia FROM usuario WHERE idusuario=?");
			pstmt.setInt(1, id);
			return pstmt.executeQuery();
		} catch (SQLException sqle) {
			return null;
		}
	}

	/**
	 * Obtiene el usuario con un determinado nombre del user
	 * 
	 * @param  con    conexión a la BD
	 * @param  nombre nombre del usuario
	 * @return        el result set con todos los datos del
	 *                usuario menos contraseña y hash
	 */
	public static ResultSet getUsuario(Connection con, String nombre) {
		try {
			PreparedStatement pstmt = con
					.prepareStatement("SELECT idusuario,imagen,nivel,experiencia FROM usuario WHERE nombre=?");
			pstmt.setString(1, nombre);
			return pstmt.executeQuery();
		} catch (SQLException sqle) {
			return null;
		}
	}

	/**
	 * Elimina a un usuarario a través de su id
	 * 
	 * @param con conexión a la BD
	 * @param id  id del usuario a eliminar
	 */
	public static void eliminarUsuario(Connection con, int id) {
		try {
			PreparedStatement pstmt = con.prepareStatement("DELETE FROM usuario WHERE idusuario=?");
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
		}
	}

	/**
	 * Comprueba si el usuario tiene esa dicha contraseña a
	 * través de su id
	 * 
	 * @param  con      conexión a la BD
	 * @param  id       id del usuario a comprobar
	 * @param  password contraseña a comprobar
	 * @return
	 */
	public static boolean comprobarContrasena(Connection con, int id, String password) {
		try {
			PreparedStatement pstmt = con
					.prepareStatement("SELECT idusuario FROM usuario WHERE idusuario=? and password=?");
			pstmt.setInt(1, id);
			pstmt.setString(2, password);
			return pstmt.executeQuery().next();
		} catch (SQLException sqle) {
			return false;
		}
	}

	/**
	 * Comprueba si el usuario tiene esa dicha contraseña a
	 * través de su nombre del usuario
	 * 
	 * @param  con      conexión a la BD
	 * @param  nombre   nombre del usuario a comprobar
	 * @param  password contraseña a comprobar
	 * @return
	 */
	public static boolean comprobarContrasena(Connection con, String nombre, String password) {
		try {
			PreparedStatement pstmt = con
					.prepareStatement("SELECT idusuario FROM usuario WHERE nombre=? and password=?");
			pstmt.setString(1, nombre);
			pstmt.setString(2, password);
			return pstmt.executeQuery().next();
		} catch (SQLException sqle) {
			return false;
		}
	}

	/**
	 * Cambia la contraseña si el usuario y la contraseña
	 * coinciden
	 * 
	 * @param con         conexion a la BD
	 * @param id          id del usuario a cambiar contraseña
	 * @param password    contraseña antigua
	 * @param newPassword contraseña nueva
	 * @param stage       stage que ha llamado esta función para
	 *                    poder cerrarlo
	 */
	public static void cambiarContrasena(Connection con, int id, String password, String newPassword, Stage stage) {
		try {
			if (comprobarContrasena(con, id, password)) {
				PreparedStatement pstmt = con.prepareStatement("UPDATE usuario SET password=? WHERE idusuario=?");
				pstmt.setString(1, newPassword);
				pstmt.setInt(2, id);

				pstmt.executeUpdate();

				stage.close();
				Alert alertaCambiada = new Alert(Alert.AlertType.INFORMATION);

				alertaCambiada.setTitle("Contraseña Cambiada");
				alertaCambiada.setHeaderText("Contraseña Cambiada con éxito");
				alertaCambiada.initOwner(stage);
				alertaCambiada.show();
			}
		} catch (SQLException e) {
		}
	}

	/**
	 * Le sube de experiencia a un usuario, y si tiene más de
	 * 500 que es el tope sube de nivel y la experiencia se la
	 * volvemos a asignar a 0
	 * 
	 * @param  con       conexión a la BD
	 * @param  id        id del usuario
	 * @param  aumentoXP cantidad a aumentar la experiencia
	 * @return           false sino ha podido subir nivel o true
	 *                   si ha posido
	 */
	public static boolean amuentarXP(Connection con, int id, int aumentoXP) {
		try {
			ResultSet rs = getUsuario(con, id);
			rs.next();
			int exp = rs.getInt("experiencia") + aumentoXP;
			int nivel = rs.getInt("nivel");
			// Si es nivel 5 no puede subir más
			if (nivel != 5) {
				if (exp > 500) {
					nivel++;
					PreparedStatement pstmt = con
							.prepareStatement("UPDATE usuario SET experiencia=0 WHERE idusuario=?");
					pstmt.setInt(1, id);

					pstmt.executeUpdate();
					return subirNivel(con, id, nivel);
				} else {
					PreparedStatement pstmt = con
							.prepareStatement("UPDATE usuario SET experiencia=? WHERE idusuario=?");
					pstmt.setInt(1, exp);
					pstmt.setInt(2, id);

					pstmt.executeUpdate();
					return true;
				}
			} else {
				return false;
			}
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Sube el nivel de un usuario
	 * 
	 * @param  con   conexión a la BD
	 * @param  id    id del usuario
	 * @param  nivel nivel a subir
	 * @return       devuelve true si ha subido de nivel sino no
	 */
	public static boolean subirNivel(Connection con, int id, int nivel) {
		try {
			PreparedStatement pstmt = con.prepareStatement("UPDATE usuario SET nivel=? WHERE idusuario=?");
			pstmt.setInt(1, nivel);
			pstmt.setInt(2, id);

			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Cambia la imagen de un usuario
	 * 
	 * @param con conexión a la BD
	 * @param id  id del usuario a cambiar imagen
	 * @param img imagen a actualizar
	 */
	public static void cambiarImagen(Connection con, int id, int img) {
		try {
			PreparedStatement pstmt = con.prepareStatement("UPDATE usuario SET imagen=? WHERE idusuario=?");
			pstmt.setInt(1, img);
			pstmt.setInt(2, id);

			pstmt.executeUpdate();
		} catch (SQLException e) {
		}
	}
}
