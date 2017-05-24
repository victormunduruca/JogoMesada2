package uefs.br.ecomp.mi.model;

import uefs.br.ecomp.mi.OnSalaEspera;

public class Jogador {
	private int id;
	private float real;
	private String nome;
	private String ip;
	private int porta;
	private OnSalaEspera onEsperaCallback;
	
	public Jogador(String ip, int porta) {
		this.ip = ip;
		this.porta = porta;
	}
	public OnSalaEspera getOnEsperaCallback() {
		return onEsperaCallback;
	}
	public void setOnEsperaCallback(OnSalaEspera onEsperaCallback) {
		this.onEsperaCallback = onEsperaCallback;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getReal() {
		return real;
	}
	public void setReal(float real) {
		this.real = real;
	}
	public String getNome() {
		return "Jogador" + id;
	}	
}
