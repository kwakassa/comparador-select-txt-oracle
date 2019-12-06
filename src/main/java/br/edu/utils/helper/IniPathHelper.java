package br.edu.utils.helper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;

public class IniPathHelper {
	private static final Logger logger = Logger.getLogger(IniPathHelper.class);
	private Ini arquivoIni;
	private Map<String,String> listaValoresPadrao = new HashMap<>();
	
	public IniPathHelper(String nomeArquivoIniComExtensao, Map<String,String> listaValoresPadrao) throws IOException {
		inicializaListaDeValoresPadrao(listaValoresPadrao);
		inicializa(nomeArquivoIniComExtensao);
	}
	
	/*	listaValoresPadrao.put(ConstantesUtils.PATH_ARQUIVO_SELECT_TXT_1, "");
		listaValoresPadrao.put(ConstantesUtils.PATH_ARQUIVO_SELECT_TXT_2, "");*/
	private void inicializaListaDeValoresPadrao(Map<String,String> listaValoresPadrao){
		this.listaValoresPadrao = listaValoresPadrao;
	}
	
	public void inicializa(String nomeArquivoIniComExtensao) throws IOException{
		if(nomeArquivoIniComExtensao.contains(".ini")){
			try {
				File pathArquivoIni = new File(System.getProperty("user.home") + File.separator + nomeArquivoIniComExtensao);
				logger.debug("Caminho Absoluto do arquivo INI: " + pathArquivoIni.getAbsolutePath());
				if(!pathArquivoIni.exists()){
					JOptionPane.showMessageDialog(null, "Arquivo de configura\u00E7\u00E3o nao encontrado. Criando INI na home do usu\u00E1rio");
					logger.debug("Arquivo INI: " + pathArquivoIni.getAbsolutePath());
					if(pathArquivoIni.createNewFile()){
						arquivoIni = new Ini(pathArquivoIni);
						inicializaValoresPadrao(arquivoIni, ConstantesUtils.PROFILE_CONFIG, listaValoresPadrao);
					}else{
						throw new IOException("N\u00E3o foi poss\u00EDvel criar o arquivo de configura\u00E7\u00E3o INI no home do usu\u00E1rio");
					}
				}else{
					logger.debug("Arquivo INI: " + pathArquivoIni.getAbsolutePath());
					arquivoIni = new Ini(pathArquivoIni);
					imprimeValoresPropriedadesIni(arquivoIni);
				}
			}catch (IOException e ) {
				logger.error("Problema na opera\u00E7\u00E3o do objeto INI - cria\u00E7\u00E3o ou persistencia de valor", e);
			}
		}else{
			throw new IOException("Informe o nome do arquivo com extens\u00E3o .ini");
		}
	}
	
	private void inicializaValoresPadrao(Ini arquivoIni, String SessaoIni, Map<String,String> listaPropriedadesIni) throws IOException {
		for (String key : listaPropriedadesIni.keySet()) {
			arquivoIni.put(SessaoIni, key, listaValoresPadrao.get(key));
		}
		arquivoIni.store();
	}
	
	private void imprimeValoresPropriedadesIni(Ini arquivoIni){
		Set<Entry<String, Section>> entrySet = arquivoIni.entrySet();
		for (Entry<String, Section> entry : entrySet) {
			logger.debug("EntrySet Key (Section): " + entry.getKey());
			logger.debug("EntrySet Values (propriedade,valor): " + entry.getValue());
		}
	}
	
	public String getPropertyValue(String sessao, String propriedade){
		return arquivoIni.get(sessao, propriedade);
	}
	
	public void setPropertyValue(String sessao, String propriedade, String valor) throws IOException{
		arquivoIni.put(sessao, propriedade,valor);
		arquivoIni.store();		
	}
}
