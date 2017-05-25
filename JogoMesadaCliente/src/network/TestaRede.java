package network;
import java.io.IOException;


public class TestaRede {

    private static Servidor server;
    private static int porta = 9090;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			server = new Servidor(porta);
			server.run();
		} catch (IOException e) {
			System.err.println("Erro ao inciar o servidor");
		}
		testarEnviar();
	}
	
    public static void testarEnviar() {
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
