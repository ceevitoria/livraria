package com.cee.livraria.entity.produto;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

@MappedSuperclass
public class Midia extends Produto {
	private static final long serialVersionUID = 6217296144309188018L;

	@Size(max = 100)
	private String artista;

	@Size(max = 100)
	private String gravadora;

	public String getArtista() {
		return artista;
	}

	public void setArtista(String artista) {
		this.artista = artista;
	}

	public String getGravadora() {
		return gravadora;
	}

	public void setGravadora(String gravadora) {
		this.gravadora = gravadora;
	}

	/*
	 * Construtor padrao
	 */
	public Midia() {
	}
	
	@Override
	public String toString() {
		if (getTitulo() != null) {
			return getTitulo();
		} else {
			return "CD";
		}
	}
	

}
