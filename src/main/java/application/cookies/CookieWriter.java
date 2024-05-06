package application.cookies;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.text.PDFTextStripper;

public class CookieWriter {

	private static final int USUARIO_DESCONECTADO = -1;

	public static void crearCookies(int id, String hash) {
		try {
			// Si no exite el directo lo creamos
			File directorioCookies = new File(".\\Cookies");
			if (!directorioCookies.exists()) {
				directorioCookies.mkdir();
			}

			// Crear un nuevo documento PDF
			PDDocument document = new PDDocument();
			// Crear una nueva página
			PDPage newPage = new PDPage();
			// Agregar la nueva página al documento
			document.addPage(newPage);
			// Crear el contenido de la nueva página
			PDPageContentStream contentStream = new PDPageContentStream(document, newPage);

			// Establecer la fuente y tamaño de la fuente
			contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 2);

			// Escribir el contenido en la página
			contentStream.beginText();
			contentStream.newLineAtOffset(100, 700); // Donde empieza el texto
			contentStream.showText("user=" + id);
			contentStream.newLine();
			contentStream.showText("hash=" + hash);
			contentStream.endText();
			// Cerramos el ContentStream
			contentStream.close();

			// Lo Guardamos en el documento PDF
			File pdf = new File(".\\Cookies\\Cookies_User" + id + ".pdf");

			// Si existe uno antiguo creamos uno nuevo
			if (pdf.exists()) {
				pdf.delete();
			}

			document.save(pdf);
			document.close();
		} catch (IOException e) {
			new File(".\\Cookies\\Cookies_User" + id + ".pdf").delete();
		}
	}

	public static ArrayList<Integer> comprobarCookies(Connection con) {

		// Buscamos en el directorios
		File directorioCookies = new File(".\\Cookies");

		// ID de todas las sesiones inciadas
		ArrayList<Integer> idReferencias = new ArrayList<Integer>();

		// Si no exite el directo lo creamos
		if (!directorioCookies.exists()) {
			directorioCookies.mkdir();
		}

		// Almacenamos todas las cookies guardadas en dicho
		// directorio
		File[] cookies = directorioCookies.listFiles();

		// Si está vacia (la carpeta .\\cookies) enviamos
		// directamente la referencia
		// -1(usuario_desconectado)
		if (cookies == null) {
			idReferencias.add(0, USUARIO_DESCONECTADO);
			return idReferencias;
		}

		// Por cada cookie.pdf encontrada comprueba
		for (File cookie : cookies) {
			// Si es un archivo y tiene la estructura
			// Cookies_User\\digitos\\.pdf
			if (cookie.isFile() && cookie.getName().matches("^Cookies_User\\d+\\.pdf$")) {
				// Intentamos cargar el documento cookie
				try (PDDocument documento = Loader.loadPDF(cookie)) {
					// Extraemos el texto del pdf
					PDFTextStripper textStripper = new PDFTextStripper();
					String text = textStripper.getText(documento);

					// Guardamos un patron que siga el texto \\d+ (1 digito o
					// mas) y \\w+ es un caracter alfanumerico o más
					Pattern patron = Pattern.compile("user=(\\d+)hash=(\\w+)");
					// Y el matcher un patron aplicarlo a un texto
					Matcher matcher = patron.matcher(text);

					// Si hay coincidencia
					if (matcher.find()) {
						// Extraemos el idusario y la cadena hash
						int idusuario = Integer.parseInt(matcher.group(1)); // (\\d+)
						String hash = matcher.group(2);

						// Si hace referencia al hash real pues lo guardamos como
						// login
						if (Hash.comprobarHash(con, idusuario, hash)) {
							idReferencias.add(idusuario);
						}
					}
				} catch (IOException e) {
					idReferencias.add(0, USUARIO_DESCONECTADO);
					return idReferencias;
				}
				// Si hay algunas cookies que descuadran lo consideramos
				// desconectado ya que el propio usuario las está tocando
			} else {
				idReferencias.add(0, USUARIO_DESCONECTADO);
				return idReferencias;
			}
		}
		if (idReferencias.isEmpty()) {
			idReferencias.add(0, USUARIO_DESCONECTADO);
			return idReferencias;
		}
		return idReferencias;
	}

	public static void eliminarCookie(int userLog) {
		File cookie = new File(".\\Cookies\\Cookies_User" + userLog + ".pdf");
		if (cookie.exists()) {
			cookie.delete();
		}
	}
}
