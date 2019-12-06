package br.gov.caixa.testes;

public class TestaCastLongToDouble {
	public static void main(String[] args) {
		Long l1 = 2L;
		Long l2 = 3L;
		Double d1 = ((double)l1/(double)l2);
		System.out.println(d1);
	}
}
