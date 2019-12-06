package br.gov.caixa.testes;

import br.gov.caixa.utils.string.ManipulaStringHelper;

public class TestePorcentagemStringsIgual {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String texto1 = "  aviao pap ";
		String texto2 = " O aviao de papel ";
		ManipulaStringHelper helper = new ManipulaStringHelper();
		
		
		System.out.println("Texto1: \"" + texto1+"\"");
		System.out.println("Texto1 sem espacos: \"" + helper.retiraEspacosEsqDir(texto1) + "\"" );
		System.out.println("Texto2: \""+ texto2 + "\"");
		System.out.println("Texto2 sem espacos: \"" + helper.retiraEspacosEsqDir(texto2) + "\"");
		
		double porcentagem = 0;
		porcentagem = helper.porcentagemIgual(texto1, texto2);
		
		System.out.println("Porcentagem: " + porcentagem * 100 + "% de igualdade");
	}

}
