package network;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by franz on 4/17/17.
 */
public class RequestWorker implements Runnable {

    private Socket socket;

    public RequestWorker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // Abre um stream de dados para atual requisição do cliente
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            
            System.out.println("Cliente conectado: " + socket.getRemoteSocketAddress().toString() + " conectado!");

            // Faz a leitura dos dados passados na requisição do cliente
            //final StringBuffer request = new StringBuffer();
            List<String> request = new ArrayList<String>();
            String line = reader.readLine();
            while (line != null && !line.isEmpty()) {
                System.out.println(line);
                request.add(line);
                line = reader.readLine();
            }

            // Monta um mecânismo de resposta
            StringBuffer response = new StringBuffer();
            response.append("");

            // Imprime uma resposta para o cliente e encerra a conexão
            socket.getOutputStream().write(response.toString().getBytes());
            socket.getOutputStream().close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> getParams(String params) {
        Map<String, String> m = null;
        String[] p = params.split(",");
        if (p.length == 0) return m;
        String[] v;
        try {
            m = new HashMap<String, String>();
            for (int i = 0; i < p.length; i++) {
                v = p[i].split("=");
                m.put(v[0], v[1]);
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return m;
    }
}
