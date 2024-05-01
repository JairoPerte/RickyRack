package application.cookies;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class Hash {

	public static boolean comprobarHash(Connection con, int id, String hash) {
		try {
			PreparedStatement pstmt = con
					.prepareStatement("SELECT idusuario FROM usuario WHERE idusuario=? and hash=?");
			pstmt.setInt(1, id);
			pstmt.setString(2, hash);
			return pstmt.executeQuery().next();
		} catch (SQLException sqle) {
			return false;
		}
	}

	public static void crearHash(Connection con, int id) {
		try {
			PreparedStatement pstmt = con.prepareStatement("UPDATE usuario SET hash=? WHERE idusuario=?");
			String hash = generarHash();
			pstmt.setString(1, hash);
			pstmt.setInt(2, id);

			pstmt.executeUpdate();

			CookieWriter.crearCookies(id, hash);
		} catch (SQLException e) {

		}
	}

	public static String generarHash() {
		String caracteresPermitidos = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder hash = new StringBuilder();
		Random random = new Random();
		int longitud = (int) ((Math.random() * 3) + 6);

		// Generamos cada caracter del string aleatorio
		for (int i = 0; i < longitud; i++) {
			int indice = random.nextInt(caracteresPermitidos.length());
			hash.append(caracteresPermitidos.charAt(indice));
		}

		return hash.toString();
	}
}
