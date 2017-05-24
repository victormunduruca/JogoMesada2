package uefs.br.ecomp.mi.controlador;

import java.util.ArrayList;

import uefs.br.ecomp.mi.OnSalaEspera;
import uefs.br.ecomp.mi.model.Jogador;
import uefs.br.ecomp.mi.model.Sala;

public class Controlador {
	
	private static Controlador selfInstance;
	
	private ArrayList<Sala> salas = new ArrayList<Sala>();
	
	private Controlador () { } 
	
	public static Controlador getInstance() {
		if (selfInstance == null) {
			selfInstance = new Controlador();
		}
		return selfInstance;
	}
	
	public void registrarParaSala(String ip, int porta, OnSalaEspera onSalaEsperaCallback) {
		Sala ultimaSala;
		Jogador jogador = new Jogador(ip, porta);
		jogador.setOnEsperaCallback(onSalaEsperaCallback);
		
		if (salas.size() != 0) {
			ultimaSala = salas.get(salas.size() - 1);
			if (ultimaSala.getTotalJogadores() == 6) { // Sala cheia
				ultimaSala = new Sala(); // Cria nova sala
				
				jogador.setId(ultimaSala.getTotalJogadores() + 1);
				ultimaSala.addJogador(jogador);
				
				salas.add(ultimaSala);
			} else {
				jogador.setId(ultimaSala.getTotalJogadores() + 1);
				ultimaSala.addJogador(jogador);
			}
		} else {
			ultimaSala = new Sala();
			
			jogador.setId(ultimaSala.getTotalJogadores() + 1);
			ultimaSala.addJogador(jogador); // Add jogador
			
			salas.add(ultimaSala);
		}
	}
}
