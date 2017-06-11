package test;

public class ProtocoloJogadores {
	public static String getAcao(String str) {
		return str.split("#")[0];
	}
	public static String getId(String str) {
		str = str.split("#")[1];
		return str.split(",")[0];
	}
	public static String getPosicao(String str) {
		str = str.split("#")[1];
		return str.split(",")[1];
	}
	public static String getSaldo(String str) {
		str = str.split("#")[1];
		return str.split(",")[2];
	}
	
 }
