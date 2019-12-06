package br.edu.utils.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import br.edu.utils.vo.TabelaSelectGenericaOracle;

public class ConversorSelectTxtToObject {
	private static final Logger logger = Logger.getLogger(ConversorSelectTxtToObject.class);
	
	public TabelaSelectGenericaOracle selectTxtToList(File arquivoSelectTxt) throws FileNotFoundException{
		TabelaSelectGenericaOracle tabelaGenerica = new TabelaSelectGenericaOracle();
		Scanner leitor = new Scanner(arquivoSelectTxt);
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
		leitor.close();
		return tabelaGenerica;
	}
	
}
