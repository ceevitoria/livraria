package com.cee.livraria.entity.estoque;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name = "ESTOQUE")
@SequenceGenerator(name = "SE_ESTOQUE", sequenceName = "SE_ESTOQUE")
@Access(AccessType.FIELD)
@NamedQueries({ 
	@NamedQuery(name = "EstoqueEntity.querySel", query = "select obj.id, obj.quantidade, obj.dataConferencia, obj2.id as produto_id, obj2.tipoProduto as produto_tipoProduto, obj2.titulo as produto_titulo, obj2.codigoBarras as produto_codigoBarras, obj2.palavrasChave as produto_palavrasChave, obj2.precoUltCompra as produto_precoUltCompra, obj2.precoVendaSugerido as produto_precoVendaSugerido, obj3.id as localizacao_id, obj3.codigo as localizacao_codigo, obj3.descricao as localizacao_descricao from EstoqueEntity obj inner join obj.produto obj2 inner join obj.localizacao obj3 order by obj2.titulo asc"),
	@NamedQuery(name = "EstoqueEntity.querySelByProduto", query = "from EstoqueEntity where produto = :produto"),
	@NamedQuery(name = "EstoqueEntity.querySelByProdutoAndLocalizacao", query = "from EstoqueEntity where produto = :produto and localizacao = :localizacao"),
	@NamedQuery(name = "EstoqueEntity.querySelByProdutoAndEstoque", query = "from EstoqueEntity where produto = :produto and estoque = :estoque"),
	@NamedQuery(name = "EstoqueEntity.querySelLookup", query = "select id as id, produto as produto from EstoqueEntity where id = ? order by id asc")})
public class EstoqueEntity extends Estoque {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public EstoqueEntity() {
	}

	@Override
	public String toString() {
		return getProduto().getTitulo() + "-" + getQuantidade().toString();
	}

}
