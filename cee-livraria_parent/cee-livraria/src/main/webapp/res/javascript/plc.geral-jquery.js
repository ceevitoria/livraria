/**
 * plc.geral.jquery
 * Funções javascripts utilizadas pelo jCompany/Jaguar 
 * As funções abaixo, estão dispostas no formato jQuery, dando suporte para o funcionamento do sistema na camada visão.
 */

plc = {
	/**
	 * Transforma um Form em um Objeto.
	 * @param form Seletor jQuery, ou DOM do formulário.
	 * @param preffix Prefixo presente nos elementos do formulário, que deve ser descartado.
	 * 
	 * O Prefixo é comum em JSF. 
	 */
	uriParam:'?evento=y'
		
	/** Isolando nosso objeto jQuery */	
	,jq: jQuery.noConflict()
	,componentesDef:null
	,componenteFoco:null
	,form2json: function(form, preffix){
		var $form = (form && plc.jq(form)) || null;
		if ($form) {
			if ($form.is('form')) {
				var elements = $form[0].elements;
				if (elements && elements.length) {
					var data = {};
					plc.jq.each(elements, function(){
						var $it = plc.jq(this);
						var name = $it.attr('name');
						
						if (preffix && name && name.indexOf(preffix) == 0) {
							name = name.substring(preffix.length);
						}
						if (name) {
							var value = $it.val();
							var type = $it.attr('type');
							if ((type == 'checkbox' || type == 'radio') && $it.attr('checked') != true) {
								return;
							}
							if (value) {
								if (typeof data[name] != 'undefined') {
									if (data[name].constructor!= Array) {
										data[name]=[data[name], value];
									} else {
										data[name].push(value);
									}
								} else {
									//concatenando nome e valor para criar uma URI a ser inserida no linkPermanente.
									plc.uriParam=plc.uriParam+"&"+name+"="+value;
									data[name] = value;
								}
							}
						}
					});
					return data;
				}
			}
		}
		return null;
	}
	/**
	 * Cria uma nova Janela Dialog do jQuery UI.
	 * Recebe um objeto de configuração, que deve ter como parâmetro, a url e o tamanho da janela.
	 * {
	 * 	url: '...'
	 *  ,width: ...
	 *  ,height: ...
	 * }
	 * @param Objeto de configuração com URL.
	 * @return Retorna o Objeto jQuery Dialog.
	 */
	,janelaModal: function(c, dialogOpener){
		// Possibilita recursividade!
		if (window.parent && window.parent != window) {
			var funcaoModal = window.parent.plc.jq && window.parent.plc.jq && window.parent.plc.janelaModal;
			if (funcaoModal)
				return window.parent.plc.janelaModal(c, (dialogOpener || window));
		}
		var defaultWidth = 720, defaultHeight = 480;
		//verificando se já existe uma janela modal
		var janelas = plc.jq('div[id^=plc-modal]');
		if (plc.jq('div[id^=plc-modal]').length == 0) {
			id_modal = 'plc-modal';
		} else if (janelas.length > 0) {
			var id_modal = /(\d)/.exec(plc.jq('div[id^=plc-modal]')[janelas.length -1].id);
			if (id_modal) {
				id_modal = 'plc-modal' + (parseInt(id_modal[0]) + 1);	
			} else {
				id_modal = 'plc-modal2';	
			}
		}	

		// Cria o jQuery UI Dialog com iFrame!
		var dialog = plc.jq(
			'<div id="' + id_modal + '"'+
			'title="'+ (c.title||'') +'" '+
				'style="padding: 0px; margin: 0px; width: '+(c.width || defaultWidth)+'px; height: '+(c.height || defaultHeight)+'px; display: block;"'+
			'>'+
				'<iframe src="#" style="display: none; width: 99%; height: 99%; border: none;"></iframe>'+
			'</div>'
		);
		
		dialog.dialog({
			modal: true
			,width: (c.width || defaultWidth)
			,height: (c.height || defaultHeight)
			//,hide: 'slide'
			,open: function(){
				if (c.onopen){
					c.onopen(c, dialogOpener);
				}
				dialog.children('iframe').attr('src',c.url).show();
			}
			,dragStart: function(){
				if (c.ondragstart){
					c.ondragstart(c, dialogOpener);
				}
				dialog.children('iframe').hide();
			}
			,resizeStart: function(){
				if (c.onresizestart){
					c.onresizestart(c, dialogOpener);
				}
				dialog.children('iframe').hide();
			}
			,dragStop: function(){
				if (c.ondragstop){
					c.ondragstop(c, dialogOpener);
				}
				dialog.children('iframe').show();
			}
			,resizeStop: function(){
				if (c.onresizestop){
					c.onresizestop(c, dialogOpener);
				}
				dialog.children('iframe').show();
			}
			,beforeClose: function(){
				if (c.onbeforeclose){
					c.onbeforeclose(c, dialogOpener);
				}
				dialog.children('iframe').hide();
			}
			,close: function(){
				dialog.children('iframe').remove();
				if (c.onclosed){
					c.onclosed(c, dialogOpener);
				}
				dialog.dialog('destroy');
			}
		});
		// Injeta no iFrame o dialogOpener, e a funcao dialogClose!
		dialog.children('iframe').bind('load', function(){
			var iWindow = plc.jq(this)[0].contentWindow;
			iWindow.dialogOpener = dialogOpener || window;
			iWindow.dialogClose = function(){
				dialog.dialog('close');
				dialog.children('iframe').unbind('load');
			};
		});
		return dialog;
	}
	,applyToString: function(o){
		function toString(){
			return this.lookup || '';
		}
		if (o && (typeof o == 'object') && o.toString !== toString) {
			o.toString = toString;
			for (i in o) {
				if (o[i] && (typeof o[i] == 'object')) {
					plc.applyToString(o[i]);
				}
			}
		}
	}
	/**
	 * Transforma um Form em um Objeto.
	 * @param form Seletor jQuery, ou DOM do formulário.
	 * @param preffix Prefixo presente nos elementos do formulário, que deve ser descartado.
	 * 
	 * O Prefixo é comum em JSF. 
	 */
	,criaSelecaoJqGrid: function(idGrid, idNavegacao, configuracaoGrid){
		
		idGrid = document.getElementById(idGrid);
		idNavegacao = document.getElementById(idNavegacao);
		
		var actionForm = function(){
			var action = plc.jq('#corpo\\:formulario').attr('action');
			var index = action.indexOf('?');
			return (index != -1 ? action.substring(0, index) : action);
		};
		var modoJanelaPlc = configuracaoGrid.modoJanelaPlc || '';
		var nomeLogica = configuracaoGrid.nomeLogica || '';
		var evento = configuracaoGrid.evento ||getParametroUrl('evento')||'';
		var propAgregada = configuracaoGrid.propAgregada || nomeLogica;
		var urlPesquisa = (configuracaoGrid.urlPesquisa || (plcGeral.contextPath + '/soa/service/grid.' + nomeLogica));
		var params = configuracaoGrid.params || '';
		var urlParams = extractURLParams();
		var emiteMensagem = (configuracaoGrid.emiteMensagem == 'undefined' || configuracaoGrid.emiteMensagem == undefined) ? true : configuracaoGrid.emiteMensagem;
		var fwPlc = urlParams.fwPlc;
		var indMultiSelPlc = urlParams.indMultiSelPlc;
		
		var propAgregadaCampoOrigem = configuracaoGrid.propAgregadaCampoOrigem;
		var propAgregadaCampoDestino = configuracaoGrid.propAgregadaCampoDestino;

		var seletorBotaoPesquisar = (modoJanelaPlc == 'modal' ? '#modal\\:formulario\\:botaoAcaoPesquisar' : '#corpo\\:formulario\\:botaoAcaoPesquisar');
		var seletorFormulario = (modoJanelaPlc == 'modal' ? '#modal\\:formulario' : '#corpo\\:formulario');
		var prefixoFormulario = (modoJanelaPlc == 'modal' ? 'modal:formulario:' : 'corpo:formulario:');
		var dadosPesquisa = {};
		var aoSelecionarLinha = null;
		var urlEdicao = function(){
			if (!fwPlc) {
				if (configuracaoGrid.urlEdicao) {
					return configuracaoGrid.urlEdicao;
				} else {
					//se termina com 'con', significa que e uma consulta
					if(nomeLogica.lastIndexOf('con')==nomeLogica.length-3){
						var actionForm = actionForm();
						var index=actionForm.indexOf(plcGeral.contextPath)+plcGeral.contextPath.length;
						actionForm=actionForm.substring(index);
					} else {
						return '/f/n/' + consultardepartamentocon + 'man'
					}
				}
			} else {
				var link = fwPlc;
				if (fwPlc.indexOf('&')>0) {
					link = fwPlc.substring(0, fwPlc.indexOf('&'));
				}
				return plcGeral.contextPath+"/f/n/"+link;
			}			
			return actionForm;
			
		};
		var paramFwplc = function(){
			if (fwPlc) {
				if (fwPlc.indexOf('&')>0) {
					return fwPlc.substring(fwPlc.indexOf('&'));
				}
			}			
			return '';
			
		};
		var getDadosPesquisa = function(id) {
			var data = null;
			if (dadosPesquisa) {
				plc.jq.each(dadosPesquisa, function(){
					if (this.id == id) {
						data = this;
						return false;
					}
				});
			}
			return data;
		};
		
		if (configuracaoGrid.processaPostData){
			processaPostData = function(postData){
				configuracaoGrid.processaPostData(postData);
			}
		} else {
			processaPostData = function(postData){
				var _params = [];
				if (postData) {
					plc.jq.each(postData, function(k, v){
						_params.push(escape(k)+'='+escape(v));
					});	
				}	
				return _params;
			}
		}
		
		var pesquisaAjax = function(postData){
			// evitar cliques repetidos no botao
			plc.jq(seletorBotaoPesquisar).attr("disabled",true);
			//alterando ponteiro do mouse
			plc.jq(seletorBotaoPesquisar).css("cursor","wait");
			//exibindo icone ajax
			plc.partialLoading();
			var _params = [];
			if (params=='') {
				_params = processaPostData(postData);
			} else {	
				plc.jq.each(params, function(k, v){
					_params.push(v);
				});					
				_params.push('page='+postData.page);
				_params.push('rows='+postData.rows);
				_params.push('sidx='+postData.sidx);
				_params.push('sord='+postData.sord);
			}	
			plc.jq.ajax({
				url: urlPesquisa
				,type: "GET"
				,data: _params.join('&')
				,dataType: "json"
				,success: function(data){
					//limpando msgs antigas
					if(emiteMensagem) {
						plc.mensagem(false);
					}	
					//habilitando o botao de pesquisa
					plc.jq(seletorBotaoPesquisar).removeAttr("disabled");
					//alterando ponteiro do mouse
					plc.jq(seletorBotaoPesquisar).css("cursor","pointer");
					// ocultando icone ajax
					plc.partialLoading(false);
					if (!data) {
						if(emiteMensagem) {
							plc.mensagem("erro", "Erro ao processar pesquisa.", true);
						}	
						return;
					} else if (typeof data == "string") {
						if(emiteMensagem) {
							plc.mensagem("erro", data, false);
						}	
						return;
					}
					else if(data.messages){
						if(emiteMensagem) {
							plc.mensagem(data.messages, false);
						}	
					}

					else {
						if(emiteMensagem) {
							plc.mensagem(false);
						}	
					}
					dadosPesquisa = (data && data.rows) || [];
					// Em caso de chave natural, o objeto não tem ID, e o Grid precisa do ID para identificar a linha
					plc.jq.each(dadosPesquisa, function(i, row){
						plc.applyToString(row);
						if (row.id == null && row.idNatural) {
							row.id = i;
						}
					});
					var jqGrid = plc.jq(idGrid).get(0);
					if (jqGrid && data) {
						jqGrid.addJSONData(data);
						if (data.records == 0) {
							if(emiteMensagem) {
								plc.mensagem("erro", "Nenhum registro que atende aos crit&eacute;rios informados foi encontrado.", false);
							}	
						} 
					}
					plc.jq(idGrid).find('tr.jqgrow:odd').addClass('linhapar');
					plc.jq(idGrid).find('tr.jqgrow:even').addClass('linhaimpar');
				}
				,error: function(){
					//habilitando o botao de pesquisa
					plc.jq(seletorBotaoPesquisar).removeAttr("disabled");
					//alterando ponteiro do mouse
					plc.jq(seletorBotaoPesquisar).css("cursor","pointer");
					// ocultando icone ajax
					plc.partialLoading(false);

					plc.mensagem("erro", "Erro ao processar pesquisa.", true);
				}
			});
		};
		var pesquisar = function(){ 
			var formData = plc.form2json(seletorFormulario, prefixoFormulario);
			//plc.jq(idGrid).setPostData(formData);
			if(formData) {
				plc.jq(idGrid).setGridParam({
					postData: null
				});
				plc.jq(idGrid).setGridParam({
					page: 1
					,datatype: pesquisaAjax
					,postData: formData
				});
			} else {
				plc.jq(idGrid).setGridParam({
					postData: null
				});
				plc.jq(idGrid).setGridParam({
					page: 1
					,datatype: pesquisaAjax
				});
			}			
			plc.jq(idGrid).trigger("reloadGrid");
			//alterando o valor do link permanente.
			plc.jq("#linkPermanente").removeAttr("href").attr('href',''+actionForm()+plc.uriParam);
		};
		if (configuracaoGrid.aoSelecionar){
			aoSelecionarLinha = function(id){
				configuracaoGrid.aoSelecionar(id, getDadosPesquisa(id));
			};
		} else {
			aoSelecionarLinha = function(id){
				var data = getDadosPesquisa(id);
				if (!data) {
					return;
				}
				if (modoJanelaPlc == 'modal' || modoJanelaPlc == 'popup') {
					if (indMultiSelPlc) {
						
						var existeEmArray = false;

						for (var i=0; i<valorSelMulti.length; i++){
							var arrayObj = valorSelMulti[i];
							if (arrayObj.indice == data['id']){
								existeEmArray = true;	
							}
						}

						if (!existeEmArray){
							var obj = new Object();
							obj.indice = data['idAux'];
							var propAgregadaCampo = "";
							var virgula = ",";
							if(!propAgregadaCampoOrigem){
								obj.valor = propAgregada + '#' + data['idAux'] + "," + propAgregada + 'Lookup#' + data['lookup'];
							}else{
								var origem = propAgregadaCampoOrigem.split(",");
								var destino = propAgregadaCampoDestino.split(",");
								for(var c=0; c< origem.length; c++){
									var origemAux = data[origem[c]];
									var destinoAux = destino[c];
									if(c == origem.length-1){
										virgula = "";
									}
									propAgregadaCampo = propAgregadaCampo + destinoAux + "#" + origemAux + virgula;
								}
								
								obj.valor = propAgregada + '#' + data['idAux'] + "," + propAgregada + 'Lookup#' + data['lookup'] + "," + propAgregadaCampo;
							}

							valorSelMulti[valorSelMulti.length] = obj;
						} 
					
					} else {
						var values = [];
						for (var n in data) {
							values.push(n + '#' + data[n]);
						}
						values.push(propAgregada + '#' + data['idAux']);
						values.push(propAgregada + 'Lookup#' + data['lookup']);
						if (typeof data['idNatural']!= "undefined") {
							for (var n in data['idNatural']) {
								values.push(n + '#' + data['idNatural'][n]);
							}
						}	 
						values = values.join(',');
						devolveSelecao(values);
					}	
				} else {
					var redirect = urlEdicao();
					// Extrai os parametros da URL
					var params = extractURLParams(redirect);
					// Extrai os parametros da edicao.
					if (data.linkEdicaoPlc) {
						plc.jq.extend(params, extractURLParams('?' + data.linkEdicaoPlc));
					} else {
						params.id = id;
					}
					// Gera a URL final com os parametros.
					params = plc.jq.param(params);
					
					paramFwplc = paramFwplc();
					// gera a URL final
					document.location= redirect + (params ? ('?' + params + paramFwplc) : ('' +paramFwplc) );
				}
			};
			
			if (indMultiSelPlc) {
				aoSelecionarTodasLinhas = function(aRowids, status) {	
					valorSelMulti = new Array();
					if (status) {	
						for (var i=0; i < aRowids.length; i++){
							var data = getDadosPesquisa(aRowids[i]);
							if (!data) {
								continue;
							}
							var obj = new Object();
							obj.indice = data['idAux'];
							obj.valor = propAgregada + '#' + data['idAux'] + "," + propAgregada + 'Lookup#' + data['lookup'];
							valorSelMulti[valorSelMulti.length] = obj;
						} 
					}
				};
			}	
		}
		// Pesquisa não intrusiva!
		plc.jq(seletorBotaoPesquisar).removeAttr("onclick").click(pesquisar);
		var larg; 
		if (NavYes) { 
			larg= 0.965; 
		} else 	{
			larg = 0.965;
		}
		
		// Sempre quando a tabela termina de ser carregada a sua altura é reajustada basedo
		// na quantidade de linhas
		reajustaAltura = function() {
			plc.jq(".ui-jqgrid-bdiv").css("height", plc.jq("#plc-grid tr").length * 26);
		};
		
		var defaultJqGrid = {        
                jsonReader: {repeatitems: false}
				,datatype: "local"
                ,page: 1
                ,rowNum: configuracaoGrid.rowNum || 10
                ,pager: plc.jq(idNavegacao)
                ,width: plc.jq(idGrid).parent().width() * larg
                ,scrollrows : true
                ,viewrecords: true
                ,sortorder: "asc"
            	,multiselect: (indMultiSelPlc ? true : false)
                ,onSelectRow: aoSelecionarLinha
 				,onSelectAll: (indMultiSelPlc ? aoSelecionarTodasLinhas : "")
				,gridComplete: reajustaAltura
        }
		defaultJqGrid.height = defaultJqGrid.rowNum * 26;
		
		if (evento == 'y' || evento == 'Y' || evento == 'z' || evento == 'Z') {
			//se vai entrar pesquisando, verificar os parametros para ser preenchidos como argumento.
			setURLParamIntoForm(prefixoFormulario);
			defaultJqGrid.postData = plc.form2json(seletorFormulario, prefixoFormulario);
			plc.jq("#linkPermanente").attr('href', actionForm() + plc.uriParam);
			defaultJqGrid.datatype = pesquisaAjax;
		}
		 var jqGrid = plc.jq.extend({}, defaultJqGrid, configuracaoGrid.jqGrid);
		 if (jqGrid.rowNum && !jqGrid.hasOwnProperty('rowList')) {
                jqGrid.rowList = [jqGrid.rowNum, jqGrid.rowNum*2, jqGrid.rowNum*3]
        }
         plc.jq(idGrid)
         .jqGrid(jqGrid)
		.navGrid(idNavegacao, {
			search: false
			,add: false
			,edit: false
			,del: false
		});
		plc.jq(window).bind('resize', function() { plc.jq(idGrid).setGridWidth(plc.jq(window).width() * larg); });
	}
	,dadosTreeView: {
	}
	,criaTreeView: function(options){
		if (!options) {
			options = {};
		}
		var id = options.id || 'plc-treeview';
		var element = document.getElementById(id);
		if (!element) {
			element = plc.jq('<table id="plc-treeview" cellpadding="0" cellspacing="0"></table>').prependTo('#div-treeview').get(0);
		}
		var logica = options.logica || null;
		var dados = {};
		if (logica) {
			if (!plc.dadosTreeView.hasOwnProperty(logica)) {
				plc.dadosTreeView[logica] = {};
			}
			dados = plc.dadosTreeView[logica];
		}
		var urlEdicao = options.urlEdicao || null;
		var aoSelecionar = null;
		var recuperaObjetos = function(parent, level, data){
			
			var tree = plc.jq(document.getElementById(id));
			if (tree.length > 0 && data && data.rows) {
				// Injeta nos dados omplementares para arvore.
				if (data.rows.length > 0) {
					plc.jq.each(data.rows, function(){
						// Atualiza os dados carregados!
						plc.applyToString(this);
						dados[this.id] = plc.jq.extend(this, {
							level: level + 1
							,parent: parent
							,isLeaf: false
							,expanded: false
						});
					});
					if (dados[parent]) {
						dados[parent].expanded = true;
					}
					tree[0].addJSONData(data);
				}
			}

		}
		if (options.aoSelecionar) {
			aoSelecionar = function(id){
				options.aoSelecionar(id, dados[id]);
			};
		} else {
			aoSelecionar = function(id){
			 	var objeto = dados[id];
				if (!objeto) {
					return;
				}
				plc.jq("#corpo\\:formulario\\:idTreeView").val(id);
				plc.jq("#corpo\\:formulario\\:editObjectTreeView").click();
				
				return false;
			};
		}
		var defaultOptions = {
				colNames: ['', (options.titulo || '')]
				,colModel: [{
					name: 'id',
					index: 'id',
					width: 1,
					hidden: true,
					sortable: false
				},{
					name: 'lookup',
					index: 'lookup',
					width: 200,
					hidden: false,
					sortable: false
				}]
				,treeGrid: true
				,treeGridModel: 'adjacency'
				,ExpandColumn: 'lookup'
				,height: 240
				,ExpandColClick: false
				,jsonReader: {repeatitems: false}
				/*,treeIcons:{
					plus:'ui-icon-folder-collapsed'
					,minus:'ui-icon-folder-open'
					,leaf:'ui-icon-tag'
				}*/
				,multiselect: false
				,onSelectRow: aoSelecionar
				,datatype: function(postData){
					var level = (postData.n_level >= 0 ? postData.n_level : -1);
					var parent = (postData.nodeid || null);
					plc.jq.ajax({
						type: 'GET'
						,url: plcGeral.contextPath + '/soa/service/treeview.' + logica
						,data: postData
						,dataType: 'json'
						,success: function(data){
							recuperaObjetos(parent, level, data);
						}
					});
				}
			};
		plc.jq(function(){
			if (element) {
				plc.jq(element).jqGrid(plc.jq.extend(defaultOptions, (options.jqGrid || {})));		
			}
		});
	}
	,trocaConteudoParcial: function(selector, html, beginComment, endComment){		
		var begin = html.indexOf(beginComment);
		if (begin != -1) {
			var end = html.indexOf(endComment, begin);
			if (end != -1) {
				var content = plc.jq(selector);
				if (content.length > 0) {					
					plc.cacheScripts(html.substring(begin, end + endComment.length));
					content.empty().html(html.substring(begin, end + endComment.length));
					
				}
			}
		}
	}

	,cacheScripts: function(html,selector){
		var $parsed = plc.jq(html);
		var cachedScripts = new Array();
		$parsed.each(
			function(value){
				if(this && this.tagName && this.tagName.indexOf('SCRIPT')>=0){
					plcAjax.cachedScripts[plcAjax.cachedScripts.length]=this;
				}
			}
		);
	}


	,partialLoading: function(active) {
		if (active === false) {
			plc.jq('#partial-loading').remove();
		} else {
			var el = plc.jq('#partial-loading');
			if (el.length == 0) {
				var el = plc.jq('<div id="partial-loading" class="partial-loading"/>');
				if (plc.jq('#barraAcoes').length != 0) {
					plc.jq('#barraAcoes').append(el);
				} else if (plc.jq('#plc-corpo-acao').length != 0) {
					plc.jq('#plc-corpo-acao').append(el);
				} else {
					plc.jq('body').append(el);
				}
			} else {
				el.show();
			}
		}
	} 
	/**
	 * Exibe mensagem no cabeçalho da aplicação. Se a mensagem que for enviada for nula, o cabeçalho será limpado
	 */
	,mensagem: function( type, msg, clear, classMsg, classIcon, container ) {
		// funcao que adiciona uma mensagem.
		var appendMessage = function( $msg, icon, msg ){
			var text = plc.jq.trim( msg );
			if ( text ) {
				$msg.append( "<span class=\"ico "+ icon +"\"></span>" ).append( "<p>"+ text +"</p>" );
			}
		};
		// Monta as classes CSS de tipo e icone.
		if ( typeof type == "object" ) {
			// espera objetos de configuracao.
			// shift de argumentos.
			container = classIcon;
			classIcon = classMsg;
			classMsg = clear;
			clear = msg;
			for (var t in type) {
				this.mensagem( t, type[t], clear, (classMsg && classMsg[t] ? classMsg[t] : false), (classIcon && classIcon[t] ? classIcon[t] : false), container || false );
			}
			return;
		}
		if ( typeof type == "string" ) {
			classMsg = classMsg || ( "plc-msg-" + type );
			classIcon = classIcon || ( "i-plc-msg-" + type );
		} else {
			type = !!type;
		}
		// Possibilita forçar o container de mensagens.
		// Se nao informar, procura pelos divs de plc-msg-* dentro do #plc-mensagem
		// Itera em todas os DIVS containers de mensagens.
		plc.jq( container || "#plc-mensagem" ).children( "div" ).each(function( ){
			var $msg = plc.jq( this );
			// mensagem(false) limpa as mensagens.
			if ( type === false ) {
				$msg.empty( ).hide( );
			} else {
				if ( $msg.hasClass( classMsg ) ) {
					// mensagem(type, false) some com as mensagens especificas.
					if ( msg === false ) {
						$msg.empty( );
					} else if ( msg !== true ) {
						// mensagem(type, "msg", false) não limpa mensagens anteriores.
						if ( clear !== false ) {
							$msg.empty( ).hide( );
						}
						// Uma unica mensagem.
						if ( typeof msg == "string" ) {
							appendMessage( $msg, classIcon, msg );
						} else {
							// Uma colecao de mensagens.
							for (var m in msg) {
								appendMessage( $msg, classIcon, msg[m] );
							}
						}
					}
				}
			}
			// verifica se mensagens foram adicionadas.
			if ( $msg.find( "p" ).length > 0 ) {
				$msg.fadeIn( "slow" );
			} else {
				$msg.hide( );
			}
		});
	}

	/**
	 * Defini uma tecla de atalho, correspondendo a alguma ação do jCompany.
	 */
	,hotkey: function( hotkey, acao ) {
		
		//unbind no botao
		if(typeof(acao) == "function") {
			plc.jq(document).bind('keydown', hotkey,function (evt){acao(); return false; });
		} else {
			var botao = eval(selBotao(acao));
			//se o botao for null é porque não achou ou é um link
			if (botao == null) {
				botao = plc.jq("#" + acao);
			}	
			plc.jq(document).bind('keydown', hotkey,function (evt){plc.jq(botao).click(); return false; });
		}
	}	
	
	,manipulaErroAjax: function( data ) {
		plc.mensagem(false);
		plc.mensagem("erro", data.errorMessage, false);
	}
}
plc.jq;
