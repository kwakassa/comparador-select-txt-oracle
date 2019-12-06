package br.edu.utils.vo;

import java.util.Set;
import java.util.TreeSet;

public class ListaOcorrenciasMesmoTipoVO {
	private String nomeArquivoSelect;
	private TipoOcorrencia tipoOcorrencia;
	private Set<String> ocorrencias;
	
	public ListaOcorrenciasMesmoTipoVO(String nomeArquivoSelect, TipoOcorrencia tipoOcorrencia) {
		if(!nomeArquivoSelect.isEmpty()){
			this.nomeArquivoSelect = nomeArquivoSelect;
		}else{
			throw new IllegalArgumentException("Nome do arquivo informado vazio");
		}
		this.tipoOcorrencia = tipoOcorrencia;
		ocorrencias = new TreeSet<>();
	}
	
	public String getNomeArquivoSelect() {return nomeArquivoSelect;}
	public TipoOcorrencia getTipoOcorrencia() {return tipoOcorrencia;}
	public String getTipoOcorrenciaDesc() {
		switch (tipoOcorrencia) {
			case COLUNAS_DIVERGENTES:
				return "Colunas Divergentes";
			case QTD_CARACTERES_DIFERENTES:
				return "Quantidade de Caracteres de Coluna Diferentes";
			case REGISTROS_DIFERENTES:
				return "Registros Diferentes";
			case REGISTROS_INEXISTENTES:
				return "Registros Inexistentes";
			default:
				return"Tipo Indefinido";
		}
		
	}
	public Set<String> getOcorrencias() {return ocorrencias;}
	
	public void addOcorrencia(String ocorrencia){
		ocorrencias.add(ocorrencia);
	}
	
	public boolean isEmpty(){
		return ocorrencias.isEmpty();
	}
}
