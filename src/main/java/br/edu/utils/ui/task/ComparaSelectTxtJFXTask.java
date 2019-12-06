package br.edu.utils.ui.task;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import br.edu.utils.helper.TabelaSelectGenericaOracleHelper;
import br.edu.utils.vo.ColunasIguaisIndicesVO;
import br.edu.utils.vo.RelatorioComparacaoTabelaSelect;
import br.edu.utils.vo.TabelaSelectGenericaOracle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

public class ComparaSelectTxtJFXTask extends Task<Void>{
	private static final Logger logger = Logger.getLogger(ComparaSelectTxtJFXTask.class);	
	private TabelaSelectGenericaOracleHelper tabelaSelectGenericaOracleHelper = null;
	private File arquivoSelectOracle1 = null;
	private File arquivoSelectOracle2 = null;
	private TextArea textArea = null;
	private List<ColunasIguaisIndicesVO> primaryKeys = null;
	private TabelaSelectGenericaOracle tabelaSelectGenericaOracle1;
	private TabelaSelectGenericaOracle tabelaSelectGenericaOracle2;

	public List<ColunasIguaisIndicesVO> getPrimaryKey() {return primaryKeys;}
	public ComparaSelectTxtJFXTask setPrimaryKeys(List<ColunasIguaisIndicesVO> primaryKeys) {this.primaryKeys = primaryKeys;return this;}

	public ComparaSelectTxtJFXTask setTabelaSelectGenericaOracleHelper(TabelaSelectGenericaOracleHelper tabelaSelectGenericaOracleHelper) {
		if(tabelaSelectGenericaOracleHelper!=null){
			this.tabelaSelectGenericaOracleHelper = tabelaSelectGenericaOracleHelper;
			return this;
		}else{
			throw new NullPointerException("Objeto TabelaSelectGenericaOracleHelper nulo");
		}
	}
	
	public ComparaSelectTxtJFXTask setArquivoSelectOracle1(File arquivoSelectOracle1){
		if(arquivoSelectOracle1.isFile()){
			this.arquivoSelectOracle1 = arquivoSelectOracle1;
			return this;
		}else{
			throw new NullPointerException("Objeto ArquivoSelectOracle1 esta nulo");
		}
		
	}
	
	public ComparaSelectTxtJFXTask setArquivoSelectOracle2(File arquivoSelectOracle2){
		if(arquivoSelectOracle2.isFile()){
			this.arquivoSelectOracle2 = arquivoSelectOracle2;
			return this;
		}else{
			throw new NullPointerException("Objeto ArquivoSelectOracle2 esta nulo");
		}		
	}
	
	public ComparaSelectTxtJFXTask setTextArea(TextArea textArea){
		if(textArea!=null){
			this.textArea = textArea;
			return this;
		}else{
			throw new NullPointerException("Objeto textArea esta nulo");
		}		
	}
	
	@Override
	protected Void call() throws Exception {
		try {
			/* Task tem duas property interessantes para usar junto a um ProgressBar
			a messageProperty, que pode ser ligada a outra StringProperty
			para transmitir uma mensagem, e a progressProperty, que serve para mandar 
			valores númericos a uma ProgressBar ou ProgressIndicator */
			/* Bloco de Processamento da Tarefa */
			if(tabelaSelectGenericaOracleHelper!=null && arquivoSelectOracle1!=null && arquivoSelectOracle2!=null && textArea!=null){

				tabelaSelectGenericaOracle1 = tabelaSelectGenericaOracleHelper.getTabelaGenericaDeArquivoSelectTxtOracle(arquivoSelectOracle1);
				tabelaSelectGenericaOracle2 = tabelaSelectGenericaOracleHelper.getTabelaGenericaDeArquivoSelectTxtOracle(arquivoSelectOracle2);
				logger.debug("--- Tabela 1 ---");
				logger.debug(tabelaSelectGenericaOracle1);
				logger.debug("--- Tabela 2 ---");
				logger.debug(tabelaSelectGenericaOracle2);
				RelatorioComparacaoTabelaSelect relatorio = tabelaSelectGenericaOracleHelper.comparaTabelasSelect(tabelaSelectGenericaOracle1, tabelaSelectGenericaOracle2, primaryKeys);
				this.atualizaProgresso(100, 100);
				this.atualizaMensagem("Processo: 100");
				logger.debug("Relatorio: " + relatorio.getConteudoRelatorio());
				Platform.runLater( () -> textArea.setText("" + relatorio.getConteudoRelatorio()) );
				logger.debug("textArea: " + textArea.getText());
			}else{
				throw new NullPointerException("TabelaSelectGenericaOracleHelper, arquivoSelectOracle1 ou arquivoSelectOracle1 Nulo. Use os sets para fornecer os atributos necessarios.");
			}		
			/* Fim do Bloco de Processamento */        
			return null;
		} catch (Exception e) {
			logger.error("",e);
			return null;
		}
	}
	
	public TabelaSelectGenericaOracle getTabelaSelectGenericaOracle1() {return tabelaSelectGenericaOracle1;}
	public void setTabelaSelectGenericaOracle1(TabelaSelectGenericaOracle tabelaSelectGenericaOracle1) {this.tabelaSelectGenericaOracle1 = tabelaSelectGenericaOracle1;}
	public TabelaSelectGenericaOracle getTabelaSelectGenericaOracle2() {return tabelaSelectGenericaOracle2;}
	public void setTabelaSelectGenericaOracle2(TabelaSelectGenericaOracle tabelaSelectGenericaOracle2) {this.tabelaSelectGenericaOracle2 = tabelaSelectGenericaOracle2;}
	
	public void atualizaProgresso(long valor, long maxValor){
		super.updateProgress(valor, maxValor);
	}	
	
	public void atualizaMensagem(String message) {
		super.updateMessage(message);
	}

}
