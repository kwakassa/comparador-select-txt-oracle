package br.edu.utils.helper;

import br.edu.utils.ui.task.ComparaSelectTxtJFXTask;

public class ComparadorTabelasSelectTxtProgessControler {
	
	private ComparaSelectTxtJFXTask task;
	private Double progressoParcial = 0.0;
	private Double progressoTotal = 100.0;

	public ComparadorTabelasSelectTxtProgessControler(ComparaSelectTxtJFXTask task) {
		this.task = task;
	}

	public void atualizaProgresso(Double porcentagemDecimalDoProcesso) {
		progressoParcial += (porcentagemDecimalDoProcesso * 100)/2;//Sao 2 mini processos (Serao 3)		
		task.atualizaProgresso(progressoParcial.longValue(), progressoTotal.longValue());
		task.atualizaMensagem("Processo: "+ progressoParcial);
	}
	
	public void atualizaMensagem(String mensagem) {
		task.atualizaMensagem(mensagem);
	}

	public void zeraProgressoTotal(){
		progressoTotal = 0.0;
	}
	
}
