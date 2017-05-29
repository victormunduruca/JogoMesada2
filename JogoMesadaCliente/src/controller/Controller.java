package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JOptionPane;


import excpeptions.IdNaoEncontradoException;
import model.CartaCompra;
import model.Jogador;
import model.Observer;
import model.Publisher;

public class Controller implements Publisher{
	
	private static Controller instanciaController;
	private ArrayList<Observer> observers;
	
	private ArrayList<Jogador> jogadores; //Jogadores na partida
	private int idAtual; //Id do jogador que está em sua vez
	private int idJogadorMaquina; //Id jogador desse computador
	private ArrayList<String> cartasCorreio; //Cartas referentes a casa correio
//	private ArrayList<String> bufferCartasCorreio;
	private int jogadaDado; //Camada de rede retorna a jogada do dado
	private int saldoCliente;
	
	
	
	private Controller() {
		//TESTEEEEEEEEE
		jogadores = new ArrayList<Jogador>();
		for(int i = 0; i < 7; i++) {
			Jogador jogador = new Jogador(i, i+1, i+1000); //tirar esse construtor
			jogadores.add(jogador);
		}
		//TESTEEEEEEEEE
		observers = new ArrayList<Observer>();
		criaCartas();
	}
	public static Controller getInstance(){
		if(instanciaController == null)
			instanciaController = new Controller();
		return instanciaController;
	}
	//Colocar método update para mudar o id do jogador atual com base no controller de rede
	//Implementar padrão observer com a interface para atualizar quando um jogador muda
	
	/*
	 * while(nao acaba o jogo){
	 * 		if(idAtual == idJogadorMaquina) 
	 * 				jogadaInterna();
	 * 		else 
	 * 				jogadaExterna();	
	 * }
	 * Alternativa: while na view
	 */
	
	public static void main(String[] args) {
		//JOptionPane.showConfirmDialog(null, "teste");
		System.out.println("AFF");
		Controller controller = new Controller();
		Jogador jogador = new Jogador();
		jogador.setId(1);
		int opcao = JOptionPane.showConfirmDialog(null, null, "Selecione os pontos", JOptionPane.OK_CANCEL_OPTION);
//		System.out.println("Saldo antes: " +jogador.getSaldo());
//		controller.acaoCompraEntretenimento(false, jogador, controller.getCompraEntretenimento());
//		System.out.println("Nome da carta: " +jogador.getCartasCompras().get(0).getNomeCarta());
//		controller.casaAchouComprador(jogador);
//		System.out.println("Saldo depois: " +jogador.getSaldo());
		
		//jogador.setPosicaoPino(3);
		//ArrayList<String> cartinhas = controller.casaCorreio(jogador);
		
	}
	private static int jogaDado() {
		Random rand = new Random();
		return rand.nextInt(6) + 1;
	}
	/**
	 * Método que realiza as ações da casa correio
	 * @param JogadorAtual
	 * @return ArrayList referente às cartas aleatórias geradas
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
	 * Método que gera cartas correios aleatórias
	 * @param Quantidade de cartas que se deseja obter
	 * @return Lista contendo a quantidade de cartas correios aleatórias requisitadas
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
	 * Método que inicializa as cartas correio
	 */
	public void criaCartas() {
		cartasCorreio = new ArrayList<String>();
		cartasCorreio.add("contas");
		cartasCorreio.add("pague a um vizinho agora");
		cartasCorreio.add("dinheiro extra");
		cartasCorreio.add("doações");
		cartasCorreio.add("cobrança monstro");
		cartasCorreio.add("vá para frente agora");
	}
	/**
	 * Realiza as açoes da casa correio a partir das cartas aleatórias antes tiradas 
	 * @param String referente ao tipo de carta que a ação va ser tomada
	 */
	public void acaoCasaCorreio(String acao) {
		if(acao == "contas") {
			//realiza acao contas
		} else if(acao == "pague a um vizinho agora") {
			
		} else if(acao == "dinheiro extra") {
			
		} else if(acao == "doacoes") {
			
		} else if(acao == "cobrança monstro") {
			
		} else if(acao == "vá para frente agora") {
			
		}
	}
//	public void mudaSaldoTeste() {
//		saldoCliente++;
//		notifyObserver();
//	}
	public void metodoTeste() throws IdNaoEncontradoException {
		Jogador jogador = getJogador(1);
		jogador.setSaldo(jogador.getSaldo() + 1000);
		notifyObserver(2, jogador);
	}
 	/**
	 * Método para obter o jogador a partir de um determinado id
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
	/**
	 * 
	 * @return CartaCompra criada no controller
	 */
	public CartaCompra getCompraEntretenimento() {
		CartaCompra carta = new CartaCompra();
		return carta;  
	}
	/**
	 * Método que realiza compra da carta de compras e entretenimento, ou diretamente ou por meio de empréstimo
	 * @param Sinalização se é empréstimo ou não
	 * @param Joador
	 * @param Carta Compra
	 */
	public void acaoCompraEntretenimento(boolean eEmprestimo, Jogador jogador, CartaCompra carta) {
		if(eEmprestimo)
			jogador.setDivida(jogador.getDivida()+carta.getValorCarta());
		jogador.addCarta(carta); //adiciona a carta, debitando o valor no saldo internamente
	}
	public void casaAchouComprador(Jogador jogador) {
		CartaCompra carta;
		if(!jogador.getCartasCompras().isEmpty()) {
			carta = jogador.getCartasCompras().get(0);
			jogador.getCartasCompras().remove(0);
			jogador.setSaldo(jogador.getSaldo()+carta.getValorRevendaCarta());
		}			
	}
	public void casaPrêmio(Jogador jogador) {
		jogador.setSaldo(jogador.getSaldo() + 5000);
	}
	public void vendeseCasa(int valorDado, Jogador jogador) {
		jogador.setSaldo(jogador.getSaldo() - 100*valorDado);
		jogador.addCarta(getCompraEntretenimento());
	} 
	public boolean eInterna() {
		if(idAtual == idJogadorMaquina)
			return true;
		else
			return false;
	}
	@Override
	public void register(Observer o) {
		// TODO Auto-generated method stub
		observers.add(o);
		
	}
	@Override
	public void unregister(Observer o) {
		// TODO Auto-generated method stub
		observers.remove(o);
	}
	@Override
	public void notifyObserver(int jogadaDado, Jogador jogador) {
		// TODO Auto-generated method stub
		for(Observer observer : observers) {
			observer.update(jogadaDado, jogador);
		}
	}
	public ArrayList<Jogador> getJogadores() {
		return jogadores;
	}

	public void setJogadores(ArrayList<Jogador> jogadores) {
		this.jogadores = jogadores;
	}

	public int getIdAtual() {
		return idAtual;
	}

	public void setIdAtual(int idAtual) {
		this.idAtual = idAtual;
	}

	public int getIdJogadorMaquina() {
		return idJogadorMaquina;
	}

	public void setIdJogadorMaquina(int idJogadorMaquina) {
		this.idJogadorMaquina = idJogadorMaquina;
	}
	
	
	
}
