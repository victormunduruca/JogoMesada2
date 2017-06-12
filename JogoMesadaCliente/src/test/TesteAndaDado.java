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
  
    
    	//JanelaPrincipal.initialize();
    	janelaEspera = new JanelaSalaDeEspera();
    	janelaEspera.iniciar(new RequestCallback());
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
			inicializaJogo();
		}
		public void inicializaJogo() {
			//JanelaPrincipal.iniciar();
			if(Controller.getInstance().getEuJogador().getId() == 1) {
				//JanelaPrincipal.habilitaJogar();
			}
			inicializaServidor();
		}
		public void inicializaServidor() {
			try {
				server = new Servidor(4040 + Controller.getInstance().getEuJogador().getId());
				server.run(new OnServidor() {
					
					@Override
					public void onDadoRecebido(String data) {
						System.out.println("Eco recbedido: " + data);
						Jogador jogadorUpdate = Controller.getInstance().getAdversario(ProtocoloJogadores.getId(data));
						if(jogadorUpdate == null) {
							System.out.println("Erro, jogador nulo");
							return;
						}
						
						jogadorUpdate.setPosicaoPino(ProtocoloJogadores.getPosicao(data));
						jogadorUpdate.setSaldo(ProtocoloJogadores.getSaldo(data));
						Controller.getInstance().notifyObserver(jogadorUpdate);
						
						if(ProtocoloJogadores.getId(data)+1 == Controller.getInstance().getEuJogador().getId() ||  daVolta(data)) {
							//JanelaPrincipal.habilitaJogar();
						}
					}
					@Override
					public void onErro() { }
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public boolean daVolta(String data) {
			if(ProtocoloJogadores.getId(data) == 6 && Controller.getInstance().getEuJogador().getId() == 1) {
				return true;
			}
			return false;
		}
		@Override
		public void onErro(String erro) {
			janelaEspera.setInfo("Erro de conexão");
		}
	}
}