package br.edu.utils.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RelatorioComparacaoTabelaSelect {
	private List<ListaOcorrenciasMesmoTipoVO> listaDeTodasAsOcorrencias = new ArrayList<>();

	public List<ListaOcorrenciasMesmoTipoVO> getListaDeTodasAsOcorrencias() {
		return listaDeTodasAsOcorrencias;
	}

	public void addListaDeTodasAsOcorrencias(ListaOcorrenciasMesmoTipoVO listaDeTodasAsOcorrencias) {
		this.listaDeTodasAsOcorrencias.add(listaDeTodasAsOcorrencias);
	}
	
	public String getConteudoRelatorio(){
		if(listaDeTodasAsOcorrencias.isEmpty()){
			return "N\u00E3o h\u00E1 ocorrencias";
		}else{
			StringBuilder conteudoRelatorio = new StringBuilder();
			for (ListaOcorrenciasMesmoTipoVO listaOcorrenciasMesmoTipoVO : listaDeTodasAsOcorrencias) {
				if(!listaOcorrenciasMesmoTipoVO.isEmpty()){
					conteudoRelatorio.append("Ocorrencia no arquivo: " + listaOcorrenciasMesmoTipoVO.getNomeArquivoSelect() + "\n");
					conteudoRelatorio.append("Tipo de ocorrencia: " + listaOcorrenciasMesmoTipoVO.getTipoOcorrenciaDesc() + "\n");					
					for ( String ocorrencia : listaOcorrenciasMesmoTipoVO.getOcorrencias()) {
						conteudoRelatorio.append(ocorrencia + "\n");
					}
					conteudoRelatorio.append("\n");
				}
			}
			return conteudoRelatorio.toString();
		}
	}
	
	public boolean isEmpty(){
		return listaDeTodasAsOcorrencias.isEmpty();
	}
	
}
