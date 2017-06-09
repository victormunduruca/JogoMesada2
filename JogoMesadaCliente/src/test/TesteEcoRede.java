package test;
import java.io.IOException;

import network.Cliente;
import network.Servidor;
import network.Servidor.OnServidor;


public class TesteEcoRede {

    private static Servidor server;
    private static int porta = 4040;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			server = new Servidor(porta);
			server.run(new OnServidor() {
				
				@Override
				public void onDadoRecebido(String data) {
					// XXX Apagar quando você ler, Victor
					// É aqui onde começa a lógica do jogo.
					// Falta apenas formatar esse "data" bruto que representa o protocolo 
					// para as informações de jogo (semelhante o que eu fiz em network.Protocolo
					// para implementar a sala de espera
					System.out.println("Eco recbedido: " + data);
				}
				
				@Override
				public void onErro() { }
			});
		} catch (IOException e) {
			System.err.println("Erro ao inciar o servidor");
		}
		
		testarEnviar(server.getPort()); // Envia uma mensagem para si mesmo
	}
	
    public static void testarEnviar(final int porta) {
        (new Thread() {
            @Override
            public void run() {
            	Cliente cliente = new Cliente();
            	try {
					Thread.sleep(500);
					System.out.println("Resposta: " + 
							cliente.enviar("127.0.0.1", porta, "Testando pingback"));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
        }).start();
    }

}
