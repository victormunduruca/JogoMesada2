package uefs.br.ecomp.mi.model;

import java.util.ArrayList;

import uefs.br.ecomp.mi.OnSalaEspera;

/**
 * Essa classe implementa uma abstração de uma sala de espera
 * 
 * @author franz
 *
 */
public class Sala {
	private ArrayList<Jogador> jogadores = new ArrayList<Jogador>();
	
	public Sala() {
		
	}
	
	/**
	 * Retorna o total de jogadores na sala de espera
	 * 
	 * @return
	 */
	public int getTotalJogadores() {
		return jogadores.size();
	}
	
	/**
	 * Adiciona um jogador a sala
	 * 
	 * @param jogador
	 */
	public void addJogador(Jogador jogador) {
		jogadores.add(jogador);
		notificarJogadoresNovoJogador(jogador.getNome());
		
		if (getTotalJogadores() == 6) {
			notificarJogadoresComecoJogo();
		}
	}
	
	private void notificarJogadoresComecoJogo() {
		for (int i = 0; i < jogadores.size(); i++) {
			if (jogadores.get(i).getOnEsperaCallback() != null) {
				jogadores.get(i).getOnEsperaCallback().onJogoComecou(this);
			}
		}
	}
	
	private void notificarJogadoresNovoJogador(String nome) {
		for (int i = 0; i < jogadores.size(); i++) {
			if (jogadores.get(i).getOnEsperaCallback() != null) {
				jogadores.get(i).getOnEsperaCallback().onNovoJogadorEntrou(this, nome);
			}
		}
	}
}
