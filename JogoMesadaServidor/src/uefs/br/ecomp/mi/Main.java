package uefs.br.ecomp.mi;

import java.io.IOException;

public class Main {

    private static Servidor server;
    private static int porta = 7070;
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
	}
}
