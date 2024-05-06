package application.cookies;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class Hash {

	/**
	 * Comprueba si es el hash introducido y el id introducido
	 * hay resultados en la base de datos, si hay resultados
	 * significan que están correctos sino no están bien
	 * 
	 * @param  con  conexión a la Base de Datos
	 * @param  id   id para comprobar el hash
	 * @param  hash el hash a comprobar
	 * @return      true si está bien, sino devuelve false
	 */
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

	/**
	 * Crea el hash, normalmente esta funcion solo se llama
	 * cuando está marcado en iniciar sesión o registrarse para
	 * asignarle un hash y que pueda iniciar sesión sin
	 * introducir contraseña, ya que además crea las cookies en
	 * un PDF.
	 * 
	 * @param con conexión a la Base de Datos
	 * @param id  id a crear el hash
	 */
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

	/**
	 * Le quita el hash de la base de datos cuando se elimine la
	 * sesión
	 * 
	 * @param con conexión a la Base de Datos
	 * @param id  id a quitar el hash
	 */
	public static void quitarHash(Connection con, int id) {
		try {
			PreparedStatement pstmt = con.prepareStatement("UPDATE usuario SET hash=null WHERE idusuario=?");
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {

		}
	}

	/**
	 * Genera un string aleatorio de los caracteres permitidos
	 * de una longitud entre 6-8.
	 * 
	 * @return un hash
	 */
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
