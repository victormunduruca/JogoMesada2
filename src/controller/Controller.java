package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JOptionPane;

import excpeptions.IdNaoEncontradoException;
import model.Jogador;

public class Controller {
	
	private ArrayList<Jogador> jogadores; //Jogadores na partida
	private int idAtual; //Id do jogador que est� em sua vez
	private int idJogadorMaquina; //Id jogador desse computador
	private ArrayList<String> cartasCorreio; //Cartas referentes a casa correio
	private ArrayList<String> bufferCartasCorreio;
	
	public Controller() {
		criaCartas();
	}
	//Colocar m�todo update para mudar o id do jogador atual com base no controller de rede
	//Implementar padr�o observer com a interface para atualizar quando um jogador muda
	
	/*
	 * while(nao acaba o jogo){
	 * 		if(idAtual == idJogadorMaquina) 
	 * 				jogadaInterna();
	 * 		else 
	 * 				jogadaExterna();	
	 * }
	 * Alternativa: while na view
	 */
	
//	public int jogar(int valorDado) throws IdNaoEncontradoException {
//		
//		if(idAtual != idJogadorMaquina) {
//			//
//		}
//		Jogador jogador = getJogador(idAtual);
//		
//		jogador.setPosicaoPino(jogador.getPosicaoPino() + valorDado);
//		
//		int posicao = jogador.getPosicaoPino();
//		
//		if(posicao == 2) {
//			jogador.setSaldo(jogador.getSaldo() + 5000);
//			//
//		} 
////		else if() {
////			
////		}
//			
//		
//		
//		return jogador.getId();
//	}
	public static void main(String[] args) {
		JOptionPane.showConfirmDialog(null, "teste");
		Controller controller = new Controller();
		Jogador jogador = new Jogador();
		jogador.setPosicaoPino(3);
		ArrayList<String> cartinhas = controller.casaCorreio(jogador);
		
	}
	/**
	 * M�todo que realiza as a��es da casa correio
	 * @param JogadorAtual
	 * @return ArrayList referente �s cartas aleat�rias geradas
	 */
	public ArrayList<String> casaCorreio(Jogador jogador) {
		System.out.println("posicao: " +jogador.getPosicaoPino());
		int posicao = jogador.getPosicaoPino();
		int qtdCartas = 0;
		if(posicao == 1 ||  posicao == 22 || posicao == 19 || posicao == 11)
			qtdCartas = 1;
		else if(posicao == 24 || posicao == 5)
			qtdCartas = 2;
		else if(posicao == 16 || posicao == 3)
			qtdCartas = 3;
		System.out.println("qtdcartas: " +qtdCartas);
		ArrayList<String> cartasCorreios = geraCartasCorreiosAleatorias(qtdCartas);
		return cartasCorreios;
	}
	/**
	 * M�todo que gera cartas correios aleat�rias
	 * @param Quantidade de cartas que se deseja obter
	 * @return Lista contendo a quantidade de cartas correios aleat�rias requisitadas
	 */
	public ArrayList<String> geraCartasCorreiosAleatorias(int qtdCartas) {
		ArrayList<String> cartasAleatorias = new ArrayList<String>();
		Random rand = new Random();
		for(int i = 0; i < qtdCartas; i++) {
			cartasAleatorias.add(cartasCorreio.get(rand.nextInt(6)));
		}
		Iterator<String> it = (Iterator) cartasAleatorias.iterator();
		while(it.hasNext()) {
			String s = (String) it.next();
			System.out.println("Carta: " +s);
		}
		
		return cartasAleatorias;
	}
	/**
	 * M�todo que inicializa as cartas correio
	 */
	public void criaCartas() {
		cartasCorreio = new ArrayList<String>();
		cartasCorreio.add("contas");
		cartasCorreio.add("pague a um vizinho agora");
		cartasCorreio.add("dinheiro extra");
		cartasCorreio.add("doa��es");
		cartasCorreio.add("cobran�a monstro");
		cartasCorreio.add("v� para frente agora");
	}
	/**
	 * Realiza as a�oes da casa correio a partir das cartas aleat�rias antes tiradas 
	 * @param String referente ao tipo de carta que a a��o va ser tomada
	 */
	public void acaoCasaCorreio(String acao) {
		if(acao == "contas") {
			//realiza acao contas
		} else if(acao == "pague a um vizinho agora") {
			
		} else if(acao == "dinheiro extra") {
			
		} else if(acao == "doacoes") {
			
		} else if(acao == "cobran�a monstro") {
			
		} else if(acao == "v� para frente agora") {
			
		}
	}
	/**
	 * M�todo para obter o jogador a partir de um determinado id
	 * @param Id do jogador que se deseja obter
	 * @return O jogador com o id especificado
	 * @throws IdNaoEncontradoException
	 */
	public Jogador getJogador(int idJogador) throws IdNaoEncontradoException {
		Iterator<Jogador> it = (Iterator) jogadores.iterator();
		while(it.hasNext()) {
			Jogador jogador = (Jogador) it.next();
			if(jogador.getId() == idJogador)
				return jogador;
		}
		throw new IdNaoEncontradoException();
	}
	
}
