package network;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Cliente {
	
	/**
	 * Envia mensagem para um servidor
	 * 
	 * @param ip
	 * @param porta
	 * @param mensagem
	 * @return Retorna true se foi enviado com sucesso, caso contrário retorna false
	 */
	public boolean enviar(String ip, int porta, String mensagem) {
	    Socket socket = null;
	    BufferedWriter out;
	    try {
	        socket = new Socket(ip, porta);
	        out = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
	        out.write(mensagem);
	        out.newLine();
	        out.flush();
	    } catch (IOException e) {
	    	e.printStackTrace();
	        return false;
	        
	    } finally {
	        try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	    return true;
	}
}
