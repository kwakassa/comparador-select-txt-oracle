package br.edu.utils.vo;

public class ColunasIguaisIndicesVO {
	
	private String nomeColunaTexto1 = null;
	private int indiceColuna1 = 0;
	private String nomeColunaTexto2 = null;
	private int indiceColuna2 = 0;
	public ColunasIguaisIndicesVO(String nomeColunaTexto1, int indiceColuna1, String nomeColunaTexto2, int indiceColuna2) {
		this.nomeColunaTexto1 = nomeColunaTexto1;
		this.indiceColuna1 = indiceColuna1;
		this.nomeColunaTexto2 = nomeColunaTexto2;
		this.indiceColuna2 = indiceColuna2;
	}
	public String getNomeColunaTexto1() {return nomeColunaTexto1;}
	public int getIndiceColuna1() {return indiceColuna1;}
	public String getNomeColunaTexto2() {return nomeColunaTexto2;}
	public int getIndiceColuna2() {return indiceColuna2;}
	
}
