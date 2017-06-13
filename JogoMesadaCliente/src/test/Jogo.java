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
		case 1:
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
			//iniciaLogicaDeJogo();
			janelaEspera.fechar();
		}
		
		@Override
		public void onErro(String erro) {
			janelaEspera.setInfo("Erro de conexão");
		}
	}

	@Override
	public void onDadoLancado() {
		Controller controller = Controller.getInstance();
		
		int valorDado = controller.lancarDado(); // Lanca o dado
		int posicao = controller.getEuJogador().getPosicaoPino();
		if(posicao == 2) { //Acao casa premio
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
			
		}
		
		System.out.println("%%%%%%%%%%%% APERTOU O BOTAO DE JOGAR O DADO");

		// Envia meu estado para os outros jogadores
		multiCastAdversarios(ProtocoloJogadores.enviarJogador(1, Controller.getInstance().getEuJogador()));
		
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
			break;
		case "pague a um vizinho agora":
			System.out.println("PAGUE UM VIZINHO AGORA==================");	
			final int idVizinho = janelaTabuleiro.pagueUmVizinhoAgora();
			controller.debita(100, controller.getEuJogador());
			multiCastAdversarios(ProtocoloJogadores.enviarJogador(1, controller.getEuJogador()));
			(new Thread() {
				@Override
				public void run() {
					Cliente cliente = new Cliente();
					//Envia ao aniversariante confirmando que pagou
					System.out.println("---------------------------------------ENVIOU PRESENTE PAGAMENTO PARA: " +idVizinho);
					System.out.println("Resposta: " + 
							cliente.enviar(controller.getAdversario(idVizinho).getIp(), 4040 +idVizinho, ProtocoloJogadores.pagarVizinho(idVizinho))); 
				}
			}).start();
		//	controller.acaoPagueVizinhoAgora(controller.getEuJogador());
			//retira o valor no controller
			//multicast adversarios
			//avisa pra vizinho
			//vizinho adiciona o valor
			//multicast adversarios
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
//	private void enviaMeuEstado() {
//		(new Thread() {
//			@Override
//			public void run() {
//				Cliente cliente = new Cliente();
//				for (Jogador adv : Controller.getInstance().getAdversarios()) {
//					System.out.println("####################ENVIOU PARA O: " + adv.getId()); 
//					System.out.println("Resposta: " + 
//							cliente.enviar(adv.getIp(), 4040 + adv.getId(), ProtocoloJogadores.enviarJogador(1, Controller.getInstance().getEuJogador()))); //mudar pra jogador.getIP()
//				}
//
//			}
//		}).start();
//	}
//	private void requisitarAniversario() {
//		// Envia a minha posicao para os outros jogadores
//				(new Thread() {
//					@Override
//					public void run() {
//						Cliente cliente = new Cliente();
//						for (Jogador adv : Controller.getInstance().getAdversarios()) {
//							System.out.println("####################ENVIOU PARA O: " + adv.getId());
//							System.out.println("Resposta: " +
//									cliente.enviar(adv.getIp(), 4040 + adv.getId(), ProtocoloJogadores.requisitar(Acao.REQUISICAO_ANIVERSARIO, Controller.getInstance().getEuJogador().getId()))); //mudar pra jogador.getIP()
//						}
//
//					}
//				}).start();
//	}

	public void pegaCartaCompraEntretenimento() {
		Controller controller = Controller.getInstance();
		CartaCompra cartaCompra = controller.getCompraEntretenimento();
		boolean eEmprestimo =  controller.necessitaEmprestimo(cartaCompra, controller.getEuJogador());
		if(janelaTabuleiro.casaCompraEntretenimento(cartaCompra.toString(), eEmprestimo)) {
			controller.acaoCompraEntretenimento(eEmprestimo, controller.getEuJogador(), cartaCompra);
		}
	}

	
}