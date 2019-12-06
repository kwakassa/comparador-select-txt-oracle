package br.edu.utils.ui.main;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import br.edu.interfaces.MainJfxUiInterface;
import br.edu.utils.helper.ComparadorHelper;
import br.edu.utils.helper.ConstantesUtils;
import br.edu.utils.helper.IniPathHelper;
import br.edu.utils.helper.MemoriaJVMHelper;
import br.edu.utils.helper.TabelaSelectGenericaOracleHelper;
import br.edu.utils.ui.SelecionaPkJFxUI;
import br.edu.utils.ui.task.ComparaSelectTxtJFXTask;
import br.edu.utils.vo.ColunasIguaisIndicesVO;
import br.edu.utils.vo.TabelaSelectGenericaOracle;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ComparadorTabelasSelectTxtMainUI extends Application implements MainJfxUiInterface{
	
	private static final Logger logger = Logger.getLogger(ComparadorTabelasSelectTxtMainUI.class);
	private Stage stage;
	private int windowWidth = 600;
	private int windowHeight = 480;
	private int screenWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	private int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	private double paneWidthAntes;
	private double paneHeightAntes;
	private int paddingLeft = 20;
	private int paddingTop = 10;
	private int paddingSpace = 5;
	private String titulo = "CEDESSP672 - Comparador de Arquivos Select TXT";
	private Label arquivoSelectTxtLbl1;
	private Button arquivoSelectTxtBtn1;
	private Label arquivoSelectTxtLbl2;
	private Button arquivoSelectTxtBtn2;
	private FileChooser fileChooserSelectTxt1;
	private FileChooser fileChooserSelectTxt2;
	private FileChooser fileChooserSalvarRelatorio;
	private File arquivoSelectTxt1;
	private File arquivoSelectTxt2;
	private File arquivoSalvarRelatorio;
	private TabelaSelectGenericaOracle tabelaSelectGenericaOracle1 = null;
	private TabelaSelectGenericaOracle tabelaSelectGenericaOracle2 = null;
	private IniPathHelper iniHelper;
	private Button compararBtn;
	private TextArea comparacaoTA;
	private VBox comparacaoTAVB;
	private Button salvarRelatorioBtn;
	private Label status;
	private ProgressBar barra;
	private TabelaSelectGenericaOracleHelper tabelaSelectGenericaOracleHelper;
	private SelecionaPkJFxUI selecionaPkJFxUI = null;
	private List<ColunasIguaisIndicesVO> primaryKeys = null;
	private boolean modoTeste = false;
	private Stage thisStage = null;
	private MainJfxUiInterface thisUI = null;
	private Stage selecionaPkUIStage = null;
	private AnchorPane pane;
	
	@Override
	public void start(Stage stage) throws Exception {
		thisStage = stage;
		thisUI = this;
		/* --- Inicializa Sysouts iniciais --- */
		iniStatusDaAplicacao();
		/* --- Inicializa Componentes --- */
		iniComponentes(stage);
		/* --- Inicializa Helpers --- */
		iniHelpers();
		/* --- Mostra a Interface UI --- */
		this.stage.show();
		/* --- Posicao dos Components --- */
		iniPosicaoComponentes();
		/* --- Inicializacao dos Evento dos Componentes --- */
		initEvents();		
	}
	
	private void iniStatusDaAplicacao() {
		this.modoTeste = true;
		logger.info("Versao do Java: " + System.getProperty("java.version"));
		logger.debug(MemoriaJVMHelper.imprimeStatusMemoria(MemoriaJVMHelper.PRINT_TYPE_CUSTOM));
		logger.debug("Screen size: " + screenWidth + "x" + screenHeight);
	}

	private void iniComponentes(Stage stage) {
		/* --- Configuracao Inicial --- */
		this.stage = stage;
		pane = new AnchorPane();
		pane.setPrefSize(windowWidth, windowHeight);
		arquivoSelectTxtLbl1 = new Label("Selecione o 1o arquivo de select TXT do Oracle: ");
		arquivoSelectTxtBtn1 = new Button("Selecione o Select TXT 1");
		arquivoSelectTxtLbl2 = new Label("Selecione o 2o arquivo de select TXT do Oracle: ");
		arquivoSelectTxtLbl2.setTooltip(new Tooltip("Escolha arquivos select txt da mesma tabela"));
		arquivoSelectTxtBtn2 = new Button("Selecione o Select TXT 2");
		compararBtn = new Button("Comparar");
		salvarRelatorioBtn = new Button("Salvar");
		status = new Label("Status do Processo");
		barra = new ProgressBar();
		comparacaoTA = new TextArea();
		comparacaoTAVB = new VBox(comparacaoTA);
		comparacaoTAVB.setVgrow(comparacaoTA, Priority.SOMETIMES);
		comparacaoTA.setPromptText("Resultado da Compara\u00E7\u00E3o");
		comparacaoTA.setEditable(false);
		/* Adicionar no metodo addAll(), abaixo, os componentes separados com "," */
		pane.getChildren().addAll(arquivoSelectTxtLbl1,arquivoSelectTxtBtn1,arquivoSelectTxtLbl2,arquivoSelectTxtBtn2,compararBtn,salvarRelatorioBtn,status,barra,comparacaoTAVB);
		/* --- Atribuicao Final e Titulo --- */
		Scene scene = new Scene(pane);
		this.stage.setScene(scene);
		this.stage.setTitle(titulo);
	}

	private void iniHelpers() {
		novoFileChooserSelectTxt1();
		novoFileChooserSelectTxt2();
		novoFileChooserSalvarRelatorio();
		tabelaSelectGenericaOracleHelper = new TabelaSelectGenericaOracleHelper();
		selecionaPkJFxUI = new SelecionaPkJFxUI();
		try {
			Map<String,String> listaValoresPadrao = new HashMap<>();
			listaValoresPadrao.put(ConstantesUtils.PATH_ARQUIVO_SELECT_TXT_1, "");
			listaValoresPadrao.put(ConstantesUtils.PATH_ARQUIVO_SELECT_TXT_2, "");
			listaValoresPadrao.put(ConstantesUtils.PATH_ARQUIVO_SALVAR_RELATORIO, "");
			iniHelper = new IniPathHelper(ConstantesUtils.NOME_ARQUIVO_INI_VALIDADOR_XML, listaValoresPadrao);
		} catch (IOException e) {
			logger.error("Erro na inicializa\u00E7\u00E3o do arquivo de configura\u00E7\u00E3o INI",e);
		}
	}

	private void carregaInfoIniTxt2() {
		arquivoSelectTxt2 = new File(iniHelper.getPropertyValue(ConstantesUtils.PROFILE_CONFIG, ConstantesUtils.PATH_ARQUIVO_SELECT_TXT_2));
		if(arquivoSelectTxt2.isFile()){
			fileChooserSelectTxt2.setInitialDirectory(arquivoSelectTxt2.getParentFile());
		}
	}

	private void carregaInfoIniTxt1() {
		arquivoSelectTxt1 = new File(iniHelper.getPropertyValue(ConstantesUtils.PROFILE_CONFIG, ConstantesUtils.PATH_ARQUIVO_SELECT_TXT_1));
		if(arquivoSelectTxt1.isFile()){
			fileChooserSelectTxt1.setInitialDirectory(arquivoSelectTxt1.getParentFile());
		}
	}
	
	private void carregaInfoSalvarRelatorioIni() {
		arquivoSalvarRelatorio = new File(iniHelper.getPropertyValue(ConstantesUtils.PROFILE_CONFIG, ConstantesUtils.PATH_ARQUIVO_SALVAR_RELATORIO));
		if(arquivoSalvarRelatorio.isFile()){
			fileChooserSalvarRelatorio.setInitialDirectory(arquivoSalvarRelatorio.getParentFile());
		}
	}
	
	private void novoFileChooserSelectTxt2() {
		fileChooserSelectTxt2 = new FileChooser();
		fileChooserSelectTxt2.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
	}

	private void novoFileChooserSelectTxt1() {
		fileChooserSelectTxt1 = new FileChooser();
		fileChooserSelectTxt1.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
	}
	
	private void novoFileChooserSalvarRelatorio() {
		fileChooserSalvarRelatorio = new FileChooser();
		fileChooserSalvarRelatorio.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
	}

	private void iniPosicaoComponentes() {
		arquivoSelectTxtLbl1.setLayoutX(paddingLeft);
		arquivoSelectTxtLbl1.setLayoutY(paddingTop + ((arquivoSelectTxtBtn1.getHeight()-arquivoSelectTxtLbl1.getHeight())/2));
		arquivoSelectTxtBtn1.setLayoutX(paddingLeft + arquivoSelectTxtLbl1.getWidth());
		arquivoSelectTxtBtn1.setLayoutY(paddingTop);
		arquivoSelectTxtLbl2.setLayoutX(paddingLeft);
		arquivoSelectTxtLbl2.setLayoutY(paddingTop + arquivoSelectTxtBtn1.getHeight() + paddingTop + ((arquivoSelectTxtBtn2.getHeight()-arquivoSelectTxtLbl2.getHeight())/2));
		arquivoSelectTxtBtn2.setLayoutX(paddingLeft + arquivoSelectTxtLbl2.getWidth());
		arquivoSelectTxtBtn2.setLayoutY(paddingTop + arquivoSelectTxtBtn1.getHeight() + paddingTop);
		compararBtn.setLayoutX(paddingLeft);
		compararBtn.setLayoutY(arquivoSelectTxtBtn2.getLayoutY() + arquivoSelectTxtBtn2.getHeight() + paddingSpace);		
		barra.setLayoutX(paddingLeft);
		barra.setLayoutY(compararBtn.getLayoutY() + compararBtn.getHeight() + paddingSpace);		
		barra.setProgress(0);
		salvarRelatorioBtn.setLayoutX((double)(windowWidth - 20 - salvarRelatorioBtn.getWidth()));
		salvarRelatorioBtn.setLayoutY(barra.getLayoutY()-10);
		status.setLayoutX(barra.getLayoutX() + barra.getWidth() + paddingSpace);
		status.setLayoutY(barra.getLayoutY());
		comparacaoTAVB.setLayoutX(paddingLeft);
		comparacaoTAVB.setLayoutY(barra.getLayoutY() + barra.getHeight() + paddingSpace);
		comparacaoTAVB.setPrefWidth((double)(windowWidth - 40));
		comparacaoTAVB.setPrefHeight((double)(windowHeight - 135));
		logger.debug("comparacaoTAVB.isResizable: "+comparacaoTAVB.isResizable());
	}

	private void initEvents() {
		arquivoSelectTxtBtn1.setOnAction(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent event) {
				try {
					carregaInfoIniTxt1();
					arquivoSelectTxt1 =  fileChooserSelectTxt1.showOpenDialog(stage);
					atualizaTxt1ArquivoIni();
				}catch (IOException | NullPointerException e) {
					logger.error("",e);
				}
			}			
		});
		arquivoSelectTxtBtn2.setOnAction(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent event) {
				try {
					carregaInfoIniTxt2();
					arquivoSelectTxt2 =  fileChooserSelectTxt2.showOpenDialog(stage);
					atualizaTxt2ArquivoIni();
				}catch (IOException | NullPointerException e) {
					logger.error("",e);
				}
			}			
		});
		salvarRelatorioBtn.setOnAction(new EventHandler<ActionEvent>() {			
			@Override
			public void handle(ActionEvent event) {
				try {
					carregaInfoSalvarRelatorioIni();
					if(arquivoSelectTxt1!=null && arquivoSelectTxt1.isFile()){
						StringBuilder nomeArquivoSugerido = new StringBuilder(arquivoSelectTxt1.getName().replace(".txt", ""));
						nomeArquivoSugerido.append("_diff.txt");
						fileChooserSalvarRelatorio.setInitialFileName(nomeArquivoSugerido.toString());
					}
					arquivoSalvarRelatorio =  fileChooserSalvarRelatorio.showSaveDialog(stage);
					atualizaCaminhoRelatorioArquivoIni();
				}catch (IOException | NullPointerException e) {
					logger.error("",e);
				}
			}									
		});
		/* Tentativa se criacao de processo com ProcessBar */
		compararBtn.setOnAction((ActionEvent acao) -> {
			try {
				if(tabelaSelectGenericaOracle1 != null && tabelaSelectGenericaOracle2 != null){
					selecionaPkJFxUI = new SelecionaPkJFxUI();
					selecionaPkUIStage = new Stage();
					selecionaPkJFxUI.start(selecionaPkUIStage);
					selecionaPkJFxUI.setParentUi(thisUI);
					logger.debug("Lista Colunas Tabela 1: " + tabelaSelectGenericaOracle1.getNomesColunas());
					logger.debug("Lista Colunas Tabela 2: " + tabelaSelectGenericaOracle2.getNomesColunas());
					List<ColunasIguaisIndicesVO> listaNomesColunasEmComum = new ComparadorHelper().getNomesColunasEmComum(tabelaSelectGenericaOracle1.getNomesColunas(), tabelaSelectGenericaOracle2.getNomesColunas());
					logger.debug("Lista Nome Colunas em Comum: " + listaNomesColunasEmComum);
					selecionaPkJFxUI.setListaNomesCampos(listaNomesColunasEmComum);
					logger.debug("Depois de selecionaPkUI");
				}
			} catch (Exception e) {
				logger.error("",e);
			}			
		});
		stage.widthProperty().addListener((obs, oldVal, newVal) -> {
			logger.debug("--- EVENT Maximized Property ---");
			logger.debug("pane.getWidth(): " + pane.getWidth());
			comparacaoTAVB.setPrefWidth((double)(pane.getWidth() - 40));
			salvarRelatorioBtn.setLayoutX((double)(pane.getWidth() - 20 - salvarRelatorioBtn.getWidth()));
		});
		stage.heightProperty().addListener((obs, oldVal, newVal) -> {
			logger.debug("--- EVENT Height Property ---");
			logger.debug("pane.getHeight(): " + pane.getHeight());
			comparacaoTAVB.setPrefHeight((double)(pane.getHeight() - 135));
		});
		stage.maximizedProperty().addListener((obs, oldVal, newVal) -> {
			logger.debug("--- EVENT Maximized Property ---");			
			logger.debug("pane.getWidth(): " + pane.getWidth());
			logger.debug("pane.getHeight(): " + pane.getHeight());
			logger.debug("stage.getWidth(): " + stage.getWidth());
			logger.debug("stage.getHeight(): " + stage.getHeight());
			// --- Nao esta funcionando os Tamanhos Quando Maximizado
			if(stage.isMaximized()){
				logger.debug("stage.isMaximized(): " + stage.isMaximized());
				paneWidthAntes = pane.getWidth();
				paneHeightAntes = pane.getHeight();
				logger.debug("paneWidthAntes: " + paneWidthAntes);
				logger.debug("paneHeightAntes: " + paneHeightAntes);
				comparacaoTAVB.setPrefWidth((double)(screenWidth - 40));
				salvarRelatorioBtn.setLayoutX((double)(screenWidth - 20 - salvarRelatorioBtn.getWidth()));
				comparacaoTAVB.setPrefHeight((double)(screenHeight - 135));				
			}else{
				logger.debug("stage.isMaximized(): " + stage.isMaximized());
				logger.debug("paneWidthAntes: " + paneWidthAntes);
				logger.debug("paneHeightAntes: " + paneHeightAntes);
				comparacaoTAVB.setPrefWidth((double)(paneWidthAntes - 40));
				salvarRelatorioBtn.setLayoutX((double)(paneWidthAntes - 20 - salvarRelatorioBtn.getWidth()));
				comparacaoTAVB.setPrefHeight((double)(paneHeightAntes - 135));
			}
			logger.debug("comparacaoTAVB.getWidth(): " + comparacaoTAVB.getWidth());
			logger.debug("comparacaoTAVB.getHeight(): " + comparacaoTAVB.getHeight());
		});
	}
	
	private void atualizaTxt1ArquivoIni() throws IOException, FileNotFoundException {
		if(arquivoSelectTxt1.exists() && arquivoSelectTxt1.isFile()){
			fileChooserSelectTxt1.setInitialDirectory(arquivoSelectTxt1);
			iniHelper.setPropertyValue(ConstantesUtils.PROFILE_CONFIG, ConstantesUtils.PATH_ARQUIVO_SELECT_TXT_1, arquivoSelectTxt1.getAbsolutePath());						 
			tabelaSelectGenericaOracle1 = tabelaSelectGenericaOracleHelper.getTabelaGenericaDeArquivoSelectTxtOracle(arquivoSelectTxt1);
		}
	}
	
	private void atualizaTxt2ArquivoIni() throws IOException, FileNotFoundException {
		if(arquivoSelectTxt2.exists() && arquivoSelectTxt2.isFile()){
			fileChooserSelectTxt2.setInitialDirectory(arquivoSelectTxt2);
			iniHelper.setPropertyValue(ConstantesUtils.PROFILE_CONFIG, ConstantesUtils.PATH_ARQUIVO_SELECT_TXT_2, arquivoSelectTxt2.getAbsolutePath());
			tabelaSelectGenericaOracle2 = tabelaSelectGenericaOracleHelper.getTabelaGenericaDeArquivoSelectTxtOracle(arquivoSelectTxt2);
		}
	}
	
	private void atualizaCaminhoRelatorioArquivoIni() throws IOException {
		if(arquivoSalvarRelatorio != null){
			fileChooserSalvarRelatorio.setInitialDirectory(arquivoSalvarRelatorio);
			iniHelper.setPropertyValue(ConstantesUtils.PROFILE_CONFIG, ConstantesUtils.PATH_ARQUIVO_SALVAR_RELATORIO, arquivoSalvarRelatorio.getAbsolutePath());
			//Com a maneira abaixo, o FileWriter ew fechado automaticamente sem a necessidade do metodo close()
			try(FileWriter writer = new FileWriter(arquivoSalvarRelatorio,false)){
				writer.write(comparacaoTA.getText());
			}
		}		
	}
	
	private void executaComparacaoTask(){
		/* Exemplo Retirado de: "https://pt.stackoverflow.com/questions/49101/preencher-progressbar-em-javafx" */
		/*Criando um classe anonima Service que cria uma Task que tambem ew anônima.
        A classe Service serve para gerenciar threads em JavaFX */
		Service<Void> servico = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {					
				ComparaSelectTxtJFXTask comparaSelectTxtJFXTask = new ComparaSelectTxtJFXTask();
				comparaSelectTxtJFXTask.setTabelaSelectGenericaOracleHelper(tabelaSelectGenericaOracleHelper).setArquivoSelectOracle1(arquivoSelectTxt1).setArquivoSelectOracle2(arquivoSelectTxt2).setTextArea(comparacaoTA).setPrimaryKeys(primaryKeys);
				return comparaSelectTxtJFXTask;					
			}
		};
		//fazendo o bind (ligando) nas proprety
        status.textProperty().bind(servico.messageProperty());
        barra.progressProperty().bind(servico.progressProperty());
        //precisa inicializar o Service
        servico.restart();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void setPrimaryKeys(List<ColunasIguaisIndicesVO> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}
	
	public void continuaExecucaoDeComparacao(List<ColunasIguaisIndicesVO> primaryKeys){
		setPrimaryKeys(primaryKeys);
		executaComparacaoTask();
	}
	
}
