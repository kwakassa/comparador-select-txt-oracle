package br.edu.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbsConvTempo {
	
	private static SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public static String converteMillisegundos(long tempo){
		String resultado = "";
		Date data = new Date(tempo);
		dataFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		resultado = dataFormat.format(data);
		return resultado;
	}
	public static String converteMillisegundos(long tempo,String formatoData){//Exemplo formato Data "dd/MM/yyyy HH:mm:ss"
		String resultado = "";
		Date data = new Date(tempo);
		dataFormat = new SimpleDateFormat(formatoData);
		resultado = dataFormat.format(data);
		return resultado;
	}
	public static long converteDataHora(String dataHora) throws ParseException{
		long resultado = 0;
		Date data = dataFormat.parse(dataHora);
		resultado = data.getTime();
		return resultado;
	}
}
