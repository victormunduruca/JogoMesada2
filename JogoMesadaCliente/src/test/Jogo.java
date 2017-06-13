package test;

import java.io.IOException;
import java.util.ArrayList;

import controller.Controller;
import model.Acao;
import model.CartaCompra;
import model.Jogador;
import network.Cliente.OnRequest;
import network.Servidor.OnServidor;
import network.Cliente;
import network.Protocolo;
import network.Servidor;
import util.Util;
import view.JanelaPrincipal;
import view.JanelaPrincipal.OnJogo;
import view.JanelaSalaDeEspera;

public class Jogo implements OnJogo {
	
	private static JanelaSalaDeEspera janelaEspera;
	private static JanelaPrincipal janelaTabuleiro;
    private static Servidor server;
    private static int PORTA_BASE = 4040;
 
	public static void main(String[] args) {
		new Jogo();
	}
    
    public Jogo() {
    	janelaEspera = new JanelaSalaDeEspera(new SalaDeEsperaRequestCallback());
    	janelaEspera.iniciar();
	}
    
	public void inicializaServidor() {
		try {
			server = new Servidor(PORTA_BASE + Controller.getInstance().getEuJogador().getId());
			server.run(new OnServidor() {
				
				@Override
				public void onDadoRecebido(String data) {
					System.out.println("Eco recbedido: " + data);
					
					executaLogicaDeJogo(data);
				}
				@Override
				public void onErro() { }
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void executaLogicaDeJogo(final String data) {
		int acaoDeJogo = ProtocoloJogadores.getAcao(data);
		final Controller controller = Controller.getInstance();
		
		switch (acaoDeJogo) {
		case Acao.ATUALIZACAO_ESTADO:
			// Atualiza o adversario no model
			controller.atualizarAdversario(
					ProtocoloJogadores.getId(data), 
					ProtocoloJogadores.getPosicao(data), 
					ProtocoloJogadores.getSaldo(data));
			
			// Atualiza os jogadores na view 
			// (Atualmente atualizando apenas a lista. Podemos usar para atualizar o tabuleiro também)
	    	janelaTabuleiro.atualizaJogadores(
	    			controller.getEuJogador(), 
					controller.getAdversarios());
	
			break;
			
		case Acao.REQUISICAO_ANIVERSARIO: //Se for requisitado a pagar a quantia do aniversario
			System.out.println("%%%%%%%%%%% REQUISICAO ANIVERSARIO DO JOGADOR " +ProtocoloJogadores.getId(data));
			controller.requisicaoAniversario(); //Debita a quantia
			// Envia a minha resposta pra o aniversariante
			(new Thread() {
				@Override
				public void run() {
					Cliente cliente = new Cliente();
					//Envia ao aniversariante confirmando que pagou
					System.out.println("---------------------------------------ENVIOU PRESENTE ANIVERSARIO PARA: " +ProtocoloJogadores.getId(data));
					System.out.println("Resposta: " + 
							cliente.enviar(controller.getAdversario(ProtocoloJogadores.getId(data)).getIp(), 4040 + ProtocoloJogadores.getId(data), ProtocoloJogadores.responderAniversariante(controller.getEuJogador().getId(), ProtocoloJogadores.getId(data)))); 
					//Atualiza sua posicao para os outros
					for (Jogador adv : Controller.getInstance().getAdversarios()) {
						System.out.println("####################ENVIOU PARA O: " + adv.getId());
						System.out.println("Resposta: " +
								cliente.enviar(adv.getIp(), 4040 + adv.getId(), ProtocoloJogadores.enviarJogador(1, Controller.getInstance().getEuJogador()))); 
					}
				}
			}).start();
			break;
			
		case Acao.RESPOSTA_PARA_O_ANIVERSARIANTE:
			if(ProtocoloJogadores.getIdResponderAniversariante(data) == controller.getEuJogador().getId()) { // se eu for o aniversariante
				System.out.println("----------------EU SOU O ANIVERSARIANTE -------------------");
				System.out.println("++++++++++++++++++++++++=RECEBEU 100 reais de " +ProtocoloJogadores.getId(data));
				controller.getEuJogador().setSaldo(controller.getEuJogador().getSaldo()+100); //TODO Transformar isso em um m�todo
				// Envia meu estado para os outros jogadores
				multiCastAdversarios(ProtocoloJogadores.enviarJogador(1, Controller.getInstance().getEuJogador()));
				janelaTabuleiro.atualizaJogadores(
		    			Controller.getInstance().getEuJogador(), 
						Controller.getInstance().getAdversarios());
			}
			break;
			
		case Acao.ATUALIZAR_SORTE_GRANDE:
			float quantia = ProtocoloJogadores.formatarSorteGrandeQuantia(data);
			controller.addQuantiaParaSorteGrande(quantia);
			// Atualiza a view com uma nova quantia do "Sorte Grande"
			janelaTabuleiro.setQuantiaSorteGrande(controller.getTotalSorteGrande());
			break;
			
		case Acao.INICIO_MARATONA_BENEFICENTE:
			int dado = Util.randInt(1, 6);
			float valor = dado * 100;
			
			janelaTabuleiro.showJogarMaratonaBeneficente();
			janelaTabuleiro.showJogarMaratonaBeneficente(dado, valor);

			controller.addQuantiaParaSorteGrande(valor); // Atualiza a quantia da sorte grande
			controller.setHabilitarMaratonaBeneficente(true); // Habilita a Maratono Beneficente
			
			// Atualizar view do sorte grande
			janelaTabuleiro.setQuantiaSorteGrande(controller.getTotalSorteGrande());
			
			// Envia a quantia a ser somada na "Sorte Grande" para os outros jogadores
			multiCastAdversarios(ProtocoloJogadores.enviarSorteGrandeQuantia(
					Acao.ATUALIZAR_SORTE_GRANDE,
					controller.getEuJogador().getId(),
					valor));
			break;
			
		case Acao.FIM_MARATONA_BENEFICENTE:
			controller.zerarSorteGrande();
			controller.setHabilitarMaratonaBeneficente(false);
			
			janelaTabuleiro.setQuantiaSorteGrande(0);
			janelaTabuleiro.showDialogJogadorVencedorMaratonaBeneficente(ProtocoloJogadores.getId(data));
			break;
			
		case Acao.PAGUEI_VIZINHO:
			if(ProtocoloJogadores.getId(data) == Controller.getInstance().getEuJogador().getId()) { //Se eu for o vizinho que pagaram
				controller.getEuJogador().setSaldo(controller.getEuJogador().getSaldo()+100);
				multiCastAdversarios(ProtocoloJogadores.enviarJogador(1, Controller.getInstance().getEuJogador()));
				janelaTabuleiro.atualizaJogadores(
		    			Controller.getInstance().getEuJogador(), 
						Controller.getInstance().getAdversarios());
			}
			break;
		default:
			break;
		}
	}
    
    private class SalaDeEsperaRequestCallback implements OnRequest {

		@Override
		public void onUpdate(String data) {
			System.out.println("RECEBIDO: " + data);
			Controller controlador = Controller.getInstance();

			Jogador euJogador = Protocolo.SalaEspera.getEuJogador(data);
			ArrayList<Jogador> adversarios = Protocolo.SalaEspera.getAdversarios(data);

			controlador.setEuJogador(euJogador);
			controlador.setAdversarios(adversarios);

			janelaEspera.atualizarListaJogadores(euJogador, adversarios);
		}

		@Override
		public void onClose() {
	    	janelaTabuleiro = new JanelaPrincipal(Jogo.this);
	    	janelaTabuleiro.iniciar();
	    	janelaTabuleiro.atualizaJogadores(
	    			Controller.getInstance().getEuJogador(), 
					Controller.getInstance().getAdversarios());
	    	
			inicializaServidor();
			janelaEspera.fechar();
			
			// Se deve tomar a inciativa ao começar a partida
			if (Controller.getInstance().getEuJogador().getId() == 0) {
				janelaTabuleiro.eSuaVez();
				janelaTabuleiro.habilitaJogar(true);
			}
		}
		
		@Override
		public void onErro(String erro) {
			janelaEspera.setInfo("Erro de conexão");
		}
	}

	@Override
	public void onDadoLancado() {
		System.out.println("%%%%%%%%%%%% APERTOU O BOTAO DE JOGAR O DADO");
		
		Controller controller = Controller.getInstance();
		
		int valorDado = controller.lancarDado(); // Lanca o dado
		int posicao = controller.getEuJogador().getPosicaoPino();
		
		if (controller.isHabilitarMaratonaBeneficente() && valorDado == 6) {
			multiCastAdversarios(ProtocoloJogadores.enviarFimMaratonaBeneficente(
					Acao.FIM_MARATONA_BENEFICENTE, 
					controller.getEuJogador().getId()));
			
			// Atualiza o novo saldo com o dinheiro da Maratona Beneficente
			controller.getEuJogador().setSaldo(
					controller.getEuJogador().getSaldo() + controller.getTotalSorteGrande());
			
			controller.zerarSorteGrande(); // Zera o montante da sorte grande
			janelaTabuleiro.setQuantiaSorteGrande(0); // Atualiza a view o novo montante do Sorte Grande
			janelaTabuleiro.showDialogVencedorMaratonaBeneficente();
		}
		
		if (posicao == 2) { //Acao casa premio
			controller.casaPremio(controller.getEuJogador());
			janelaTabuleiro.casaPremio();
		} else if(posicao == 4 || posicao == 12 || posicao == 15 || posicao == 25) { //Acao casa Compra e Entetenimento
			pegaCartaCompraEntretenimento();
		} else if(posicao == 9 || posicao == 17 || posicao == 23 || posicao == 26 || posicao == 29) { //Acao casa Achou um Comprador
			janelaTabuleiro.casaAchouComprador(controller.casaAchouComprador(controller.getEuJogador()));
		} else if(posicao == 21) { //Acao casa vende-se
			controller.debita(100*valorDado, controller.getEuJogador()); 
			janelaTabuleiro.casaVendese();
			pegaCartaCompraEntretenimento();
		} else if(posicao == 10) { //Acao Aniversario
			janelaTabuleiro.aniversario();
			multiCastAdversarios(ProtocoloJogadores.requisitar(Acao.REQUISICAO_ANIVERSARIO, Controller.getInstance().getEuJogador().getId()));
		} else if(posicao == 1 || posicao == 3 || posicao == 5 || posicao == 11 || posicao == 16 || posicao == 19 
			||	posicao == 22 || posicao == 24) {
			ArrayList<String> cartas = controller.casaCorreio(controller.getEuJogador());
			janelaTabuleiro.casaCorreio(cartas); //mostra um comboBox, vc vai escolhendo uma por uma
			//criar onAcaoCorreio(String carta), aqui, pra realizar a acao da carta

		} else if (posicao == 7 || posicao == 14 || posicao == 18 || posicao == 28) {
			// Casas: praia de domingo, ajude a amazonia, lanchonete, compras no shopping
			float quantia = 100;
			
			switch (posicao) { // Eu sei, e' uma pessima abordagem
			case 7:
				janelaTabuleiro.showDialogSorteGrande("Praia de Domingo", 100);
				break;

			case 14:
				janelaTabuleiro.showDialogSorteGrande("Ajude a Floresta Amazônica", 100);
				break;
			
			case 18:
				janelaTabuleiro.showDialogSorteGrande("Lanchonete", 100);
				break;
				
			case 28:
				janelaTabuleiro.showDialogSorteGrande("Compras no Shopping", 100);
				break;
			
			default:
				break;
			}
			
			if (!controller.pagarSorteGrandeEuJogador(quantia, false)) { // Sem saldo
				janelaTabuleiro.showDialogEmprestimo(quantia);
				controller.pagarSorteGrandeEuJogador(quantia, true);
			}
			
			// Atualiza a view com uma nova quantia do "Sorte Grande"
			janelaTabuleiro.setQuantiaSorteGrande(controller.getTotalSorteGrande());
			
			// Envia a quantia a ser somada na "Sorte Grande" para os outros jogadores
			multiCastAdversarios(ProtocoloJogadores.enviarSorteGrandeQuantia(
					Acao.ATUALIZAR_SORTE_GRANDE,
					controller.getEuJogador().getId(),
					quantia));
			
		} else if (posicao == 30) { // Maratona beneficente
			multiCastAdversarios(ProtocoloJogadores.enviarInicioMaratonaBeneficente(
					Acao.INICIO_MARATONA_BENEFICENTE, 
					controller.getEuJogador().getId()));
			
			janelaTabuleiro.showDialogInicioMaratonaBeneficente();
		}

		// Envia meu estado para os outros jogadores
		multiCastAdversarios(ProtocoloJogadores.enviarJogador(Acao.ATUALIZACAO_ESTADO, 
				Controller.getInstance().getEuJogador()));
		
		janelaTabuleiro.atualizaJogadores(
    			Controller.getInstance().getEuJogador(), 
				Controller.getInstance().getAdversarios());
	}
	@Override
	public void onAcaoCorreio(String carta) {
//		Controller.getInstance().acaoCasaCorreio(carta);	
		final Controller controller = Controller.getInstance();
		switch (carta) {
		case "contas":
			System.out.println("CONTAS==================");
			String conta = controller.geraNovaContaAleatoria();
			controller.debita(controller.getValorConta(conta), controller.getEuJogador());
			janelaTabuleiro.conta(conta); 
			multiCastAdversarios(ProtocoloJogadores.enviarJogador(1, controller.getEuJogador()));
			janelaTabuleiro.atualizaJogadores(
	    			Controller.getInstance().getEuJogador(), 
					Controller.getInstance().getAdversarios());
			break;
		case "pague a um vizinho agora":
			System.out.println("PAGUE UM VIZINHO AGORA==================");	
			final int idVizinho = janelaTabuleiro.pagueUmVizinhoAgora();//Pega o valor do id do vizinho a se pagar, da GUI
			controller.debita(100, controller.getEuJogador()); // Debita o valor
			multiCastAdversarios(ProtocoloJogadores.enviarJogador(1, controller.getEuJogador())); //Avisa pra os demais adversarios o debito
			(new Thread() {
				@Override
				public void run() {
					Cliente cliente = new Cliente();
					//Envia ao aniversariante confirmando que pagou
					System.out.println("---------------------------------------ENVIOU PAGAMENTO PARA O VIZINHO: " +idVizinho); 
					System.out.println("Resposta: " + 
							cliente.enviar(controller.getAdversario(idVizinho).getIp(), 4040 +idVizinho, ProtocoloJogadores.pagarVizinho(idVizinho))); //Envia sinal de pagamento para o vizinho
				}
			}).start();
			break;
		case "dinheiro extra":
			System.out.println("dinheiro extra==================");
			break;
		case "doacoes":
			System.out.println("doacoes==================");
			break;
		case "cobranca monstro":
			System.out.println("cobranca monstro==================");
			break;
		case "va para frente agora":
			System.out.println("va para frente agora==================");
			break;
		default:
			break;
		}
	}
	private void multiCastAdversarios(final String msg) {
		(new Thread() {
			@Override
			public void run() {
				Cliente cliente = new Cliente();
				for (Jogador adv : Controller.getInstance().getAdversarios()) {
					System.out.println("####################ENVIOU PARA O: " + adv.getId()); 
					System.out.println("Resposta: " + 
							cliente.enviar(adv.getIp(), 4040 + adv.getId(), msg)); //mudar pra jogador.getIP()
				}

			}
		}).start();
	}
	public void pegaCartaCompraEntretenimento() {
		Controller controller = Controller.getInstance();
		CartaCompra cartaCompra = controller.getCompraEntretenimento();
		boolean eEmprestimo =  controller.necessitaEmprestimo(cartaCompra, controller.getEuJogador());
		if(janelaTabuleiro.casaCompraEntretenimento(cartaCompra.toString(), eEmprestimo)) {
			controller.acaoCompraEntretenimento(eEmprestimo, controller.getEuJogador(), cartaCompra);
		}
	}

	
}