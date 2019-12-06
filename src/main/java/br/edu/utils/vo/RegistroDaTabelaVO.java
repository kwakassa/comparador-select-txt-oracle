package br.edu.utils.vo;

import java.util.List;

public class RegistroDaTabelaVO {
	private List<String> registro;

	public RegistroDaTabelaVO(List<String> registro) {
		this.registro = registro;
	}

	@Override
	public boolean equals(Object registroDaTabelaVO) {
		
		if(registroDaTabelaVO instanceof RegistroDaTabelaVO){
			List<String> registroCast = ((RegistroDaTabelaVO)registroDaTabelaVO).getlistaRegistro();
			for (int i = 0; i < registroCast.size() ; i++) {
				if(!registroCast.get(i).equals(this.registro.get(i))){
					return false;
				}
			}			
			return true;
		}else{
			return false;
		}
	}
	
	public String getValorColuna(int numColuna){
		return registro.get(numColuna);
	}

	public List<String> getlistaRegistro(){
		return this.registro;
	}
	
	@Override
	public String toString() {
		return "RegistroDaTabela [registro=" + registro + "]";
	}
	
	
}
