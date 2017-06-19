package test;

import controller.Controller;
import model.Acao;
import model.Jogador;

/**
 * Protocolo: NUMERO_ACAO(int)#ID(int),POSICAO(int),SALDO(float)
 */
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
	public static String requisitar(int acao, int id) {
		return acao+"#"+id;
	}
	public static String responderAniversariante(int idEuJogador, int idAniversariante) {
		return Acao.RESPOSTA_PARA_O_ANIVERSARIANTE+"#"+idEuJogador+","+idAniversariante;
	}
	public static int getIdResponderAniversariante(String str) {
		str = str.split("#")[1];
		return Integer.valueOf(str.split(",")[1]);
	}
	
	public static String enviarSorteGrandeQuantia(int acao, int id, float quantia) {
		return acao + "#" + id + "," + quantia;
	}
	
	public static float formatarSorteGrandeQuantia(String str) {
		str = str.split("#")[1];
		return Float.valueOf(str.split(",")[1]);
	}
	
	public static String enviarInicioMaratonaBeneficente(int acao, int id) {
		return acao + "#" + id;
	}
	
	public static String enviarFimMaratonaBeneficente(int acao, int id) {
		return acao + "#" + id;
	}
	
	public static String enviarNotificacaoComecoDeJogada(int acao, int id) {
		return acao + "#" + id;
	}
	
	public static String enviarNotificacaoProximoJogador(int acao, int id) {
		return acao + "#" + id;
	}
	
	public static String enviarFimDeMes(int acao, int id) {
		return acao + "#" + id;
	}
	
	public static String pagarVizinho(int idVizinho) {
		return Acao.PAGUEI_VIZINHO+"#"+idVizinho;
	}
//	public static String enviarDinheiroExtra(int idDinheiroExtra, int euJogador) {
//		return Acao.DINHEIRO_EXTRA+"#"+idDinheiroExtra+Co; 
//	}
	public static int getIdResponderDinheiroExtra(String str) {
		str = str.split("#")[1];
		return Integer.valueOf(str.split(",")[1]);
	}
 }
