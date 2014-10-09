package com.cee.livraria.entity.tabpreco;

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
@Table(name = "TABELA_PRECO")
@SequenceGenerator(name = "SE_TABELA_PRECO", sequenceName = "SE_TABELA_PRECO")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="TabelaPrecoEntity.queryMan", query="from TabelaPrecoEntity where sitHistoricoPlc='A'"),
	@NamedQuery(name="TabelaPrecoEntity.querySel", query="select id as id, nome as nome, ativa as ativa, dataInicio as dataInicio, dataFim as dataFim from TabelaPrecoEntity where sitHistoricoPlc='A' order by nome asc"), @NamedQuery(name = "TabelaPrecoEntity.querySelLookup", query = "select id as id, nome as nome from TabelaPrecoEntity where id = ? order by id asc") })
public class TabelaPrecoEntity extends TabelaPreco {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public TabelaPrecoEntity() {
//        id,        data_ult_alteracao,        usuario_ult_alteracao,
//        versao,        ativa,        data_fim,        data_inicio,
//        descricao,        nome,        autor,        codigo_barras,
//        colecao,        edicao,        editora,        espirito,
//        localizacao,        palavra_chave,        titulo,     sit_historico_plc 
		
		
		this.setRegra(new RegraPesquisaLivros());
	}

	@Override
	public String toString() {
		return getNome();
	}

}
