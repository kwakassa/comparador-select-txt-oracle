package br.gov.caixa.testes;

import br.gov.caixa.utils.string.ManipulaStringHelper;

public class TestaTextoSeParecido {

	public static void main(String[] args) {
		ManipulaStringHelper helper = new ManipulaStringHelper();
		System.out.println("Bola ew qs igual a Bala: " + helper.seQsIguais("Bola", "Bala"));
		System.out.println("Urso ew qs igual a Ursa: " + helper.seQsIguais("Urso", "Ursa"));
		System.out.println("Bolacha ew qs igual a Bala: " + helper.seQsIguais("Bolacha", "Borracha"));
		System.out.println("japones ew qs igual a Japonesa: " + helper.seQsIguais("japones", "Japonesa"));
	}

}
