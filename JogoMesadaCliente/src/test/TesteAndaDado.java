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
			JanelaPrincipal.iniciar();
			Controller.getInstance().metodoTeste();
			if(Controller.getInstance().getEuJogador().getId() == 1) {
				JanelaPrincipal.eSuaVez();
				jogar();
			}
			try {
				server = new Servidor(4040 + Controller.getInstance().getEuJogador().getId());
				server.run(new OnServidor() {
					
					@Override
					public void onDadoRecebido(String data) {
						System.out.println("Recebeuu dadosss");
						System.out.println("Eco recbedido: " + data);
						Jogador jogadorUpdate = Controller.getInstance().getAdversario(Integer.valueOf(ProtocoloJogadores.getId(data)));
						if(jogadorUpdate == null) {
							System.out.println("Erro, jogador nulo");
							return;
						}
						
						System.out.println("id recebido: " +ProtocoloJogadores.getId(data));
						System.out.println("posicao recebida: "+ProtocoloJogadores.getPosicao(data));
						System.out.println("saldoRecebido: " +ProtocoloJogadores.getSaldo(data));
						
						jogadorUpdate.setPosicaoPino(Integer.valueOf(ProtocoloJogadores.getPosicao(data)));
						jogadorUpdate.setSaldo(Float.valueOf(ProtocoloJogadores.getSaldo(data)));
						Controller.getInstance().notifyObserver(jogadorUpdate);
						
						if(Integer.valueOf(ProtocoloJogadores.getId(data))+1 == Controller.getInstance().getEuJogador().getId() || Integer.valueOf(ProtocoloJogadores.getId(data))-4 == Controller.getInstance().getEuJogador().getId() ) {
							JanelaPrincipal.eSuaVez();
							jogar();
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
		public void jogar() {
			JanelaPrincipal.habilitarBtnRodarDado();
			JanelaPrincipal.jogarDado(new ActionListener() {			
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
					Jogador euJogador = Controller.getInstance().jogarDado();
					Controller.getInstance().notifyObserver(euJogador);
					 (new Thread() {
				            @Override
				            public void run() {
				            	Cliente cliente = new Cliente();
				            	//Thread.sleep(500);
				            	for(Jogador jogador : Controller.getInstance().getAdversarios()) {
				            		System.out.println("Resposta: " + 
											cliente.enviar("127.0.0.1", 4040+jogador.getId(), ProtocoloJogadores.enviarJogador(1, Controller.getInstance().getEuJogador())));
				            	}
								
				            }
				        }).start();
					 JanelaPrincipal.desabilitarBtnRodarDado();
				}
				
			});
		}
		@Override
		public void onErro(String erro) {
			janelaEspera.setInfo("Erro de conexão");
		}
	}
}