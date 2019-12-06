package br.edu.utils.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import br.edu.utils.string.ManipulaStringHelper;
import br.edu.utils.vo.ColunasIguaisIndicesVO;
import br.edu.utils.vo.ListaOcorrenciasMesmoTipoVO;
import br.edu.utils.vo.RegistroDaTabelaVO;
import br.edu.utils.vo.RelatorioComparacaoTabelaSelect;
import br.edu.utils.vo.TabelaSelectGenericaOracle;
import br.edu.utils.vo.TipoOcorrencia;

public class TabelaSelectGenericaOracleHelper {
	private static final Logger logger = Logger.getLogger(TabelaSelectGenericaOracleHelper.class);
	private ManipulaStringHelper manipulaStringHelper = new ManipulaStringHelper();
	
	public TabelaSelectGenericaOracle getTabelaGenericaDeArquivoSelectTxtOracle(File arquivoSelectOracle) throws FileNotFoundException{
		TabelaSelectGenericaOracle tabelaSelectGenericaOracle = new TabelaSelectGenericaOracle();
		tabelaSelectGenericaOracle.setPathArquivo(arquivoSelectOracle.getAbsolutePath());
		Double tamanhoOParcial = 0.0;
		Double tamanhoArquivo = ((Long)arquivoSelectOracle.length()).doubleValue();
		Double progressoEmDecimal = 0.0;
		
		Scanner leitor = new Scanner(arquivoSelectOracle);
		String linha = "";
		boolean leuALinhaDasColunas = false;
		String[] colunas = null;
		do{
			linha = leitor.nextLine();
			if(linha!=null && !linha.isEmpty()){				
				colunas = linha.trim().split(" ");			
				leuALinhaDasColunas = true;
			}
		}while(leuALinhaDasColunas==false && leitor.hasNext());
		List<String> listaColunas = new ArrayList<>();
		for (String coluna : colunas) {
			if(!coluna.isEmpty())
				listaColunas.add(coluna);
		}
		logger.info("Qtd de colunas: " + listaColunas.size());
		logger.info("Nomes das Colunas: " + listaColunas);
		tabelaSelectGenericaOracle.setNomesColunas(listaColunas);
		/* A leitura dessa linha deve ser imediatamente apos a leitura das colunas */
		if(leitor.hasNext()){
			linha = leitor.nextLine();			
			tabelaSelectGenericaOracle.setQtdCaracteresRegistro(linha);// Supondo ser a linha com os tracos '----'
			logger.info(tabelaSelectGenericaOracle.getQtdCaracteresRegistro());
			logger.debug(tabelaSelectGenericaOracle.getTamanhoDaLinha());
		}
		while(leitor.hasNext()){
			linha = leitor.nextLine();
			tamanhoOParcial = 0.0;
			tamanhoOParcial = (double)linha.length();
			logger.debug("tamanho da linha: " + linha.length());
			tabelaSelectGenericaOracle.setLinhaRegistro(linha);
			progressoEmDecimal = tamanhoOParcial/tamanhoArquivo;
			
		}
		logger.debug(tabelaSelectGenericaOracle.getRegistrosDaTabela());
		leitor.close();
		return tabelaSelectGenericaOracle;
	}
	
	/* Processo para compara 2 tabelas resultados de arquivos Select(txt)  do Oracle */
	/* Se primaryKey for nula entao so ira comparar a diferenca do registro inteiro sem discriminar se o registro ew igual ou nao*/
	public RelatorioComparacaoTabelaSelect comparaTabelasSelect(TabelaSelectGenericaOracle tabelaSelectGenericaOracle1, TabelaSelectGenericaOracle tabelaSelectGenericaOracle2, List<ColunasIguaisIndicesVO> primaryKeys){		
		try {
			RelatorioComparacaoTabelaSelect relatorio = new RelatorioComparacaoTabelaSelect();
			
			if(primaryKeys != null && !primaryKeys.isEmpty()){
				//tabelaSelectGenericaOracle1.getNomesColunas().contains(primaryKeys.getNomeColunaTexto1()
				if(verificaSeAsPrimaryKeysEstaoNaListaDaTabelaGenerica1(tabelaSelectGenericaOracle1, primaryKeys)){
					return comparacaoComPk(tabelaSelectGenericaOracle1, tabelaSelectGenericaOracle2, relatorio, primaryKeys);
				}else{ 
					return comparacaoSemPk(tabelaSelectGenericaOracle1, tabelaSelectGenericaOracle2, relatorio);
				}
			}else{
				return comparacaoSemPk(tabelaSelectGenericaOracle1, tabelaSelectGenericaOracle2, relatorio);
			}
			
			
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}

	private boolean verificaSeAsPrimaryKeysEstaoNaListaDaTabelaGenerica1(TabelaSelectGenericaOracle tabelaSelectGenericaOracle1, List<ColunasIguaisIndicesVO> primaryKeys) {
		boolean valida = true;
		for (ColunasIguaisIndicesVO colunasIguaisIndicesVO : primaryKeys) {
			if(!tabelaSelectGenericaOracle1.getNomesColunas().contains(colunasIguaisIndicesVO.getNomeColunaTexto1())){
				valida = false;
				break;
			}
		}
		return valida;
	}

	private RelatorioComparacaoTabelaSelect comparacaoComPk(TabelaSelectGenericaOracle tabelaSelectGenericaOracle1,
			TabelaSelectGenericaOracle tabelaSelectGenericaOracle2, RelatorioComparacaoTabelaSelect relatorio, List<ColunasIguaisIndicesVO> primaryKeys) {
		/* Deve se ter uma lista de ocorrencias do mesmo tipo e para um mesmo arquivo para cada processo*/
		/* Verificacao se nao existe alguma coluna no arquivo 2*/		
		verificaSeNaoTemColuna(tabelaSelectGenericaOracle1, tabelaSelectGenericaOracle2, relatorio);
		/* Verificacao se nao existe alguma coluna no arquivo 1*/		
		verificaSeNaoTemColuna(tabelaSelectGenericaOracle2, tabelaSelectGenericaOracle1, relatorio);		
		/* Verificacao divergencia no tamanho das colunas q existem em ambas as tabelas select */
		verificaQtdDeCaracteresDasColunas(tabelaSelectGenericaOracle1, tabelaSelectGenericaOracle2, relatorio);		
		/* Verificacao divergencia no tamanho das colunas q existem em ambas as tabelas select */
		verificaRegistroInexistenteComNSU(tabelaSelectGenericaOracle1, tabelaSelectGenericaOracle2, relatorio, primaryKeys);
		verificaRegistroAlteradoComNSU(tabelaSelectGenericaOracle1, tabelaSelectGenericaOracle2, relatorio, primaryKeys);
		return relatorio;
	}

	private RelatorioComparacaoTabelaSelect comparacaoSemPk(TabelaSelectGenericaOracle tabelaSelectGenericaOracle1,
			TabelaSelectGenericaOracle tabelaSelectGenericaOracle2, RelatorioComparacaoTabelaSelect relatorio) {
		/* Deve se ter uma lista de ocorrencias do mesmo tipo e para um mesmo arquivo para cada processo*/
		/* Verificacao se nao existe alguma coluna no arquivo 2*/
		verificaSeNaoTemColuna(tabelaSelectGenericaOracle1, tabelaSelectGenericaOracle2, relatorio);
		/* Verificacao se nao existe alguma coluna no arquivo 1*/
		verificaSeNaoTemColuna(tabelaSelectGenericaOracle2, tabelaSelectGenericaOracle1, relatorio);
		/* Verificacao divergencia no tamanho das colunas q existem em ambas as tabelas select */
		verificaQtdDeCaracteresDasColunas(tabelaSelectGenericaOracle1, tabelaSelectGenericaOracle2, relatorio);
		/* Verificacao de Registros Diferente da Tabela 2 em Tabela 1 */
		
		verificaRegistroDiferenteSemNSU(tabelaSelectGenericaOracle1, tabelaSelectGenericaOracle2, relatorio);
		/* Verificacao de Registros Diferente da Tabela 1 em Tabela 2 */
		verificaRegistroDiferenteSemNSU(tabelaSelectGenericaOracle2, tabelaSelectGenericaOracle1, relatorio);

		return relatorio;
	}
	
	//Nesse Metodo, o foco ew verificar registros que existe em uma tbela e nao existe na outra.
	private void verificaRegistroInexistenteComNSU(TabelaSelectGenericaOracle tabelaSelectGenericaOracle1, TabelaSelectGenericaOracle tabelaSelectGenericaOracle2, RelatorioComparacaoTabelaSelect relatorio, List<ColunasIguaisIndicesVO> primaryKeys) {
		ListaOcorrenciasMesmoTipoVO listaDiffsNaoContemRegistro1 = new ListaOcorrenciasMesmoTipoVO(tabelaSelectGenericaOracle1.getPathArquivo(),TipoOcorrencia.REGISTROS_INEXISTENTES);
		//Iteracao pelos registros da tabela 2
		for (RegistroDaTabelaVO	registro2 : tabelaSelectGenericaOracle2.getRegistrosDaTabela()) {
			boolean seEncontrouRegistro = false;
			//Inicializa como true, pois se ocorrer uma falha, a variavel ficara como falso.
			for (RegistroDaTabelaVO	registro1 : tabelaSelectGenericaOracle1.getRegistrosDaTabela()) {				
				
				boolean seTodosOsValoresDaColunasPrimaryKeySaoIguais = true;
				
				for (ColunasIguaisIndicesVO colunaPrimaryKeySelecionada : primaryKeys) {
					
					int indicePrimaryKey1 = colunaPrimaryKeySelecionada.getIndiceColuna1();
					int indicePrimaryKey2 = colunaPrimaryKeySelecionada.getIndiceColuna2();
					String conteudoPrimaryKey2 = registro2.getValorColuna(indicePrimaryKey2);
					String conteudoPrimaryKey1 = null;
					
					conteudoPrimaryKey1 = registro1.getValorColuna(indicePrimaryKey1);
					if(conteudoPrimaryKey2.equals(conteudoPrimaryKey1)){
						seTodosOsValoresDaColunasPrimaryKeySaoIguais &= true;						
					}else{
						seTodosOsValoresDaColunasPrimaryKeySaoIguais &= false;
					}
				}
				
				if(seTodosOsValoresDaColunasPrimaryKeySaoIguais){
					seEncontrouRegistro = true;
					break;
				}
			}		
			if(!seEncontrouRegistro){
				listaDiffsNaoContemRegistro1.addOcorrencia("Registro nao encontrado : " + registro2);				
			}
		}
		if(!listaDiffsNaoContemRegistro1.isEmpty()){
			relatorio.addListaDeTodasAsOcorrencias(listaDiffsNaoContemRegistro1);
		}
		/** --- Continuar Daqui para iterar as comparacoes pelas colunas das primaryKeys --- */
		//Iteracao pelos registros da tabela 1
		ListaOcorrenciasMesmoTipoVO listaDiffsNaoContemRegistros2 = new ListaOcorrenciasMesmoTipoVO(tabelaSelectGenericaOracle2.getPathArquivo(),TipoOcorrencia.REGISTROS_INEXISTENTES);
		
		for (RegistroDaTabelaVO	registro1 : tabelaSelectGenericaOracle1.getRegistrosDaTabela()) {
			boolean seEncontrouRegistro = false;
			
			for (RegistroDaTabelaVO	registro2 : tabelaSelectGenericaOracle2.getRegistrosDaTabela()) {				
				boolean seTodosOsValoresDaColunasPrimaryKeySaoIguais = true;
				
				for (ColunasIguaisIndicesVO colunaPrimaryKeySelecionada : primaryKeys) {
					int indicePrimaryKey1 = colunaPrimaryKeySelecionada.getIndiceColuna1();
					int indicePrimaryKey2 = colunaPrimaryKeySelecionada.getIndiceColuna2();
					String conteudoPrimaryKey1 = registro1.getValorColuna(indicePrimaryKey1);
					String conteudoPrimaryKey2 = null;
					
					conteudoPrimaryKey2 = registro2.getValorColuna(indicePrimaryKey2);
					if(conteudoPrimaryKey1.equals(conteudoPrimaryKey2)){
						seTodosOsValoresDaColunasPrimaryKeySaoIguais &= true;						
					}else{
						seTodosOsValoresDaColunasPrimaryKeySaoIguais &= false;
					}
				}
				if(seTodosOsValoresDaColunasPrimaryKeySaoIguais){
					seEncontrouRegistro = true;
					break;
				}				
			}
			if(!seEncontrouRegistro){
				listaDiffsNaoContemRegistros2.addOcorrencia("Registro nao encontrado : " + registro1);				
			}
		}		
		if(!listaDiffsNaoContemRegistros2.isEmpty()){
			relatorio.addListaDeTodasAsOcorrencias(listaDiffsNaoContemRegistros2);
		}
	}
	
	//Neste metodo o foco ew encontrar registros com a mesma primaryKey escolhida e 
	private void verificaRegistroAlteradoComNSU(TabelaSelectGenericaOracle tabelaSelectGenericaOracle1, TabelaSelectGenericaOracle tabelaSelectGenericaOracle2, RelatorioComparacaoTabelaSelect relatorio, List<ColunasIguaisIndicesVO> primaryKeys) {
		ListaOcorrenciasMesmoTipoVO listaDiffsAlteracaoRegistro = new ListaOcorrenciasMesmoTipoVO("\nArquivo 1: " + tabelaSelectGenericaOracle1.getPathArquivo() + "\nArquivo 2: " + tabelaSelectGenericaOracle2.getPathArquivo(),TipoOcorrencia.REGISTROS_DIFERENTES);
		ManipulaStringHelper manipulaStringHelper = new ManipulaStringHelper();
		RegistroDaTabelaVO registroEncontrado1 = null;
		RegistroDaTabelaVO registroEncontrado2 = null;
		for (RegistroDaTabelaVO	registro2 : tabelaSelectGenericaOracle2.getRegistrosDaTabela()) {
			boolean seEncontrouRegistro = false;
			
			for (RegistroDaTabelaVO	registro1 : tabelaSelectGenericaOracle1.getRegistrosDaTabela()) {				
				boolean seTodosOsValoresDaColunasPrimaryKeySaoIguais = true;
				for (ColunasIguaisIndicesVO colunaPrimaryKeySelecionada : primaryKeys) {
					
					int indicePrimaryKey1 = colunaPrimaryKeySelecionada.getIndiceColuna1();
					int indicePrimaryKey2 = colunaPrimaryKeySelecionada.getIndiceColuna2();
					String conteudoPrimaryKey2 = registro2.getValorColuna(indicePrimaryKey2);
					String conteudoPrimaryKey1 = null;
						
					conteudoPrimaryKey1 = registro1.getValorColuna(indicePrimaryKey1);
					if(conteudoPrimaryKey2.equals(conteudoPrimaryKey1)){
						seTodosOsValoresDaColunasPrimaryKeySaoIguais &= true;						
					}else{
						seTodosOsValoresDaColunasPrimaryKeySaoIguais &= false;
					}
				}
				if(seTodosOsValoresDaColunasPrimaryKeySaoIguais){
					seEncontrouRegistro = true;
					registroEncontrado1 = registro1;
					registroEncontrado2 = registro2;
					break;
				}
			}
			if(seEncontrouRegistro){
				if(registroEncontrado2.getlistaRegistro().size() <= registroEncontrado1.getlistaRegistro().size()){
					int qtdColunas2 = registroEncontrado2.getlistaRegistro().size();
					boolean achouDivergencia = false;
					for (int i = 0; i < qtdColunas2 ; i++) {
						if(achouDivergencia){
							break;
						}
						String nomeColuna2 = tabelaSelectGenericaOracle2.getNomesColunas().get(i);
						int qtdColuna1 = registroEncontrado1.getlistaRegistro().size();
						for (int j = 0; j < qtdColuna1 ; j++) {
							String nomeColuna1 = tabelaSelectGenericaOracle1.getNomesColunas().get(j);
							if(manipulaStringHelper.seQsIguais(nomeColuna2, nomeColuna1)){
								String conteudoColuna1 = registroEncontrado1.getlistaRegistro().get(j);
								String conteudoColuna2 = registroEncontrado2.getlistaRegistro().get(i);
								if(!conteudoColuna2.equals(conteudoColuna1)){
									listaDiffsAlteracaoRegistro.addOcorrencia("--- Primary Key (" + primaryKeys.getNomeColunaTexto1() + "): " + registroEncontrado1.getValorColuna(primaryKeys.getIndiceColuna1()) + " ---" + "\nColunas 1: " + tabelaSelectGenericaOracle1.getNomesColunas() + "\nColunas 2: " + tabelaSelectGenericaOracle2.getNomesColunas() + "\nRegistro Arquivo 1: " + registroEncontrado1 + "\nRegistro Arquivo 2: " + registroEncontrado2);
									achouDivergencia = true;
								}								
								break;
							}				
						}
					}					
				}else{
					for (int i = 0; i < registroEncontrado1.getlistaRegistro().size() ; i++) {
						String nomeColuna1 = tabelaSelectGenericaOracle1.getNomesColunas().get(i);
						for (int j = 0; j < registroEncontrado2.getlistaRegistro().size() ; j++) {
							String nomeColuna2 = tabelaSelectGenericaOracle2.getNomesColunas().get(i);
							if(manipulaStringHelper.seQsIguais(nomeColuna1, nomeColuna2)){
								String conteudoColuna1 = registroEncontrado1.getlistaRegistro().get(i);
								String conteudoColuna2 = registroEncontrado2.getlistaRegistro().get(j);
								if(!conteudoColuna1.equals(conteudoColuna2)){
									listaDiffsAlteracaoRegistro.addOcorrencia("--- Primary Key (" + primaryKeys.getNomeColunaTexto1() + "): " + registroEncontrado1.getValorColuna(primaryKeys.getIndiceColuna1()) + " ---" + "\nColunas 1: " + tabelaSelectGenericaOracle1.getNomesColunas() + "\nColunas 2: " + tabelaSelectGenericaOracle2.getNomesColunas() + "\nRegistro Arquivo 1: " + registroEncontrado1 + "\nRegistro Arquivo 2: " + registroEncontrado2);
								}
								break;
							}							
						}
					}
				}
			}		
			if(!listaDiffsAlteracaoRegistro.isEmpty()){
				relatorio.addListaDeTodasAsOcorrencias(listaDiffsAlteracaoRegistro);
			}
		}			
	}
	
	private void verificaRegistroDiferenteSemNSU(TabelaSelectGenericaOracle tabelaSelectGenericaOracle1, TabelaSelectGenericaOracle tabelaSelectGenericaOracle2, RelatorioComparacaoTabelaSelect relatorio) {
		ListaOcorrenciasMesmoTipoVO listaDiffsRegistros = new ListaOcorrenciasMesmoTipoVO(tabelaSelectGenericaOracle1.getPathArquivo(),TipoOcorrencia.REGISTROS_DIFERENTES);
		for (RegistroDaTabelaVO	registro : tabelaSelectGenericaOracle2.getRegistrosDaTabela()) {
			if(!tabelaSelectGenericaOracle1.getRegistrosDaTabela().contains(registro)){
				listaDiffsRegistros.addOcorrencia("Registro nao encontrado: " + registro);
			}
		}
		if(!listaDiffsRegistros.isEmpty()){
			relatorio.addListaDeTodasAsOcorrencias(listaDiffsRegistros);
		}
	}

	private void verificaQtdDeCaracteresDasColunas(TabelaSelectGenericaOracle tabelaSelectGenericaOracle1,
			TabelaSelectGenericaOracle tabelaSelectGenericaOracle2, RelatorioComparacaoTabelaSelect relatorio) {
		ListaOcorrenciasMesmoTipoVO listaDiffsTamanhoColunas1 = new ListaOcorrenciasMesmoTipoVO(tabelaSelectGenericaOracle2.getPathArquivo(),TipoOcorrencia.QTD_CARACTERES_DIFERENTES);
		for (String nomeColuna : tabelaSelectGenericaOracle1.getNomesColunas()) {			
			if(tabelaSelectGenericaOracle2.getNomesColunas().contains(nomeColuna)){
				int indiceTabela1 = tabelaSelectGenericaOracle1.getNomesColunas().indexOf(nomeColuna);
				int indiceTabela2 = tabelaSelectGenericaOracle2.getNomesColunas().indexOf(nomeColuna);
				int qtdTracosTabela1 = tabelaSelectGenericaOracle1.getListaQtdCaracteresRegistro().get(indiceTabela1);
				int qtdTracosTabela2 = tabelaSelectGenericaOracle2.getListaQtdCaracteresRegistro().get(indiceTabela2);
				if(qtdTracosTabela2!=qtdTracosTabela1){
					if(qtdTracosTabela2 > qtdTracosTabela1){
						listaDiffsTamanhoColunas1.addOcorrencia("A Qtd de caracteres da coluna " + nomeColuna + " e maior: (" + qtdTracosTabela2 + " comparado com " + qtdTracosTabela1 + ")");
					}else{
						listaDiffsTamanhoColunas1.addOcorrencia("A Qtd de caracteres da coluna " + nomeColuna + " e menor: (" + qtdTracosTabela2 + " comparado com " + qtdTracosTabela1 + ")");
					}
				}
			}
		}
		if(!listaDiffsTamanhoColunas1.isEmpty()){
			relatorio.addListaDeTodasAsOcorrencias(listaDiffsTamanhoColunas1);
		}
	}
	
	/* Itera pela lista de colunas da 1a tabela e verifica se ela existe na segunda. Caso nao exista, 
	 * grava no relatorio o nome do 2o arquivo e o nome da coluna que nao existe */
	private void verificaSeNaoTemColuna(TabelaSelectGenericaOracle tabela1, TabelaSelectGenericaOracle tabela2, RelatorioComparacaoTabelaSelect relatorio) {
		ListaOcorrenciasMesmoTipoVO listaDiffColunas = new ListaOcorrenciasMesmoTipoVO(tabela2.getPathArquivo(),TipoOcorrencia.COLUNAS_DIVERGENTES);
		for (String nomeColuna : tabela1.getNomesColunas()) {
			if(!containParecido(nomeColuna,tabela2.getNomesColunas())){
				listaDiffColunas.addOcorrencia("Nao contem a coluna " + nomeColuna);
			}
		}
		if(!listaDiffColunas.isEmpty()){
			relatorio.addListaDeTodasAsOcorrencias(listaDiffColunas);
		}
	}
	
	public boolean containParecido(String colunaTab1, List<String> nomesColunasTabela2) {
		ManipulaStringHelper stringHelper = new ManipulaStringHelper();
		if(nomesColunasTabela2.contains(colunaTab1)){
			return true;
		}else{
			for (String colunaTab2 : nomesColunasTabela2) {
				logger.debug("[" + colunaTab1 +"," + colunaTab2 + "] Ew qs Igual? " + stringHelper.seQsIguais(colunaTab1, colunaTab2));
				if(stringHelper.seQsIguais(colunaTab1, colunaTab2)){
					return true;					
				}
			}				
		}
		return false;
	}
	
}
