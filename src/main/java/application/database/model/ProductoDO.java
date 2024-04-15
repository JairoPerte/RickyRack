package application.database.model;

import java.sql.Date;

public class ProductoDO {

	public static final int LIBRO = 0;
	public static final int PELICULA = 1;
	public static final int VIDEOJUEGO = 2;

	private int idproducto;
	private int categoria;
	private String titulo;
	private int duracion;
	private String sinopsis;
	private String genero;
	private String autor;
	private Date fecha;

	/**
	 * @param idproducto
	 * @param categoria
	 * @param titulo
	 * @param duracion
	 * @param sinopsis
	 * @param genero
	 * @param autor
	 * @param fecha
	 */
	public ProductoDO(int idproducto, int categoria, String titulo, int duracion, String sinopsis, String genero,
			String autor, Date fecha) {
		super();
		this.idproducto = idproducto;
		this.categoria = categoria;
		this.titulo = titulo;
		this.duracion = duracion;
		this.sinopsis = sinopsis;
		this.genero = genero;
		this.autor = autor;
		this.fecha = fecha;
	}

	/**
	 * @return the idproducto
	 */
	public int getIdproducto() {
		return idproducto;
	}

	/**
	 * @param idproducto the idproducto to set
	 */
	public void setIdproducto(int idproducto) {
		this.idproducto = idproducto;
	}

	/**
	 * @return the categoria
	 */
	public int getCategoria() {
		return categoria;
	}

	/**
	 * @param categoria the categoria to set
	 */
	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}

	/**
	 * @return the titulo
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * @param titulo the titulo to set
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * @return the duracion
	 */
	public int getDuracion() {
		return duracion;
	}

	/**
	 * @param duracion the duracion to set
	 */
	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	/**
	 * @return the sinopsis
	 */
	public String getSinopsis() {
		return sinopsis;
	}

	/**
	 * @param sinopsis the sinopsis to set
	 */
	public void setSinopsis(String sinopsis) {
		this.sinopsis = sinopsis;
	}

	/**
	 * @return the genero
	 */
	public String getGenero() {
		return genero;
	}

	/**
	 * @param genero the genero to set
	 */
	public void setGenero(String genero) {
		this.genero = genero;
	}

	/**
	 * @return the autor
	 */
	public String getAutor() {
		return autor;
	}

	/**
	 * @param autor the autor to set
	 */
	public void setAutor(String autor) {
		this.autor = autor;
	}

	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Override
	public String toString() {
		return "ProductoDO [idproducto=" + idproducto + ", categoria=" + categoria + ", titulo=" + titulo
				+ ", duracion=" + duracion + ", sinopsis=" + sinopsis + ", genero=" + genero + ", autor=" + autor
				+ ", fecha=" + fecha + "]";
	}

}
