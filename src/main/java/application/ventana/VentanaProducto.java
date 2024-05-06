package application.ventana;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.database.model.ProductoDAO;
import application.database.model.UsuarioDAO;
import application.exceptions.FaltaInterfaz;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VentanaProducto extends Stage {

	private static final int DESCONECTADO = -1;

	private static final int FEMALE_1 = 1;
	private static final int FEMALE_2 = 2;
	private static final int MALE_1 = 3;
	private static final int MALE_2 = 4;

	private int estrellasSel;
	private static boolean primerComent = false;

	/**
	 * Crea la ventana producto con el encabezado (datos
	 * tipicos, portada, y boton que abre la ventana galería), y
	 * el cuerpo que sería la descripción y la zona de
	 * comentarios y si no está logeado cuando entró en el
	 * producto no podrá ni comentar, ni darle likes a los
	 * comentarios ni calificar.
	 * 
	 * @param con        conexión a la base de datos
	 * @param userLog    id del usuario logeado
	 * @param idProducto id del producto en cuestion
	 */
	public VentanaProducto(Connection con, int userLog, int idProducto) {
		try {
			if (userLog != DESCONECTADO) {
				estrellasSel = ProductoDAO.usuarioProductoEstrellas(con, idProducto, userLog);
			}

			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setFitToWidth(true);
			GridPane total = new GridPane();

			// PRODUCTOS
			ResultSet producto = ProductoDAO.obtenerProducto(con, idProducto);
			int categoria = producto.getInt("categoria");

			// ENCABEZADO (TITULO + ESTRELLAS SIN RELLENAR - OTROS)
			// TITULO + ESTRELLA SIN RELLENAR
			GridPane encabezado = new GridPane();
			GridPane.setMargin(encabezado, new Insets(20));
			encabezado.setPrefHeight(105);
			encabezado.setMaxHeight(105);
			encabezado.setMinHeight(105);
			encabezado.setPrefWidth(this.getWidth());
			encabezado.setId("encabezado");
			total.add(encabezado, 0, 0);

			Label titulo = new Label(producto.getString("titulo"));
			titulo.setId("titulo");
			titulo.setPrefHeight(50);
			titulo.setAlignment(Pos.BASELINE_CENTER);

			HBox estrellas = new HBox(10);
			estrellas.setPrefHeight(50);
			estrellas.setAlignment(Pos.BOTTOM_RIGHT);

			GridPane.setConstraints(estrellas, 2, 0);
			GridPane.setMargin(estrellas, new Insets(0, 0, 0, 25));
			GridPane.setConstraints(titulo, 1, 0);
			GridPane.setMargin(titulo, new Insets(0, 0, 0, 10));

			encabezado.getChildren().addAll(estrellas, titulo);

			try {
				// Añadimos las 5 estrellas al HBox
				for (int i = 1; i <= 5; i++) {
					VBox vboxEstrella = new VBox();
					ImageView estrella = new ImageView(
							new Image(new FileInputStream(".\\media\\img\\interfaz\\estrella-sin-relleno.png")));
					estrella.setFitHeight(25);
					estrella.setFitWidth(25);
					vboxEstrella.getChildren().add(estrella);
					estrellas.getChildren().add(vboxEstrella);

					int posicion = i;
					vboxEstrella.setOnMouseClicked(event -> {
						if (userLog != DESCONECTADO) {

							// Si le ha vuelto a dar a la misma estrella que le habia
							// calificado no cambia
							if (estrellasSel != posicion) {
								try {
									// Dependiendo si no le había dado antes
									// insertamos/actualizamos
									if (estrellasSel == 0) {
										estrellasSel = posicion;
										ProductoDAO.insertarCalificacion(con, idProducto, userLog, estrellasSel);
									} else {
										estrellasSel = posicion;
										ProductoDAO.actualizarCalificacion(con, idProducto, userLog, estrellasSel);
									}

									// Modificamos las imagenes
									for (int j = 0; j < estrellas.getChildren().size(); j++) {

										// Obtenemos las antiguas y las sustituimos
										VBox vboxNuevo = (VBox) estrellas.getChildren().get(j);
										ImageView imgView = (ImageView) vboxNuevo.getChildren().get(0);

										// para que las anteriores estén marcadas
										if (j <= (posicion - 1)) {
											imgView.setImage(new Image(new FileInputStream(
													".\\media\\img\\interfaz\\estrella-relleno.png")));
										} else {
											imgView.setImage(new Image(new FileInputStream(
													".\\media\\img\\interfaz\\estrella-sin-relleno.png")));
										}

										imgView.setFitHeight(25);
										imgView.setFitWidth(25);
									}
								} catch (FileNotFoundException e) {
									new FaltaInterfaz(AlertType.ERROR, this);
								}
							}
						} else {
							necesitasLogearte();
						}
					});
				}

				// Si ya tiene estrellas se las ponemos
				if (userLog != DESCONECTADO) {
					comprobarEstrellasUser(estrellas);
				}
			} catch (FileNotFoundException e) {
				new FaltaInterfaz(AlertType.ERROR, this);
			}

			// FECHA + AUTOR + GENERO + DURACION + CALIFICACIONES
			HBox otros = new HBox(10);
			otros.setPrefHeight(50);
			encabezado.add(otros, 1, 1, 2, 1);
			GridPane.setMargin(otros, new Insets(0, 0, 0, 10));

			Label fecha = new Label(producto.getString("fecha"));
			fecha.prefHeight(50);
			fecha.setAlignment(Pos.BASELINE_CENTER);
			fecha.setId("fecha");

			Label autor = new Label(producto.getString("autor"));
			autor.prefHeight(50);
			autor.setAlignment(Pos.BASELINE_CENTER);
			autor.setId("autor");

			Label genero = new Label(producto.getString("genero"));
			genero.prefHeight(50);
			genero.setAlignment(Pos.BASELINE_CENTER);
			genero.setId("genero");

			Button galeriaBtn = new Button("Galería");
			galeriaBtn.prefHeight(50);
			galeriaBtn.setAlignment(Pos.BASELINE_CENTER);

			otros.getChildren().addAll(fecha, autor, genero);

			// No comparten las mismas categorias
			try {
				switch (categoria) {
				case 0:
					this.getIcons().add(new Image(new FileInputStream(".\\media\\img\\interfaz\\libro.png")));
					Label duracionL = new Label(producto.getInt("duracion") + " páginas");
					duracionL.prefHeight(50);
					duracionL.setAlignment(Pos.BASELINE_CENTER);
					duracionL.setId("duracion");
					otros.getChildren().addAll(duracionL, galeriaBtn);
					break;
				case 1:
					this.getIcons().add(new Image(new FileInputStream(".\\media\\img\\interfaz\\pelicula.png")));
					Label duracionP = new Label(producto.getInt("duracion") + " minutos");
					duracionP.prefHeight(50);
					duracionP.setAlignment(Pos.BASELINE_CENTER);
					duracionP.setId("duracion");
					otros.getChildren().addAll(duracionP, galeriaBtn);
					break;
				case 2:
					this.getIcons().add(new Image(new FileInputStream(".\\media\\img\\interfaz\\videojuego.png")));
					Label duracionV = new Label(producto.getInt("duracion") + " horas");
					duracionV.prefHeight(50);
					duracionV.setAlignment(Pos.BASELINE_CENTER);
					duracionV.setId("duracion");
					otros.getChildren().addAll(duracionV, galeriaBtn);
					break;
				}
			} catch (FileNotFoundException e) {
				new FaltaInterfaz(AlertType.ERROR, this);
			}

			// La estrella al lado de la label calificacion
			try {
				ImageView imgEstrella = new ImageView(
						new Image(new FileInputStream(".\\media\\img\\interfaz\\estrella-relleno.png")));

				imgEstrella.setFitHeight(20);
				imgEstrella.setFitWidth(20);

				// La añadimos
				otros.getChildren().add(imgEstrella);

				// La colocamos
				GridPane.setConstraints(imgEstrella, 1, 0);

				// Le ponemos margen
				GridPane.setMargin(imgEstrella, new Insets(0, 0, 0, 0));

				// Le mandamos un fallo de interfaz(FaltaInterfaz)
			} catch (FileNotFoundException e) {
				new FaltaInterfaz(AlertType.ERROR, this);
			}

			double calificacionMedia = ProductoDAO.calificacionMedia(con, idProducto);
			Label mediaCal = new Label(String.valueOf(calificacionMedia));
			otros.getChildren().add(mediaCal);

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

			// GALERIA
			// Buscamos toda la multimedia relacionada con ese producto
			ResultSet multimedias = ProductoDAO.obtenerMultiMedia(con, idProducto);
			boolean tienePortada = false;

			// Todas las imagenes las guardamos
			ArrayList<Image> galeria = new ArrayList<Image>();

			// Buscamos imagen para ponerla (mientras que no ha
			// encontrado imagen y siga habiendo multimedias que buscar)
			while (multimedias.next()) {
				if (!tienePortada && multimedias.getString("tipo").equals("P")) {
					tienePortada = true;
					try {
						// La añadimos
						Image imageProd = new Image(new FileInputStream(multimedias.getString("ruta")));
						ImageView imgProd = new ImageView(imageProd);
						galeria.add(imageProd);

						// Forzamos la imagen
						imgProd.setFitHeight(100);
						imgProd.setFitWidth(70);

						// La añadimos
						encabezado.getChildren().add(imgProd);

						// Lo colocamos en su sitio
						GridPane.setConstraints(imgProd, 0, 0, 1, 2);

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

						imgProd.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
								CornerRadii.EMPTY, new BorderWidths(2))));

						// La añadimos
						encabezado.getChildren().add(imgProd);

						// Lo colocamos en su sitio
						GridPane.setConstraints(imgProd, 0, 0, 1, 2);
					}
				} else {
					try {
						Image imgProd = new Image(new FileInputStream(multimedias.getString("ruta")));
						galeria.add(imgProd);
					} catch (FileNotFoundException e) {
					}
				}
			}

			galeriaBtn.setOnMouseClicked(event -> {
				new VentanaGaleria(this, galeria);
			});

			if (!tienePortada) {
				Label imgProd = new Label("No hay imagen para este artículo");

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
				encabezado.getChildren().add(imgProd);

				// Lo colocamos en su sitio
				GridPane.setConstraints(imgProd, 0, 0, 1, 2);
			}

			// CUERPO (DESCRIPCION - COMENTARIOS)
			// DESCRIPCION
			GridPane cuerpo = new GridPane();
			GridPane.setMargin(cuerpo, new Insets(0, 20, 0, 20));
			cuerpo.setPrefWidth(560);
			total.add(cuerpo, 0, 1);

			Label descripcion = new Label(producto.getString("sinopsis"));
			descripcion.setMaxWidth(Double.MAX_VALUE);
			descripcion.setWrapText(true);
			GridPane.setMargin(descripcion, new Insets(0, 0, 20, 0));
			descripcion.setBorder(new Border(
					new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
			cuerpo.add(descripcion, 0, 0);

			Label lblComentario = new Label("COMENTARIOS");
			GridPane.setHalignment(lblComentario, HPos.CENTER);
			cuerpo.add(lblComentario, 0, 1);

			// COMENTARIOS
			ResultSet comentarios = ProductoDAO.obtenerComentarios(con, idProducto);
			GridPane gPComentarios = new GridPane();
			int indice = 0;

			// Mientras que halla comentarios
			while (comentarios.next()) {
				// ID del comentario
				int idcomentario = comentarios.getInt("idComentario");

				GridPane gPComentario = new GridPane();
				gPComentario.setPrefWidth(560);

				// Datos del comentario
				Label comentarioUsuario = new Label(comentarios.getString("usuario"));
				comentarioUsuario.setWrapText(true);
				gPComentario.add(comentarioUsuario, 0, 1);
				comentarioUsuario.setId("user");
				comentarioUsuario.setMinWidth(80);

				Label comentarioNivel = new Label("Nivel " + comentarios.getInt("nivel"));
				comentarioNivel.setId("nivel-user");
				comentarioNivel.setMinWidth(40);
				gPComentario.add(comentarioNivel, 1, 1);

				// Ponemos la imagen del usuario
				try {
					switch (comentarios.getInt("imagen")) {
					case FEMALE_1:
						ImageView imgF1 = new ImageView(
								new Image(new FileInputStream(".\\media\\img\\interfaz\\defaultfemale1.png")));

						// Forzamos la imagen
						imgF1.setFitHeight(50);
						imgF1.setFitWidth(50);

						// La añadimos
						gPComentario.add(imgF1, 0, 0, 2, 1);
						break;
					case FEMALE_2:
						ImageView imgF2 = new ImageView(
								new Image(new FileInputStream(".\\media\\img\\interfaz\\defaultfemale2.png")));

						// Forzamos la imagen
						imgF2.setFitHeight(50);
						imgF2.setFitWidth(50);

						// La añadimos
						gPComentario.add(imgF2, 0, 0, 2, 1);
						break;
					case MALE_1:
						ImageView imgM1 = new ImageView(
								new Image(new FileInputStream(".\\media\\img\\interfaz\\defaultmale1.png")));

						// Forzamos la imagen
						imgM1.setFitHeight(50);
						imgM1.setFitWidth(50);

						// La añadimos
						gPComentario.add(imgM1, 0, 0, 2, 1);
						break;
					case MALE_2:
						ImageView imgM2;
						imgM2 = new ImageView(
								new Image(new FileInputStream(".\\media\\img\\interfaz\\defaultmale2.png")));

						// Forzamos la imagen
						imgM2.setFitHeight(50);
						imgM2.setFitWidth(50);

						// La añadimos
						gPComentario.add(imgM2, 0, 0, 2, 1);
					}
				} catch (FileNotFoundException e) {
					new FaltaInterfaz(AlertType.ERROR, this);
				}

				Label comentario = new Label(comentarios.getString("comentario"));
				comentario.setWrapText(true);
				gPComentario.add(comentario, 2, 0, 1, 2);
				GridPane.setMargin(comentario, new Insets(0, 0, 0, 10));

				Label comentarioLikes = new Label(String.valueOf(comentarios.getInt("likes")) + " likes");
				comentarioLikes.setTextAlignment(TextAlignment.CENTER);
				GridPane.setValignment(comentarioLikes, VPos.TOP);
				gPComentario.add(comentarioLikes, 3, 1);
				GridPane.setMargin(comentarioLikes, new Insets(0, 0, 0, 10));

				HBox corazones = new HBox();
				gPComentario.add(corazones, 3, 0);
				GridPane.setMargin(corazones, new Insets(0, 0, 0, 10));

				try {
					VBox vboxCorazonR = new VBox();
					VBox vboxCorazonA = new VBox();

					corazones.getChildren().addAll(vboxCorazonR, vboxCorazonA);

					// Obtenemos su like
					int likeSel = ProductoDAO.usuarioComentarioLike(con, userLog, idcomentario);

					// Sino le ha dado like antes
					if (likeSel == -1) {
						ImageView corazonR = new ImageView(
								new Image(new FileInputStream(".\\media\\img\\interfaz\\corazon.png")));
						corazonR.setFitHeight(25);
						corazonR.setFitWidth(25);
						ImageView corazonA = new ImageView(
								new Image(new FileInputStream(".\\media\\img\\interfaz\\corazon.png")));
						corazonA.setFitHeight(25);
						corazonA.setFitWidth(25);
						vboxCorazonR.getChildren().add(corazonR);
						vboxCorazonA.getChildren().add(corazonA);
					} else {
						comprobarLikesUser(vboxCorazonR, vboxCorazonA, likeSel);
					}

					vboxCorazonA.setOnMouseClicked(event -> {
						try {
							if (userLog != DESCONECTADO) {
								int likeSelec = ProductoDAO.usuarioComentarioLike(con, userLog, idcomentario);

								// Si ya le ha dado dislike no hace falta cambiar
								if (likeSelec != 1) {
									// Dependiendo si no le había dado antes
									// insertamos/actualizamos
									if (likeSelec == -1) {
										ProductoDAO.insertarLike(con, userLog, idcomentario, 1);
									} else {
										ProductoDAO.actualizarLike(con, userLog, idcomentario, 1);
									}

									ImageView corazonR = (ImageView) vboxCorazonR.getChildren().get(0);
									corazonR.setImage(
											new Image(new FileInputStream(".\\media\\img\\interfaz\\corazon.png")));
									corazonR.setFitHeight(25);
									corazonR.setFitWidth(25);

									ImageView corazonA = (ImageView) vboxCorazonA.getChildren().get(0);
									corazonA.setImage(new Image(
											new FileInputStream(".\\media\\img\\interfaz\\corazon-dislike.png")));
									corazonA.setFitHeight(25);
									corazonA.setFitWidth(25);
								}
							} else {
								necesitasLogearte();
							}
						} catch (FileNotFoundException e) {
							new FaltaInterfaz(AlertType.ERROR, this);
						}
					});

					vboxCorazonR.setOnMouseClicked(event -> {
						try {
							if (userLog != DESCONECTADO) {

								int likeSelec = ProductoDAO.usuarioComentarioLike(con, userLog, idcomentario);

								// Si ya le ha dado like no hace falta cambiar
								if (likeSelec != 0) {
									// Dependiendo si no le había dado antes
									// insertamos/actualizamos
									if (likeSelec == -1) {
										ProductoDAO.insertarLike(con, userLog, idcomentario, 0);
									} else {
										ProductoDAO.actualizarLike(con, userLog, idcomentario, 0);
									}

									ImageView corazonR = (ImageView) vboxCorazonR.getChildren().get(0);
									corazonR.setImage(new Image(
											new FileInputStream(".\\media\\img\\interfaz\\corazon-like.png")));
									corazonR.setFitHeight(25);
									corazonR.setFitWidth(25);

									ImageView corazonA = (ImageView) vboxCorazonA.getChildren().get(0);
									corazonA.setImage(
											new Image(new FileInputStream(".\\media\\img\\interfaz\\corazon.png")));
									corazonA.setFitHeight(25);
									corazonA.setFitWidth(25);
								}
							} else {
								necesitasLogearte();
							}
						} catch (FileNotFoundException e) {
							new FaltaInterfaz(AlertType.ERROR, this);
						}
					});

					gPComentario.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
							CornerRadii.EMPTY, new BorderWidths(2))));
					gPComentarios.add(gPComentario, 0, indice);
					indice++;
				} catch (FileNotFoundException e) {
					new FaltaInterfaz(AlertType.ERROR, this);
				}
			}

			// Zona para añadir comentarios
			GridPane anadirCom = new GridPane();

			TextField escribirCom = new TextField();
			escribirCom.setId("escribir-comentario");
			anadirCom.add(escribirCom, 0, 0);
			escribirCom.setPrefWidth(480);

			Button enviar = new Button("Enviar");
			anadirCom.add(enviar, 1, 0);
			anadirCom.setBorder(new Border(
					new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
			gPComentarios.add(anadirCom, 0, indice);

			cuerpo.add(gPComentarios, 0, 2);

			enviar.setOnMouseClicked(event -> {
				if (userLog != DESCONECTADO) {
					// Escribimos comentario y lo colocamos a nada el comentario
					ProductoDAO.escribirComentario(con, userLog, idProducto, escribirCom.getText());
					escribirCom.setText("");
					// Si es su primero le damos exp
					if (!primerComent) {
						Alert alerta = new Alert(AlertType.INFORMATION);
						alerta.setTitle("Felicidades por tu primer comentario");
						alerta.setHeaderText("Acabas de ganar 50 de experiencia por comentar por primera vez");
						UsuarioDAO.amuentarXP(con, userLog, 50);
						primerComent = true;
						alerta.show();
					}
				} else {
					necesitasLogearte();
				}
			});

			scrollPane.setContent(total);

			this.setTitle(titulo.getText());
			this.setResizable(false);
			Scene escena = new Scene(scrollPane, 600, 500);
			escena.getStylesheets().add(getClass().getResource("/estilos/producto.css").toExternalForm());
			this.setScene(escena);
			this.initModality(Modality.WINDOW_MODAL);

			this.show();
		} catch (SQLException e) {
		}
	}

	/**
	 * Una funcion que comprueba las estrellas que tenía puesto
	 * antes el usuario, antes de entrar, para refrescarle la
	 * imagen y esté igual que la dejó anteriormente
	 * 
	 * @param estrellas
	 */
	private void comprobarEstrellasUser(HBox estrellas) {
		if (estrellasSel != 0) {
			try {
				for (int j = 0; j < estrellas.getChildren().size(); j++) {
					VBox vboxNuevo = (VBox) estrellas.getChildren().get(j);
					ImageView imgView = (ImageView) vboxNuevo.getChildren().get(0);
					if (j <= (estrellasSel - 1)) {
						imgView.setImage(
								new Image(new FileInputStream(".\\media\\img\\interfaz\\estrella-relleno.png")));
					} else {
						imgView.setImage(
								new Image(new FileInputStream(".\\media\\img\\interfaz\\estrella-sin-relleno.png")));
					}
					imgView.setFitHeight(25);
					imgView.setFitWidth(25);
				}
			} catch (FileNotFoundException e) {
				new FaltaInterfaz(AlertType.ERROR, this);
			}
		}
	}

	/**
	 * Una función que comprueba el like que tenia puesto
	 * anteriormente el usuario si tenia, para refrescarle la
	 * imagen y esté igual que la dejó anteriormente
	 * 
	 * @param vboxCorazonR contenedor del corazón rojo
	 * @param vboxCorazonA contenedor del corazón azul
	 * @param likeSel      likeSel es el like que tenia
	 *                     seleccionado anteriormente
	 */
	private void comprobarLikesUser(VBox vboxCorazonR, VBox vboxCorazonA, int likeSel) {
		try {
			if (likeSel != -1) {
				switch (likeSel) {
				case 0:
					ImageView corazonR = new ImageView(
							new Image(new FileInputStream(".\\media\\img\\interfaz\\corazon-like.png")));
					corazonR.setFitHeight(25);
					corazonR.setFitWidth(25);
					ImageView corazonA = new ImageView(
							new Image(new FileInputStream(".\\media\\img\\interfaz\\corazon.png")));
					corazonA.setFitHeight(25);
					corazonA.setFitWidth(25);
					vboxCorazonR.getChildren().add(corazonR);
					vboxCorazonA.getChildren().add(corazonA);
					break;
				case 1:
					ImageView corazonO = new ImageView(
							new Image(new FileInputStream(".\\media\\img\\interfaz\\corazon.png")));
					corazonO.setFitHeight(25);
					corazonO.setFitWidth(25);
					ImageView corazonZ = new ImageView(
							new Image(new FileInputStream(".\\media\\img\\interfaz\\corazon-dislike.png")));
					corazonZ.setFitHeight(25);
					corazonZ.setFitWidth(25);
					vboxCorazonR.getChildren().add(corazonO);
					vboxCorazonA.getChildren().add(corazonZ);
					break;
				}
			}
		} catch (FileNotFoundException e) {
			new FaltaInterfaz(AlertType.ERROR, this);
		}
	}

	/**
	 * Una alerta que indica que tienes que logearte
	 */
	private void necesitasLogearte() {
		Alert necesitasLogearte = new Alert(AlertType.WARNING);
		necesitasLogearte.setTitle("No estás logeado");
		necesitasLogearte.setHeaderText("Necesitas logearte para desbloquear esta función");
		necesitasLogearte.setContentText(
				"Crease una cuenta a través del menu Sesión en Registrarse o si ya tienes una en Iniciar Sesión");
		necesitasLogearte.show();
	}

}
