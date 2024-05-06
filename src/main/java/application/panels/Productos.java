package application.panels;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.App;
import application.database.model.ProductoDAO;
import application.ventana.VentanaAnadirProducto;
import application.ventana.VentanaProducto;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Productos extends ScrollPane {

	private static final int DESCONECTADO = -1;

	/**
	 * Carga todos los productos en un ScrollPane de una
	 * determinada categoría
	 * 
	 * @param userLog   user conectado/no conectado
	 * @param categoria la categoria a cargar
	 */
	public Productos(int userLog, int categoria, Connection con, Stage stage) {
		try {
			ResultSet rs = ProductoDAO.obtenerProductos(con, categoria);

			GridPane prodsCats = new GridPane();

			int indice = 0;

			while (rs.next()) {
				GridPane prod = cargarProducto(con, rs);

				prodsCats.getChildren().add(prod);

				GridPane.setMargin(prod, new Insets(10));
				GridPane.setConstraints(prod, 0, indice++);
			}

			// Añadir un articulo solo lo podrá ver si esta conectado
			if (userLog != DESCONECTADO) {
				GridPane addProd = new GridPane();

				// Propiedades:
				// La altura no es modificable, el ancho si es responsivo
				addProd.setPrefHeight(120);
				addProd.setMaxHeight(120);
				addProd.setMinHeight(120);
				addProd.setPadding(new Insets(10));
				addProd.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
						new BorderWidths(2))));

				try {
					ImageView imgBotonAdd = new ImageView(
							new Image(new FileInputStream(".\\media\\img\\interfaz\\boton-add.png")));

					// Forzamos la imagen
					imgBotonAdd.setFitHeight(100);
					imgBotonAdd.setFitWidth(100);

					// La añadimos
					addProd.getChildren().add(imgBotonAdd);

					// Lo colocamos en su sitio
					GridPane.setConstraints(imgBotonAdd, 0, 0);
				} catch (FileNotFoundException e) {
					// throws FaltaInterfaz
				}

				Label titulo = new Label("Haz click aquí para añadir un nuevo artículo...");
				titulo.setWrapText(true);

				// La añadimos
				addProd.getChildren().add(titulo);

				// Lo colocamos en su sitio
				GridPane.setConstraints(titulo, 1, 0);

				// Lo decoramos
				GridPane.setMargin(titulo, new Insets(0, 0, 0, 10));
				titulo.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
						new BorderWidths(2))));

				addProd.setId("prodCat");
				addProd.setOnMouseEntered(event -> {
					addProd.getScene().setCursor(Cursor.HAND);
				});
				addProd.setOnMouseExited(event -> {
					addProd.getScene().setCursor(Cursor.DEFAULT);
				});
				addProd.setOnMouseClicked(event -> {
					new VentanaAnadirProducto(stage, con, categoria, App.userLog);
				});

				// Lo añadimos al producto total
				prodsCats.getChildren().add(addProd);

				GridPane.setMargin(addProd, new Insets(10));
				GridPane.setConstraints(addProd, 0, indice);
			}

			// Lo añadimos
			this.setContent(prodsCats);

			// Para que sea responsiva
			this.setFitToWidth(true);
			this.setFitToHeight(true);
		} catch (SQLException e) {
		}
	}

	/**
	 * 
	 * Carga el producto de un resultset especificado
	 * 
	 * @param  con Conexión a la BD
	 * @param  rs  El resultset de un producto a cargar
	 * @return
	 */
	public static GridPane cargarProducto(Connection con, ResultSet rs) {
		try {
			GridPane prodCat = new GridPane();
			GridPane cuerpo = cargarCuerpoProducto(con, rs);

			int id = rs.getInt("idproducto");

			// Buscamos toda la multimedia relacionada con ese producto
			ResultSet multimedias = ProductoDAO.obtenerMultiMedia(con, id);
			boolean tienePortada = false;

			// Buscamos imagen para ponerla (mientras que no ha
			// encontrado imagen y siga habiendo multimedias que buscar)
			while (!tienePortada && multimedias.next()) {
				if (multimedias.getString("tipo").equals("P")) {
					tienePortada = true;
				}
			}

			// Si no existe el ficchero/no se encuentra (no existe para
			// ese artículo)
			try {
				// La añadimos
				ImageView imgProd = new ImageView(new Image(new FileInputStream(multimedias.getString("ruta"))));

				// Forzamos la imagen
				imgProd.setFitHeight(100);
				imgProd.setFitWidth(70);

				// La añadimos
				prodCat.getChildren().add(imgProd);

				// Lo colocamos en su sitio
				GridPane.setConstraints(imgProd, 0, 0);

				// Si no hay pues mostramos una label que avisa que no hay
				// para ese articulo
			} catch (Exception e) {
				Label imgProd = new Label("No hay portada para este artículo");

				imgProd.setTextAlignment(TextAlignment.CENTER);

				// Para que se amolde
				imgProd.setWrapText(true);

				// Para que tenga las mismas proporciones que la imagen
				imgProd.setMaxHeight(100);
				imgProd.setMinHeight(100);
				imgProd.setPrefHeight(100);
				imgProd.setMaxWidth(70);
				imgProd.setMinWidth(70);
				imgProd.setPrefWidth(70);

				imgProd.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
						new BorderWidths(2))));

				// La añadimos
				prodCat.getChildren().add(imgProd);

				// Lo colocamos en su sitio
				GridPane.setConstraints(imgProd, 0, 0);
			}

			prodCat.getChildren().add(cuerpo);

			GridPane.setConstraints(cuerpo, 1, 0);

			// Propiedades:
			// La altura no es modificable, el ancho si es responsivo
			prodCat.setPrefHeight(120);
			prodCat.setMaxHeight(120);
			prodCat.setMinHeight(120);
			prodCat.setPadding(new Insets(10));
			prodCat.setId("prodCat");
			prodCat.setOnMouseEntered(event -> {
				prodCat.getScene().setCursor(Cursor.HAND);
			});
			prodCat.setOnMouseExited(event -> {
				prodCat.getScene().setCursor(Cursor.DEFAULT);
			});
			prodCat.setOnMouseClicked(event -> {
				new VentanaProducto(con, App.userLog, id);
			});
			return prodCat;
		} catch (SQLException e) {
			return null;
		}
	}

	/**
	 * Carga el cuerpo del Producto para introducirlo en el
	 * producto espeficicado
	 * 
	 * @param  con Conexión de BD
	 * @param  rs  El resultset de un producto a cargar el
	 *             producto
	 * @return
	 */
	public static GridPane cargarCuerpoProducto(Connection con, ResultSet rs) {
		try {
			// El GridPane para enviar
			GridPane cuerpoProd = new GridPane();

			// Obtenemos los datos
			Label titulo = new Label(rs.getString("titulo"));
			double calificacionMedia = ProductoDAO.calificacionMedia(con, rs.getInt("idproducto"));
			Label mediaCal = new Label(String.valueOf(calificacionMedia));

			if (calificacionMedia > 4.5) {
				mediaCal.setTextFill(Color.DARKGREEN);
			} else if (calificacionMedia > 3.5) {
				mediaCal.setTextFill(Color.GREEN);
			} else if (calificacionMedia > 2.5) {
				mediaCal.setTextFill(Color.ORANGE);
			} else if (calificacionMedia > 1.5) {
				mediaCal.setTextFill(Color.ORANGERED);
			} else {
				mediaCal.setTextFill(Color.RED);
			}

			Label sinopsis = new Label(rs.getString("sinopsis"));
			sinopsis.setWrapText(true); // Para que el texto sea responsivo

			// Si no encontramos la estrella el Usuario tiene un
			// problema d interfaz
			try {
				ImageView imgEstrella = new ImageView(
						new Image(new FileInputStream(".\\media\\img\\interfaz\\estrella-relleno.png")));

				imgEstrella.setFitHeight(20);
				imgEstrella.setFitWidth(20);

				// La añadimos
				cuerpoProd.getChildren().add(imgEstrella);

				// La colocamos
				GridPane.setConstraints(imgEstrella, 1, 0);

				// Le ponemos margen
				GridPane.setMargin(imgEstrella, new Insets(0, 0, 0, 25));
			} catch (FileNotFoundException e) {
				;
			}

			cuerpoProd.getChildren().addAll(titulo, mediaCal, sinopsis);
			cuerpoProd.autosize();

			// Los colocamos
			GridPane.setConstraints(titulo, 0, 0);
			GridPane.setConstraints(mediaCal, 2, 0);
			GridPane.setConstraints(sinopsis, 0, 1, 3, 1);

			GridPane.setMargin(sinopsis, new Insets(10, 0, 0, 10));
			GridPane.setMargin(titulo, new Insets(0, 0, 0, 10));

			// Los bordes
			sinopsis.setBorder(new Border(
					new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
			titulo.setBorder(new Border(
					new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
			mediaCal.setBorder(new Border(
					new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

			return cuerpoProd;
		} catch (SQLException e) {
			return null;
		}
	}
}
