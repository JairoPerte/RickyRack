package application.cookies;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

public class CookieWriter {

	public static void crearCookies(int id, String hash) {
		try {
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
			document.save(new File(".\\Cookies\\Cookies_User" + id + ".pdf"));
			document.close();
		} catch (IOException e) {
			new File(".\\Cookies\\Cookies_User" + id + ".pdf").delete();
			crearCookies(id, hash);
		}
	}

	public static void comprobarCookies() {

	}

	public static void main(String[] args) {
		crearCookies(1, Hash.generarHash());
	}
}
