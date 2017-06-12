package test;

import model.Jogador;

public class ProtocoloJogadores {
	public static int getAcao(String str) {
		return Integer.valueOf(str.split("#")[0]);
	}
	public static int getId(String str) {
		str = str.split("#")[1];
		return Integer.valueOf(str.split(",")[0]);
	}
	public static int getPosicao(String str) {
		str = str.split("#")[1];
		return Integer.valueOf(str.split(",")[1]);
	}
	public static float getSaldo(String str) {
		str = str.split("#")[1];
		return Float.valueOf(str.split(",")[2]);
	}
	public static String enviarJogador(int acao, Jogador jogador) {
		return acao+"#"+jogador.getId()+","+jogador.getPosicaoPino()+","+jogador.getSaldo();
	}
 }
