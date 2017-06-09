package network;
import java.io.IOException;

import network.Servidor.OnServidor;


public class TestaRede {

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
					System.out.println("Callback: " + data);
				}
				
				@Override
				public void onErro() { }
			});
		} catch (IOException e) {
			System.err.println("Erro ao inciar o servidor");
		}
		testarEnviar(server.getPort());
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
