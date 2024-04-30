package application.database.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.cookies.Hash;

public class UsuarioDAO {

	private static final int ERROR_SQL = -2;
	private static final int USUARIO_EXISTENTE = -1;

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
				// throws UsuarioExistente
				return USUARIO_EXISTENTE;
			}
		} catch (SQLException e) {
			return ERROR_SQL;
		}
	}

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

	public static void eliminarUsuario(Connection con, int id) {
		try {
			PreparedStatement pstmt = con.prepareStatement("DELETE FROM usuario WHERE idusuario=?");
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
		}
	}

	public static boolean comprobarContrasena(Connection con, int id, String password) {
		try {
			PreparedStatement pstmt = con
					.prepareStatement("SELECT password FROM usuario WHERE idusuario=? and password=?");
			pstmt.setInt(1, id);
			pstmt.setString(2, password);
			return pstmt.executeQuery().next();
		} catch (SQLException sqle) {
			return false;
		}
	}

	public static void cambiarContrasena(Connection con, int id, String password, String newPassword) {
		try {
			if (comprobarContrasena(con, id, password)) {
				PreparedStatement pstmt = con.prepareStatement("UPDATE usuario SET password=? WHERE idusuario=?");
				pstmt.setString(1, newPassword);
				pstmt.setInt(2, id);

				pstmt.executeUpdate();
			} else {
				// throws ConetrasenaErronea
			}
		} catch (SQLException e) {
		}
	}

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
