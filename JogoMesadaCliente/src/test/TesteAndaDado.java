package test;

import java.util.ArrayList;

import controller.Controller;
import excpeptions.IdNaoEncontradoException;
import model.Jogador;
import network.Cliente.OnRequest;
import network.Protocolo;
import network.Servidor;
import view.JanelaPrincipal;
import view.JanelaSalaDeEspera;

public class TesteAndaDado {
	
	private static JanelaSalaDeEspera janelaEspera;
	private static JanelaPrincipal janelaPrincipal;
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
			//fechar();
		}

		@Override
		public void onErro(String erro) {
			janelaEspera.setInfo("Erro de conexão");
		}
	}
}