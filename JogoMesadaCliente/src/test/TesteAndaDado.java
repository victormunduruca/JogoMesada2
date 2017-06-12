package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import controller.Controller;
import excpeptions.IdNaoEncontradoException;
import model.Jogador;
import network.Cliente.OnRequest;
import network.Servidor.OnServidor;
import network.Cliente;
import network.Protocolo;
import network.Servidor;
import view.JanelaPrincipal;
import view.JanelaSalaDeEspera;

public class TesteAndaDado {
	
	private static JanelaSalaDeEspera janelaEspera;
	private static JanelaPrincipal janelaPrincipal;
//	private static Cliente cliente;
    private static Servidor server;
    private static int porta = 4040;
    
//    public TesteAndaDado() throws IdNaoEncontradoException {
//    	
//    }
//    
    public static void main(String[] args) throws IdNaoEncontradoException {
  
    
    	JanelaPrincipal.initialize();
    	janelaEspera = new JanelaSalaDeEspera();
    	janelaEspera.iniciar(new RequestCallback());
    	
    	
    	
    	
//    	try {
//    		server = new Servidor(porta);
//    		server.run(new OnServidor() {
//    			
//    			@Override
//    			public void onDadoRecebido(String data) {
//    				// XXX Apagar quando você ler, Victor
//    				// É aqui onde começa a lógica do jogo.
//    				// Falta apenas formatar esse "data" bruto que representa o protocolo 
//    				// para as informações de jogo (semelhante o que eu fiz em network.Protocolo
//    				// para implementar a sala de espera
//    				System.out.println("Eco recbedido: " + data);
//    			}
//    			
//    			@Override
//    			public void onErro() { }
//    		});
//    	} catch (IOException e) {
//    		System.err.println("Erro ao inciar o servidor");
//    	}
	}
    private static class RequestCallback implements OnRequest {

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
			System.out.println("DEU CLOOSSEEEE +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			System.out.println("Meu ip: " +Controller.getInstance().getEuJogador().getIp());
			JanelaPrincipal.iniciar();
			//Controller.getInstance().metodoTeste();
			if(Controller.getInstance().getEuJogador().getId() == 1) {
				System.out.println("PRIMEIRA VEZZZZ +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				//JanelaPrincipal.eSuaVez();
				System.out.println("-------------------------------- SUA VEZ ---------------------------------------:::: " +Controller.getInstance().getEuJogador().getId());
				JanelaPrincipal.habilitaJogar();
			}
			try {
				server = new Servidor(4040 + Controller.getInstance().getEuJogador().getId());
				server.run(new OnServidor() {
					
					@Override
					public void onDadoRecebido(String data) {
						System.out.println("_________ RECEBEU DADOS DO__________"+ProtocoloJogadores.getId(data));
						System.out.println("Eco recbedido: " + data);
						Jogador jogadorUpdate = Controller.getInstance().getAdversario(ProtocoloJogadores.getId(data));
						if(jogadorUpdate == null) {
							System.out.println("Erro, jogador nulo");
							return;
						}
						
////						System.out.println("id recebido: " +ProtocoloJogadores.getId(data));
//						System.out.println("posicao recebida: "+ProtocoloJogadores.getPosicao(data));
//						System.out.println("saldoRecebido: " +ProtocoloJogadores.getSaldo(data));
//						
						jogadorUpdate.setPosicaoPino(ProtocoloJogadores.getPosicao(data));
						jogadorUpdate.setSaldo(ProtocoloJogadores.getSaldo(data));
						Controller.getInstance().notifyObserver(jogadorUpdate);
						
						if(ProtocoloJogadores.getId(data)+1 == Controller.getInstance().getEuJogador().getId() ||  daVolta(data)) {
//							JanelaPrincipal.eSuaVez();
							System.out.println("-------------------------------- SUA VEZ ---------------------------------------:::: " +Controller.getInstance().getEuJogador().getId());
							JanelaPrincipal.habilitaJogar();
						}
					}
					@Override
					public void onErro() { }
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//fechar();
		}
		public boolean daVolta(String data) {
			if(ProtocoloJogadores.getId(data) == 6 && Controller.getInstance().getEuJogador().getId() == 1) {
				System.out.println("DEU A VOLTA \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
				return true;
			}
			return false;
		}
//		public void jogar() {
//			//JanelaPrincipal.habilitarBtnRodarDado();
//			
//		}
		@Override
		public void onErro(String erro) {
			janelaEspera.setInfo("Erro de conexão");
		}
	}
}