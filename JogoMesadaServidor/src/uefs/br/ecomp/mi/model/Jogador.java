package uefs.br.ecomp.mi.model;

import uefs.br.ecomp.mi.OnSalaEspera;

public class Jogador {
	private int id;
	private float saldo = 3000;
	private String nome;
	private String ip;
	
	private OnSalaEspera onEsperaCallback;
	
	public Jogador(String ip) {
		this.ip = ip;
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
	public float getSaldo() {
		return saldo;
	}
	public void setSaldo(float saldo) {
		this.saldo = saldo;
	}
	public String getNome() {
		return "Jogador" + id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
}
