package com.cee.livraria.entity.produto;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

/**
 * Class DVD
 */
@SPlcEntity
@Entity
@Table(name = "DVD")
@SequenceGenerator(name = "SE_DVD", sequenceName = "SE_DVD")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="DVD.queryMan", query="from DVD"),
	@NamedQuery(name="DVD.querySel", query="select obj.id as id, obj.tipoProduto as tipoProduto, obj.codigoBarras as codigoBarras, obj.titulo as titulo, obj.artista as artista, obj.gravadora as gravadora, obj.palavrasChave as palavrasChave, obj.precoUltCompra as precoUltCompra from DVD obj order by obj.titulo asc"),
	@NamedQuery(name="DVD.queryEdita", query="select obj from DVD obj where obj.id = ?"),
	@NamedQuery(name="DVD.querySelLookup", query="select id as id, codigoBarras as codigoBarras, titulo as titulo from DVD where id = ? order by id asc") })
@DiscriminatorValue("D")
@ForeignKey(name = "FK_DVD_PRODUTO")
public class DVD extends Midia {
	private static final long serialVersionUID = 7797394047043215945L;
}
