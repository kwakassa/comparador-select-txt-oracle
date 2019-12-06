package br.gov.caixa.testes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import br.gov.caixa.utils.vo.TabelaSelectGenericaOracle;

public class TesteLeituraDeArquivoDaPastaResourcesEObtencaoTabelaGenerica {	
	private static final Logger logger = Logger.getLogger(TesteLeituraDeArquivoDaPastaResourcesEObtencaoTabelaGenerica.class);
	public static void main(String[] args) throws FileNotFoundException {
		/* Leitura do Arquivo de exemplo da Pasta Resource */
		String pathArquivoResource = new TesteLeituraDeArquivoDaPastaResourcesEObtencaoTabelaGenerica().getClass().getClassLoader().getResource("02_SAIDA_MTXTB001_SERVICO.txt").getFile();
		logger.info(pathArquivoResource);
		File arquivoResource = new File(pathArquivoResource);
		logger.info("Arquivo Existe? " + arquivoResource.exists());
		/* Obtencao dos Nomes das Colunas */
		TabelaSelectGenericaOracle tabelaGenerica = new TabelaSelectGenericaOracle();
		Scanner leitor = new Scanner(arquivoResource);
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
		tabelaGenerica.setNomesColunas(listaColunas);
		/* A leitura dessa linha deve ser imediatamente apos a leitura das colunas */
		if(leitor.hasNext()){
			linha = leitor.nextLine();
			tabelaGenerica.setQtdCaracteresRegistro(linha);// Supondo ser a linha com os tracos '----'
			logger.info(tabelaGenerica.getQtdCaracteresRegistro());
			logger.debug(tabelaGenerica.getTamanhoDaLinha());
		}
		while(leitor.hasNext()){
			linha = leitor.nextLine();
			logger.debug("tamanho da linha: " + linha.length());
			tabelaGenerica.setLinhaRegistro(linha);
		}
		logger.debug(tabelaGenerica.getRegistrosDaTabela());
		leitor.close();
	}
}
