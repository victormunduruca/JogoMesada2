package network;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by franz on 3/29/17.
 */
public class Servidor {

	private ServerSocket serverSocket;
	private int port;
	private boolean isRunning = false;

	public Servidor(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		isRunning = true;
		this.port = port;
	}

	/**
	 * Inicia a execução do servidor
	 */
	public void run() {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.print("Servidor iniciado ");
				printLocalIPv4();
				while (isRunning) {
					try {
						// Espera por uma requisição do cliente
						Socket socket = serverSocket.accept();

						// Lança uma thread para processar a corrente requisição
						Thread thread = new Thread(new RequestWorker(socket));
						thread.start();

					} catch (IOException e) {
						isRunning = false;
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * Para a execução do servidor
	 */
	public void stop() {
		if (serverSocket != null && !serverSocket.isClosed()) {
			try {
				serverSocket.close();
				isRunning = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Retorna a porta designada para o servidor
	 *
	 * @return porta atual
	 */
	public int getPort() {
		return this.port;
	}

	private void printLocalIPv4() {
		try {
			InetAddress inet = InetAddress.getLocalHost();
			InetAddress[] ips = InetAddress.getAllByName(inet.getCanonicalHostName());
			if (ips  != null ) {
				for (int i = 0; i < ips.length; i++) {
					System.out.println("(" + ips[i] + ":" + port + ")");
				}
			}
		} catch (UnknownHostException e) {
			System.out.println("Erro ao recuperar o endereço IPv4 local");
		}

	}
}
