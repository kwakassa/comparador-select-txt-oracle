package br.gov.caixa.testes;

import java.io.File;

public class TesteLeituraArquivoSelectDaPastaResources {	
	
	public static void main(String[] args) {
		String pathArquivoResource = new TesteLeituraArquivoSelectDaPastaResources().getClass().getClassLoader().getResource("01_SAIDA_MTXTB004_CANAL.txt").getFile();
		System.out.println(pathArquivoResource);
		File arquivoResource = new File(pathArquivoResource);
		System.out.println("Arquivo Existe? " + arquivoResource.exists());
	}
}
