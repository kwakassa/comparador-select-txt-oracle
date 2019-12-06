package br.edu.utils.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.edu.utils.string.ManipulaStringHelper;
import br.edu.utils.vo.ColunasIguaisIndicesVO;

public class ComparadorHelper {
	private static final Logger logger = Logger.getLogger(ComparadorHelper.class);
	
	public List<ColunasIguaisIndicesVO> getNomesColunasEmComum(List<String> nomesColunasTabela1, List<String> nomesColunasTabela2) {
		List<ColunasIguaisIndicesVO> listaNomesColunasEmComum = new ArrayList<>();
		ManipulaStringHelper stringHelper = new ManipulaStringHelper();
		for (String colunaTab1 : nomesColunasTabela1) {
			if(nomesColunasTabela2.contains(colunaTab1)){
				listaNomesColunasEmComum.add(new ColunasIguaisIndicesVO(colunaTab1, nomesColunasTabela1.indexOf(colunaTab1), colunaTab1, nomesColunasTabela2.indexOf(colunaTab1)));
			}else{
				for (String colunaTab2 : nomesColunasTabela2) {
					logger.debug("[" + colunaTab1 +"," + colunaTab2 + "] Porcentagem igual:" + stringHelper.seQsIguais(colunaTab1, colunaTab2));
					if(stringHelper.seQsIguais(colunaTab1, colunaTab2)){
						listaNomesColunasEmComum.add(new ColunasIguaisIndicesVO(colunaTab1, nomesColunasTabela1.indexOf(colunaTab1), colunaTab2, nomesColunasTabela2.indexOf(colunaTab2)));
						break;
					}
				}				
			}
		}
		logger.debug("listaNomesColunasEmComum: " + listaNomesColunasEmComum);
		return listaNomesColunasEmComum;
	}
}
