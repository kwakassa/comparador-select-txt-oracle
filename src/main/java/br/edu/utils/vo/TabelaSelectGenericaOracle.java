package br.edu.utils.vo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class TabelaSelectGenericaOracle {
	private static final Logger logger = Logger.getLogger(TabelaSelectGenericaOracle.class);
	private String pathArquivo = null; 
	private List<String> nomesColunas = null;
	private List<RegistroDaTabelaVO> registrosDaTabela = null;
	private BigInteger qtdColunas = BigInteger.ZERO;
	private BigInteger qtdRegistros = BigInteger.ZERO;
	private BigInteger tamanhoDaLinha = BigInteger.ZERO;
	private List<Integer> listaQtdCaracteresRegistro = null;
	
	public List<Integer> getQtdCaracteresRegistro() {return listaQtdCaracteresRegistro;}	
	public List<Integer> getListaQtdCaracteresRegistro() {return listaQtdCaracteresRegistro;}
	public void setListaQtdCaracteresRegistro(List<Integer> listaQtdCaracteresRegistro) {this.listaQtdCaracteresRegistro = listaQtdCaracteresRegistro;}
	public BigInteger getTamanhoDaLinha() {return tamanhoDaLinha;}
	public String getPathArquivo() {return pathArquivo;}
	public void setPathArquivo(String pathArquivo) {this.pathArquivo = pathArquivo;}
	
	/* Usar esse metodo antes do setLinhaRegistro() */
	public void setQtdCaracteresRegistro(String linhaComTracosDoSelect) {
		/* Exemplo da linha sem colchetes: [---- ------- ------- ------]*/
		String[] tracos = linhaComTracosDoSelect.trim().split(" ");
		this.listaQtdCaracteresRegistro = new ArrayList<>();
		for (String traco : tracos) {
			this.listaQtdCaracteresRegistro.add(traco.length());
		}
		tamanhoDaLinha = BigInteger.ZERO;
		for (Integer tamanhoRegistro : listaQtdCaracteresRegistro) {
			tamanhoDaLinha = tamanhoDaLinha.add(new BigInteger(""+tamanhoRegistro));
		}
		if(this.listaQtdCaracteresRegistro.size() != nomesColunas.size()){
			throw new RuntimeException("tamanho da listaQtdCaracteresRegistro é diferente da qtd de colunas");
		}else{
			registrosDaTabela = new ArrayList<>();
		}
	}
	/* Usar o metodo setLinhaRegistro() antes de usar fazer o loop para inserir os registros */
	public void setLinhaRegistro(String linhaComRegistroPosicional) {
		Integer proxPosicao = 0;
		Integer posicaoInicial = 0;
		Integer posicaoFinal = 0;
		if(linhaComRegistroPosicional.length() >= tamanhoDaLinha.longValue()){
			List<String> registros = new ArrayList<>();
			for (Integer tamanhoRegistro : listaQtdCaracteresRegistro) {
				posicaoInicial = proxPosicao;
				posicaoFinal = (Integer)(posicaoInicial+tamanhoRegistro);
				String registro = linhaComRegistroPosicional.substring(posicaoInicial, posicaoFinal);
				registros.add(registro.trim());
				proxPosicao = posicaoInicial + tamanhoRegistro + 1;
			}
			registrosDaTabela.add(new RegistroDaTabelaVO(registros));
			logger.debug(registros);
		}
	}
	
	
	public void setQtdCaracteresRegistro(List<Integer> listaQtdCaracteresRegistro) {
		if (!listaQtdCaracteresRegistro.isEmpty() && listaQtdCaracteresRegistro.size() == qtdColunas.intValue()) {			
			this.listaQtdCaracteresRegistro = listaQtdCaracteresRegistro;
		}else{
			throw new RuntimeException("Lista Vazia ou tamanho da lista diferente da Qtd de Colunas");
		}
	}

	public List<String> getNomesColunas() {return nomesColunas;}
	
	public void setNomesColunas(String[] colunas){
		if (colunas.length>0) {
			List<String> listaColunas = new ArrayList<>();
			this.qtdColunas = BigInteger.ZERO;
			for (String coluna : colunas) {
				if(!coluna.isEmpty()){
					listaColunas.add(coluna);
					this.qtdColunas = this.qtdColunas.add(BigInteger.ONE);
				}
			}
			this.nomesColunas = listaColunas;			
		}
	}
	
	public void setNomesColunas(List<String> nomesColunas) {
		if(!nomesColunas.isEmpty()){
			this.nomesColunas = nomesColunas;
			qtdColunas = new BigInteger(""+nomesColunas.size());
		}
	}
	
	public List<RegistroDaTabelaVO> getRegistrosDaTabela() {return registrosDaTabela;}
	public void setRegistrosDaTabela(List<RegistroDaTabelaVO> registrosDaTabela) {
		if (!registrosDaTabela.isEmpty()) {
			this.registrosDaTabela = registrosDaTabela;
			qtdRegistros = new BigInteger(""+registrosDaTabela.size());
		}
	}
	
	public BigInteger getQtdColunas() {return qtdColunas;}
	public BigInteger getQtdRegistros() {return qtdRegistros;}

	@Override
	public String toString() {
		return "TabelaSelectGenericaOracle [nomesColunas=" + nomesColunas + ", registrosDaTabela=" + registrosDaTabela
				+ ", qtdColunas=" + qtdColunas + ", qtdRegistros=" + qtdRegistros + ", tamanhoDaLinha=" + tamanhoDaLinha
				+ ", listaQtdCaracteresRegistro=" + listaQtdCaracteresRegistro + "]";
	}	
	
	
	
}
