package br.edu.utils.date;

import java.util.Calendar;

public class CurrentDateTime {
	public static String getCurrentDateTime(){
		return AbsConvTempo.converteMillisegundos(Calendar.getInstance().getTimeInMillis());
	}
	public static String getCurrentDateTime(String formato){
		return AbsConvTempo.converteMillisegundos(Calendar.getInstance().getTimeInMillis(),formato);
	}
}
