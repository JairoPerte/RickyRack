package application.database.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.cookies.Hash;
import application.panels.PaneRegistro;

public class UsuarioDAO {

	public static int crearUsuario(Connection con, PaneRegistro registro) {
		try {
			PreparedStatement pstmt = con.prepareStatement(
					"INSERT INTO usuario (nombre,password,imagen,experiencia,nivel) VALUE(?,?,?,0,1)");

			// obtener del paneregistro los datos
			pstmt.setString(1, null);
			pstmt.setString(2, null);
			pstmt.setInt(3, 0);

			pstmt.executeUpdate();

			PreparedStatement pstmtId = con.prepareStatement("SELECT idusuario FROM usuario Where nombre=?");
			pstmtId.setString(1, null);

			ResultSet rs = pstmt.executeQuery();
			rs.next();

			// Si selecciona guardar sesión
			boolean opcionSelec = false;
			if (opcionSelec) {
				Hash.crearHash(con, rs.getInt("idusuario"));
			}

			return rs.getInt("idusuario");
		} catch (SQLException e) {
			return -1;
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

	public static void eliminarUsuario(Connection con, int id) {
		try {
			PreparedStatement pstmt = con.prepareStatement("DELETE FROM usuario WHERE idusuario=?");
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {

		}
	}

	public static boolean comprobarContraseña(Connection con, int id, String password) {
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

	public static boolean comprobarHash(Connection con, int id, String hash) {
		try {
			PreparedStatement pstmt = con.prepareStatement("SELECT hash FROM usuario WHERE idusuario=? and hash=?");
			pstmt.setInt(1, id);
			pstmt.setString(2, hash);
			return pstmt.executeQuery().next();
		} catch (SQLException sqle) {
			return false;
		}
	}

	public static void cambiarContrasena(Connection con, int id) {

	}
}
