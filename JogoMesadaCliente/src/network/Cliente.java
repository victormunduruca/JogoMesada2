package network;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {
	
	public interface OnRequest {
		void onUpdate(String data);
		void onClose();
		void onErro(String erro);
	}
	
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
	    	//e.printStackTrace();
	        return false;
	        
	    } finally {
	        try {
	        	if (socket != null) {
	        		socket.close();
	        	}
			} catch (IOException e) {
				return false;
				//e.printStackTrace();
			}
	    }
	    return true;
	}
	
	public void requisitar(final String ip, final int porta, final OnRequest callback) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Socket socket = new Socket(ip, porta);
			        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			        String line;

			        while((line = reader.readLine()) != null){
			            callback.onUpdate(line);
			        }
			        
			        callback.onClose();
			        reader.close();
				} catch (IOException e) {
					callback.onErro("Erro de conexão");
				};
			}
		}).start();
	}
}
