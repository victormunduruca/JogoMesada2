package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JOptionPane;

import network.Servidor;
import network.Servidor.OnServidor;
import excpeptions.IdNaoEncontradoException;
import model.CartaCompra;
import model.Jogador;
import model.Observer;
import model.Publisher;
import model.onController;

public class Controller{
	
	private static Controller instanciaController;
	private ArrayList<Observer> observers;
	
	private ArrayList<Jogador> adversarios = new ArrayList<>();
	private Jogador euJogador;
	
	private ArrayList<Jogador> jogadores; //Jogadores na partida
	private ArrayList<String> cartasCorreio; //Cartas referentes a casa correio
//	private ArrayList<String> bufferCartasCorreio;
	private int saldoCliente;
	
	private Servidor servidor;
	
	private Controller() {
//		//TESTEEEEEEEEE
//		adversarios = new ArrayList<Jogador>();
//		for(int i = 0; i < 7; i++) {
//			Jogador jogador = new Jogador(i, i*1000, "abc"); //tirar esse construtor
//			adversarios.add(jogador);
//		}
//		//TESTEEEEEEEEE
	//	iniciarServidor();
		observers = new ArrayList<Observer>();
		criaCartas();
	}
	/**
	 * M�todo que notfica o come�o da partida, no momento que sai da sala de espera
	 */
//	public void notificarComeco() {
//		notifyObserver(0, euJogador);
//		for(Jogador j : jogadores) {
//			notifyObserver(0, j);
//		}
//	}
	public static Controller getInstance(){
		if(instanciaController == null) 
			instanciaController = new Controller();
		return instanciaController;
	}
	
//	private void iniciarServidor() {
//		try {
//			servidor = new Servidor(4040 + getEuJogador().getId());
//			servidor.run(new OnServidor() {
//				
//				@Override
//				public void onDadoRecebido(String data) {
//					// XXX Apagar quando você ler, Victor
//					// É aqui onde começa a lógica do jogo.
//					// Falta apenas formatar esse "data" bruto que representa o protocolo 
//					// para as informações de jogo (semelhante o que eu fiz em network.Protocolo
//					// para implementar a sala de espera
//					
//					System.out.println("Eco recbedido: " + data);
//				}
//				@Override
//				public void onErro() { }
//			});
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	public ArrayList<Jogador> getAdversarios() {
		return adversarios;
	}
	public Jogador getAdversario(int id) {
		for(Jogador jogador : adversarios) {
			if(jogador.getId() == id) {
				return jogador;
			}
		}
		return null;
	}
	public void setAdversarios(ArrayList<Jogador> adversarios) {
		this.adversarios = adversarios;
	}

	public Jogador getEuJogador() {
		return euJogador;
	}

	public void setEuJogador(Jogador euJogador) {
	//	System.out.println("Atualizou eu jogador com o id: " +euJogador.getId());
		this.euJogador = euJogador;
		//notifyObserver(euJogador);
	}
	
//	public static void main(String[] args) {
//		//JOptionPane.showConfirmDialog(null, "teste");
//		System.out.println("AFF");
//		Controller controller = new Controller();
//		Jogador jogador = new Jogador();
//		jogador.setId(1);
//		int opcao = JOptionPane.showConfirmDialog(null, "WOW", "Selecione os pontos", JOptionPane.OK_CANCEL_OPTION);
////		System.out.println("Saldo antes: " +jogador.getSaldo());
////		controller.acaoCompraEntretenimento(false, jogador, controller.getCompraEntretenimento());
////		System.out.println("Nome da carta: " +jogador.getCartasCompras().get(0).getNomeCarta());
////		controller.casaAchouComprador(jogador);
////		System.out.println("Saldo depois: " +jogador.getSaldo());
//		
//		//jogador.setPosicaoPino(3);
//		//ArrayList<String> cartinhas = controller.casaCorreio(jogador);
//		
//	}
	
	
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
			qtdCartas = 1; //TODO Colocar pra 1 de novo
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
			cartasAleatorias.add(cartasCorreio.get(rand.nextInt(2))); //TODO colocar 6 novamente
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
//		cartasCorreio.add("dinheiro extra");
//		cartasCorreio.add("doacoes");
//		cartasCorreio.add("cobranca monstro");
//		cartasCorreio.add("va para frente agora");
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
	/**
	 * 
	 * @return CartaCompra criada no controller
	 */
	public CartaCompra getCompraEntretenimento() {
		CartaCompra carta = new CartaCompra();
		return carta;  
	}
	/**
	 * M�todo que realiza compra da carta de compras e entretenimento, ou diretamente ou por meio de empr�stimo
	 * @param Sinaliza��o se � empr�stimo ou n�o
	 * @param Joador
	 * @param Carta Compra
	 */
	public void acaoCompraEntretenimento(boolean eEmprestimo, Jogador jogador, CartaCompra carta) {
		if(eEmprestimo)
			jogador.setDivida(jogador.getDivida()+carta.getValorCarta());
		jogador.addCarta(carta); //adiciona a carta, debitando o valor no saldo internamente
	}
	public boolean necessitaEmprestimo(CartaCompra cartaCompra, Jogador jogador) {
		if(jogador.getSaldo() - cartaCompra.getValorCarta() < 0) {
			return true;
		}
		return false;
	}
	/**
	 * M�todo a��o em casa "achou comprador", que retorna ao jogador o valor de revenda na sua primeira carta de compras e 
	 * entretenimento.
	 * @param Jogador 
	 */
	public String casaAchouComprador(Jogador jogador) {
		CartaCompra carta;
		if(!jogador.getCartasCompras().isEmpty()) {
			carta = jogador.getCartasCompras().get(0);
			jogador.getCartasCompras().remove(0);
			jogador.setSaldo(jogador.getSaldo()+carta.getValorRevendaCarta());
			return carta.toString();
		} 
		return null;
	}
	/**
	 *M�todo casa pr�mio: adiciona 5000 ao saldo do jogador de par�metro
	 * @param jogador
	 */
	public void casaPremio(Jogador jogador) {
		jogador.setSaldo(jogador.getSaldo() + 5000);
	}
	/**
	 * M�todo vende-se que recebe o valor do dado e retira 100 vezes esse valor do saldo do jogador do par�metro.
	 * @param valorDado
	 * @param jogador
	 */	
	public ArrayList<Jogador> getJogadores() {
		return jogadores;
	}

	public void setJogadores(ArrayList<Jogador> jogadores) {
		this.jogadores = jogadores;
	}

	public Jogador jogarDado() {
		// TODO Auto-generated method stub
		euJogador.setPosicaoPino(euJogador.getPosicaoPino()+1);
		//notifyObserver(euJogador);
		return euJogador;
	}
	
	public void atualizarAdversario(int id, int posicao, float saldo) {
		for (Jogador adv : adversarios) {
			if (adv.getId() == id) {
				System.out.println("Adversario " + id + " modificado");
				adv.setPosicaoPino(posicao);
				adv.setSaldo(saldo);
				return;
			}
		}
		System.out.println("atualizarAdversario(): Adversario nao encontrado");
	}
	
//	public int lancarDado() {
//		int posicao = ((euJogador.getPosicaoPino() + 1) % 32); // Se igual a 32, zera a posicao
//		euJogador.setPosicaoPino(posicao);
//		return posicao;
//	}
	private static int valorDado() {
		Random rand = new Random();
		return rand.nextInt(6) + 1;
	}
	public int lancarDado() {
		//int valorDado = valorDado();
		int valorDado = 16;
		int posicao = ((euJogador.getPosicaoPino() + valorDado) % 32); // Se igual a 32, zera a posicao
		euJogador.setPosicaoPino(posicao);
		return valorDado;
	}
	private int randInt(int min, int max) { //TODO tirar static
	    Random rand = new Random();
	    return rand.nextInt((max - min) + 1) + min;
	}
	public void requisicaoAniversario() {
		euJogador.setSaldo(euJogador.getSaldo()-100); //TODO olhar pra nao ficar negativo
	}
	/**
	 * Metodo que for�a o emprestimo
	 * @param valor
	 * @param jogador
	 */
	public void debita(float valor, Jogador jogador) {
		float divida = 0;
		float valorEmprestimo = 0;
		if(jogador.getSaldo() - valor < 0) { //se precisa de emprestimo 
			valorEmprestimo = -1*(jogador.getSaldo() - valor);
			divida = jogador.getDivida()+valorEmprestimo;
			jogador.setDivida(divida);
		}
		jogador.setSaldo(jogador.getSaldo() - valor + valorEmprestimo);
	}
	public ArrayList<Integer> getListaIdsAdversarios() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for(Jogador adv : adversarios) {
			ids.add((adv.getId()));
		}
		return ids;
	}
	public String geraNovaContaAleatoria() {
		ArrayList<String> contas = new ArrayList<String>();
		contas.add("Conta de agua, 400$");
		contas.add("Celular novo, 600$");
		contas.add("Energia, 100$");
		return contas.get(randInt(0, 2));
	}
	public float getValorConta(String conta) {
		conta = conta.split(",")[1];
		conta = conta.replace("$", "");
		return Float.valueOf(conta);
	}
	
}
