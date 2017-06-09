package network;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.Servidor.OnServidor;

/**
 * Created by franz on 4/17/17.
 */
public class RequestWorker implements Runnable {

    private Socket socket;
    private OnServidor callback;

    public RequestWorker(Socket socket, OnServidor callback) {
        this.socket = socket;
        this.callback = callback;
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
            String line = reader.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null && !line.isEmpty()) {
            	sb.append(line);
                line = reader.readLine();
            }

            callback.onDadoRecebido(sb.toString());
            
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
}
