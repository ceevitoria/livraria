package com.cee.livraria.entity.compra;

import com.cee.livraria.entity.AppBaseEntity;
import com.cee.livraria.entity.produto.Produto;

public class FornecedorProduto extends AppBaseEntity {
	private static final long serialVersionUID = -305835460793103573L;

	private Fornecedor fornecedor;
	private Produto produto;
}
