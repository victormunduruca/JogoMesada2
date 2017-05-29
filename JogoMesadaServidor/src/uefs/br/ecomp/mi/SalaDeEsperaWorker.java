package uefs.br.ecomp.mi;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uefs.br.ecomp.mi.controlador.Controlador;
import uefs.br.ecomp.mi.model.Jogador;
import uefs.br.ecomp.mi.model.Sala;


public class SalaDeEsperaWorker implements Runnable {

	private boolean emEspera = true;
	
    private Socket socket;
    private Jogador correnteJogador; 

    public SalaDeEsperaWorker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // Abre um stream de dados para atual requisição do cliente
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            
            String ipPorta = socket.getRemoteSocketAddress().toString();
            String ip = formatarIp(ipPorta);
            int porta = Integer.valueOf(formatarPorta(ipPorta));
            
            System.out.println("Cliente conectado: " + ip + ":" + porta + " conectado!");

            // Faz a leitura dos dados passados na requisição do cliente
            //final StringBuffer request = new StringBuffer();
            /*List<String> request = new ArrayList<String>();
            String line = reader.readLine();
            while (line != null && !line.isEmpty()) {
                System.out.println(line);
                request.add(line);
                line = reader.readLine();
            }*/
            
            //String line = reader.readLine();
            //System.out.println(">> " + line);
            
            // Monta um mecânismo de resposta 
            
            Controlador controller = Controlador.getInstance();
            controller.registrarParaSala(ip, new OnSalaEspera() {
				@Override
				public void onInicioCorrenteJogadorNaSala(Sala sala,
						Jogador jogador) {
					correnteJogador = jogador;
				}
				
				@Override
				public void onNovoJogadorEntrou(Sala sala, String nome) {
					System.out.println("Um novo jogador entrou: " + nome + "\n");
					
		            StringBuffer response = new StringBuffer();
					response.append(formatJogadores(Protocolo.ACAO_NOVO_JOGADOR_ADICIONADO, sala));
					try {
						socket.getOutputStream().write(response.toString().getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onJogadorSaiu(Sala sala, String nome) { }
				
				@Override
				public void onJogoComecou(Sala sala) {
					System.out.println("O jogo começou!");
		            StringBuffer response = new StringBuffer();
					response.append(formatJogadores(Protocolo.ACAO_INICIO_JOGO, sala));
					
		            // Imprime uma resposta para o cliente e encerra a conexão
		            try {
			            socket.getOutputStream().write(response.toString().getBytes());
						socket.getOutputStream().close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String formatJogadores(String acao, Sala sala) {
    	ArrayList<Jogador> jogadores = sala.getJogadores();
    	StringBuilder sb = new StringBuilder();
    	sb.append(acao + "\n");
    	sb.append(correnteJogador.getId() + "," + correnteJogador.getSaldo() + "," + correnteJogador.getIp() + "\n");
    	for (Jogador j : jogadores) {
    		if (correnteJogador.getId() != j.getId()) {
    			sb.append(j.getId() + "," + j.getSaldo() + "," + j.getIp() + "\n");
    		}
    	}
    	return sb.toString();
    }
    
    private String formatarIp(String str) {
    	str = str.replaceAll("/", "");
    	return str.substring(0, str.indexOf(":"));
    }
    
    private String formatarPorta(String str) {
    	str = str.replaceAll("/", "");
    	return str.substring(str.indexOf(":") + 1, str.length() - 1);
    }
}
