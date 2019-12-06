package br.gov.caixa.testes;

public class TesteRetirarEspacosInicialEFinalDaString {

	public static void main(String[] args) {
		String texto = "     Esse ew um texto com espacos inicial e final.     ";
		System.out.println("Antes: [" + texto + "]");
		System.out.println("Depois: [" + texto.trim() + "]");

	}

}
