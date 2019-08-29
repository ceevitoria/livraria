package com.cee.livraria.entity.tabpreco;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import com.cee.livraria.entity.AppBaseEntity;
import com.cee.livraria.entity.produto.RegraPesquisaProdutos;
import com.powerlogic.jcompany.domain.type.PlcYesNo;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;

@MappedSuperclass
public abstract class TabelaPreco extends AppBaseEntity {
	private static final long serialVersionUID = -2465750378390266238L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_TABELA_PRECO")
	private Long id;

	@NotNull
	@Size(max = 50)
	private String nome;

	@Size(max = 200)
	private String descricao;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInicio;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFim;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 1)
	private PlcYesNo padrao = PlcYesNo.N;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 1)
	private PlcYesNo ativa = PlcYesNo.S;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 1)
	private TipoVariacao tipoVariacao = TipoVariacao.V;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 2)
	private FontePrecificacao fontePrecificacao = FontePrecificacao.PV;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 1)
	private TipoPrecificacao tipoPrecificacao = TipoPrecificacao.A;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 2)
	private TipoArredondamento tipoArredondamento = TipoArredondamento.DC;

	@DecimalMax(value="1000")
	@Digits(integer = 8, fraction = 2)
	private BigDecimal variacao =  new BigDecimal(0.00);

	@NotNull
	@Size(max = 1)
	private String sitHistoricoPlc = "A";
	
	@OneToMany(targetEntity = com.cee.livraria.entity.tabpreco.ItemTabelaEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tabelaPreco")
	@ForeignKey(name = "FK_ITEMTABELA_TABELAPRECO")
	@PlcValDuplicity(property = "produto")
	@PlcValMultiplicity(referenceProperty = "produto", message = "{jcompany.aplicacao.mestredetalhe.multiplicidade.ItemTabelaEntity}")
	@Valid
	private List<ItemTabela> itemTabela;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public PlcYesNo getPadrao() {
		return padrao;
	}

	public void setPadrao(PlcYesNo padrao) {
		this.padrao = padrao;
	}

	public PlcYesNo getAtiva() {
		return ativa;
	}

	public void setAtiva(PlcYesNo ativa) {
		this.ativa = ativa;
	}

	public TipoVariacao getTipoVariacao() {
		return tipoVariacao;
	}

	public FontePrecificacao getFontePrecificacao() {
		return fontePrecificacao;
	}

	public void setFontePrecificacao(FontePrecificacao fontePrecificacao) {
		this.fontePrecificacao = fontePrecificacao;
	}

	public void setTipoVariacao(TipoVariacao tipoVariacao) {
		this.tipoVariacao = tipoVariacao;
	}

	public TipoPrecificacao getTipoPrecificacao() {
		return tipoPrecificacao;
	}

	public void setTipoPrecificacao(TipoPrecificacao tipoPrecificacao) {
		this.tipoPrecificacao = tipoPrecificacao;
	}

	public TipoArredondamento getTipoArredondamento() {
		return tipoArredondamento;
	}

	public void setTipoArredondamento(TipoArredondamento tipoArredondamento) {
		this.tipoArredondamento = tipoArredondamento;
	}

	public BigDecimal getVariacao() {
		return variacao;
	}

	public void setVariacao(BigDecimal variacao) {
		this.variacao = variacao;
	}

	public List<ItemTabela> getItemTabela() {
		return itemTabela;
	}

	public void setItemTabela(List<ItemTabela> itemTabela) {
		this.itemTabela = itemTabela;
	}

	public String getSitHistoricoPlc() {
		return sitHistoricoPlc;
	}

	public void setSitHistoricoPlc(String sitHistoricoPlc) {
		this.sitHistoricoPlc = sitHistoricoPlc;
	}

	@Embedded
	@Valid
	@Transient
	private transient RegraPesquisaProdutos regra;
	
	public RegraPesquisaProdutos getRegra() {
		return regra;
	}

	public void setRegra(RegraPesquisaProdutos regra) {
		this.regra = regra;
	}
	
}
