package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import util.Util;

import excpeptions.IdNaoEncontradoException;
import model.CartaCompra;
import model.Jogador;


public class Controller{
	
	private static Controller instanciaController;

	private ArrayList<Jogador> adversarios = new ArrayList<>();
	private Jogador euJogador;
	
	private ArrayList<String> cartasCorreio; //Cartas referentes a casa correio
	
	private float totalSorteGrande = 0;
	
	private boolean habilitarMaratonaBeneficente = false;
	
	private boolean fimDeJogo = false;
	
	private int totalJogadoresAtivos = 6;
	
	private Controller() {
		criaCartas();
	}
	
	
	public int getTotalJogadoresAtivos() {
		return totalJogadoresAtivos;
	}
	
	public int incrementarTotalJogadoresAtivos() {
		return (++totalJogadoresAtivos);
	}
	
	public int decrementarTotalJogadoresAtivos() {
		return (--totalJogadoresAtivos);
	}
	
	public static Controller getInstance(){
		if(instanciaController == null) 
			instanciaController = new Controller();
		return instanciaController;
	}
		
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
	
		this.euJogador = euJogador;
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
		cartasCorreio.add("doacoes");
//		cartasCorreio.add("cobranca monstro");
//		cartasCorreio.add("va para frente agora");
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
	/**
	 * Lança o dado e atualiza a posicao do jogador
	 * 
	 * @return valor do dado lancado
	 */
	public int lancarDado() {
		int valorDado = Util.randInt(1, 6);
		int posicao = Math.min((euJogador.getPosicaoPino() + valorDado), 31); // Se igual a 32, zera a posicao
		euJogador.setPosicaoPino(posicao);
		return valorDado;
	}
	
	private void fazerEmprestimoEuJogador(float quantia) {
		getEuJogador().setDivida(getEuJogador().getDivida() + quantia);
		getEuJogador().setSaldo(getEuJogador().getSaldo() + quantia);
	}
	
	/**
	 * Adiciona uma quantia para o montante do "Sorte Grande"
	 * 
	 * @param quantia
	 * @param fazerEmprestimo
	 * @return false caso o jogador nao tenha creditos suficientes, caso contrario true
	 */
	public boolean pagarSorteGrandeEuJogador(float quantia, boolean fazerEmprestimo) {
		if (fazerEmprestimo) {
			fazerEmprestimoEuJogador(quantia);
		}
		
		if ((getEuJogador().getSaldo() - quantia) >= 0) { // Checa saldo
			getEuJogador().setSaldo(getEuJogador().getSaldo() - quantia);
			addQuantiaParaSorteGrande(quantia);
			return true;
			
		} else { // Sem saldo, e' preciso fazer um emprestimo
			System.out.println("++++++++++++++ Novo saldo: " + getEuJogador().getSaldo());
			return false;
		}
	}
	
	/**
	 *  Atualiza a quantia da "sorte grande" para esse jogador
	 *  
	 * @param quantia
	 */
	public void addQuantiaParaSorteGrande(float quantia) {
		setTotalSorteGrande(getTotalSorteGrande() + quantia);
	}
	
	public float getTotalSorteGrande() {
		return totalSorteGrande;
	}
	
	public void zerarSorteGrande() {
		setTotalSorteGrande(0);
	}
	
	private void setTotalSorteGrande(float totalSorteGrande) {
		this.totalSorteGrande = totalSorteGrande;
	}

	public boolean isHabilitarMaratonaBeneficente() {
		return habilitarMaratonaBeneficente;
	}
	
	public void setHabilitarMaratonaBeneficente(boolean habilitarMaratonaBeneficente) {
		this.habilitarMaratonaBeneficente = habilitarMaratonaBeneficente;
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
		return contas.get(Util.randInt(0, 2));
	}
	public float getValorConta(String conta) {
		conta = conta.split(",")[1];
		conta = conta.replace("$", "");
		return Float.valueOf(conta);	
	}
	public boolean casaDiaMesada(Jogador jogador) { //Realiza a a��o assim como indica se o total ou o juros foi pago
		jogador.setSaldo(jogador.getSaldo()+3500);
		float divida = jogador.getDivida();
		if(jogador.getSaldo() - (divida + divida*0.1) >= 0) {
			pagarTotalDiaMesada(jogador);
			return true;
		} else {
			pagarJurosDiaMesada(jogador);
			return false;
		}
	}
	public void pagarTotalDiaMesada(Jogador jogador) {
		float dividaTotal = (float) (jogador.getDivida() + jogador.getDivida()*0.1);
		System.out.println("Divida total: " +dividaTotal);
		jogador.setSaldo(jogador.getSaldo()-(dividaTotal));
		jogador.setDivida(0); //TODO Verificar se � pra zerar mesmo
	}
	public  void pagarJurosDiaMesada(Jogador jogador) {
		float juros = (float) (jogador.getDivida()*0.1);
		jogador.setSaldo(jogador.getSaldo()-juros);
		jogador.setDivida(jogador.getDivida()-juros);
	}

	public boolean isFimDeJogo() {
		return fimDeJogo;
	}
	
	public void setFimDeJogo(boolean fimDeJogo) {
		this.fimDeJogo = fimDeJogo;
	}
}
