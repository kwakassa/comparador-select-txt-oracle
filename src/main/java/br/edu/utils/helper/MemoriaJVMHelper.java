package br.edu.utils.helper;

import org.apache.log4j.Logger;


public class MemoriaJVMHelper {
	private static final Logger logger = Logger.getLogger(MemoriaJVMHelper.class);
	public static final int PRINT_TYPE_CUSTOM = 1;
	public static final int PRINT_TYPE_APACHE = 2;
	
	private MemoriaJVMHelper(){}
	
	// Convert byte size into human readable format in java. Set si=true
	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1)
				+ (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
	public static String imprimeStatusMemoria(int type) {
		String texto = "Memoria Total/Livre da JVM: ";
		String totalMemory;
		String freeMemory;
		switch (type) {
		case PRINT_TYPE_CUSTOM:
			totalMemory = humanReadableByteCount(Runtime.getRuntime().totalMemory(), true);
			freeMemory = humanReadableByteCount(Runtime.getRuntime().freeMemory(), true);
			break;
		case PRINT_TYPE_APACHE:
			totalMemory = org.apache.commons.io.FileUtils.byteCountToDisplaySize(Runtime.getRuntime().totalMemory());
			freeMemory = org.apache.commons.io.FileUtils.byteCountToDisplaySize(Runtime.getRuntime().freeMemory());
			break;
		default:
			totalMemory = "" + Runtime.getRuntime().totalMemory();
			freeMemory = "" + Runtime.getRuntime().freeMemory();
			break;
		}
		texto += totalMemory + "/" + freeMemory;
		return texto;
	}
	
}
