package br.edu.utils.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.apache.log4j.Logger;

import br.edu.interfaces.MainJfxUiInterface;
import br.edu.utils.helper.MemoriaJVMHelper;
import br.edu.utils.vo.ColunasIguaisIndicesVO;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SelecionaPkJFxUI extends Application {

	private static final Logger logger = Logger.getLogger(SelecionaPkJFxUI.class);
	private Stage stage;
	private int windowWidth = 600;
	private int windowHeight = 480;
	private int paddingLeft = 10;
	private int paddingTop = 35;
	private int paddingSpace = 5;
	private String titulo = "Selecione o campo da Chave Primaria (Primary Key)";
	private List<ColunasIguaisIndicesVO> listaNomesCampos = new ArrayList<>();
	private List<CheckBox> listaCheckBox = new ArrayList<>();
	private AnchorPane pane;
	private Button testeButton;
	private Button confirmaButton;
	private SelecionaPkJFxUI thisUI;
	private boolean modoTeste = false;
	private long contTesteCampo = 0;
	private List<ColunasIguaisIndicesVO> primaryKeys =  null;
	private MainJfxUiInterface parentUi = null;
	private Stage thisStage = null;

	@Override
	public void start(Stage stage) throws Exception {
		this.thisUI = this;
		this.thisStage = stage;
		/* --- Inicializa Sysouts iniciais --- */
		iniStatusDaAplicacao();
		/* --- Inicializa Componentes --- */
		iniComponentes(stage);
		/* --- Mostra a Interface UI --- */
		this.stage.show();
		/* --- Posicao dos Components --- */
		iniPosicaoComponentes();
		/* --- Inicializacao dos Evento dos Componentes --- */
		initEvents();
	}

	private void iniStatusDaAplicacao() {
		this.modoTeste = false;
		logger.info("Versao do Java: " + System.getProperty("java.version"));
		logger.debug(MemoriaJVMHelper.imprimeStatusMemoria(MemoriaJVMHelper.PRINT_TYPE_CUSTOM));
	}

	private void iniComponentes(Stage stage) {
		/* --- Configuracao Inicial --- */
		this.stage = stage;
		pane = new AnchorPane();
		pane.setPrefSize(windowWidth, windowHeight);
		testeButton = new Button("Teste");
		
		if(modoTeste){
			testeButton.setVisible(true);
		}else{
			testeButton.setVisible(false);
		}
		confirmaButton = new Button("Confirmar");
		pane.getChildren().addAll(testeButton,confirmaButton);
		initFakeRadioButton();
		/* --- Atribuicao Final e Titulo --- */
		Scene scene = new Scene(pane);
		this.stage.setScene(scene);
		this.stage.setTitle(titulo);
	}

	private void initFakeRadioButton() {		
		List<ColunasIguaisIndicesVO> listaCampos = new ArrayList<>();
		listaCampos.add(new ColunasIguaisIndicesVO("Teste 1", 1, "Teste 1", 1));
		listaCampos.add(new ColunasIguaisIndicesVO("Teste 2", 2, "Teste 2", 2));
		listaCampos.add(new ColunasIguaisIndicesVO("Teste 3", 3, "Teste 3", 3));
		thisUI.setListaNomesCampos(listaCampos);
	}

	private void atualizaCheckBoxs(List<ColunasIguaisIndicesVO> listaNomeCampos) {
		logger.debug("Tamanho Lista Nome Campos: " + listaNomeCampos.size());
		//Remove os Radio Buttons Anteriores
		if(listaCheckBox != null){
			for (CheckBox checkBox : listaCheckBox) {
				pane.getChildren().removeAll(checkBox);
			}			
		}
		listaCheckBox = new ArrayList<>();
		double layoutXcontroller = paddingLeft;
		double layoutYcontroller = paddingTop + 30;
		logger.debug("layoutXcontroller: " + layoutXcontroller);
		logger.debug("testeButton.getLayoutY(): " + testeButton.getLayoutY());
		logger.debug("testeButton.getHeight(): " + testeButton.getHeight());
		logger.debug("layoutYcontroller: " + layoutYcontroller);
		//Inclui os Novos Radio Buttons
		for (ColunasIguaisIndicesVO campo : listaNomesCampos) {
			CheckBox checkBox = new CheckBox(campo.getNomeColunaTexto1());
			//radioButton.setToggleGroup(toggleGroup);
			listaCheckBox.add(checkBox);
			pane.getChildren().addAll(checkBox);
			checkBox.setLayoutX(layoutXcontroller);
			checkBox.setLayoutY(layoutYcontroller);
			layoutYcontroller = checkBox.getLayoutY() + checkBox.getHeight() + 30;
		}

	}

	private void iniPosicaoComponentes() {
		confirmaButton.setLayoutX(10);
		confirmaButton.setLayoutY(10);
		testeButton.setLayoutX(confirmaButton.getLayoutX() + confirmaButton.getWidth() + 10);
		testeButton.setLayoutY(10);
	}

	private void initEvents() {
		// listen to changes in selected toggle		
		testeButton.setOnAction(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent action) {
				List<ColunasIguaisIndicesVO> listaCampos = new ArrayList<>();
				listaCampos.add(new ColunasIguaisIndicesVO("Campo 1", 1, "Campo 1", 1));
				listaCampos.add(new ColunasIguaisIndicesVO("Campo 2", 2, "Campo 2", 2));
				listaCampos.add(new ColunasIguaisIndicesVO("Campo 3", 3, "Campo 3", 3));
				thisUI.setListaNomesCampos(listaCampos);				
			}
		});
		confirmaButton.setOnAction(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent action) {
				List<ColunasIguaisIndicesVO> listaDePrimaryKeys = getListaDeCamposPrimaryKeys();
				parentUi.continuaExecucaoDeComparacao(listaDePrimaryKeys);
				thisStage.close();
			}			
		});
	}

	public void setListaNomesCampos(List<ColunasIguaisIndicesVO> listaNomesCampos) {
		this.listaNomesCampos = listaNomesCampos;
		atualizaCheckBoxs(this.listaNomesCampos);
	}
	
	public void setVisible(boolean visible){
		this.setVisible(visible);
	}
	
	public List<ColunasIguaisIndicesVO> geyPrimaryKey() {		
		return primaryKeys;
	}
	
	public void setParentUi(MainJfxUiInterface parentUi) {
		this.parentUi = parentUi;
	}
	
	public static void main(String[] args) throws Exception {
		launch(args);
	}
	
	private List<ColunasIguaisIndicesVO> getListaDeCamposPrimaryKeys() {
		List<ColunasIguaisIndicesVO> subListaSelecionadaDeColunas = new ArrayList<>();
		for (CheckBox checkBox : listaCheckBox) {
			if(checkBox.isSelected()){
				ColunasIguaisIndicesVO colunasIguaisIndicesVO = getColunasIguaisIndicesVODaLista(checkBox);
				if(colunasIguaisIndicesVO!=null){
					subListaSelecionadaDeColunas.add(colunasIguaisIndicesVO);
				}
			}
		}
		return subListaSelecionadaDeColunas;			
	}
	
	private ColunasIguaisIndicesVO getColunasIguaisIndicesVODaLista(CheckBox checkBox){
		for (ColunasIguaisIndicesVO colunasIguaisIndicesVO : listaNomesCampos) {
			if(colunasIguaisIndicesVO.getNomeColunaTexto1().equals(checkBox.getText())){
				return colunasIguaisIndicesVO;
			}
		}
		return null;
	}
	
}
