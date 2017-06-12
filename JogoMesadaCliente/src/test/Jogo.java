package test;

import java.io.IOException;
import java.util.ArrayList;

import controller.Controller;
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
    	janelaEspera = new JanelaSalaDeEspera();
    	janelaEspera.iniciar(new SalaDeEsperaRequestCallback());
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
	
	private void executaLogicaDeJogo(String data) {
		int acaoDeJogo = ProtocoloJogadores.getAcao(data);
		
		switch (acaoDeJogo) {
		case 1:
			// Atualiza o adversario no model
			Controller.getInstance().atualizarAdversario(
					ProtocoloJogadores.getId(data), 
					ProtocoloJogadores.getPosicao(data), 
					ProtocoloJogadores.getSaldo(data));
			
			// Atualiza os jogadores na view 
			// (Atualmente atualizando apenas a lista. Podemos usar para atualizar o tabuleiro também)
	    	janelaTabuleiro.atualizaJogadores(
	    			Controller.getInstance().getEuJogador(), 
					Controller.getInstance().getAdversarios());
	    	
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
		if(posicao == 2) {
			controller.casaPremio(controller.getEuJogador());
			janelaTabuleiro.casaPremio();
		} else if(posicao == 4 || posicao == 12 || posicao == 15 || posicao == 25) {
			CartaCompra cartaCompra = controller.getCompraEntretenimento();
			boolean eEmprestimo =  controller.necessitaEmprestimo(cartaCompra, controller.getEuJogador());
			if(janelaTabuleiro.casaCompraEntretenimento(cartaCompra.toString(), eEmprestimo)) {
				controller.acaoCompraEntretenimento(eEmprestimo, controller.getEuJogador(), cartaCompra);
			}
		} else if(posicao == 9 || posicao == 17 || posicao == 23 || posicao == 26 || posicao == 29) {
			janelaTabuleiro.casaAchouComprador(controller.casaAchouComprador(controller.getEuJogador()));
		} else if(posicao == 21) {
			controller.casaVendese(valorDado, controller.getEuJogador());
		}
		
		System.out.println("%%%%%%%%%%%% APERTOU O BOTAO DE JOGAR O DADO");

		// Envia a minha posicao para os outros jogadores
		(new Thread() {
			@Override
			public void run() {
				Cliente cliente = new Cliente();
				for (Jogador adv : Controller.getInstance().getAdversarios()) {
					System.out.println("####################ENVIOU PARA O: " + adv.getId());
					System.out.println("Resposta: " + 
							cliente.enviar(adv.getIp(), 4040 + adv.getId(), ProtocoloJogadores.enviarJogador(1, Controller.getInstance().getEuJogador()))); //mudar pra jogador.getIP()
				}

			}
		}).start();
		
		janelaTabuleiro.atualizaJogadores(
    			Controller.getInstance().getEuJogador(), 
				Controller.getInstance().getAdversarios());
	}
}