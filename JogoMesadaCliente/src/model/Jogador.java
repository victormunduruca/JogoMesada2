package model;

import java.util.ArrayList;

public class Jogador {
	
	private int id; //Id identificador para cada jogador
	private int posicaoPino = 0; //Posi��o no pino no tabuleiro, de 1 a 31
	private float saldo; //Saldo do jogador
	private float divida; 
	private String ip;
	private ArrayList<CartaCompra> cartasCompras = new ArrayList<CartaCompra>(); //Cartas de compras, obtidas em casas do tipo "compras e entretenimento"
	
	public Jogador() { }
	
	
	
	public Jogador(int id, float saldo, String ip) {
		this.id = id;
		this.saldo = saldo;
		this.setIp(ip);
	}
	
	public int getPosicaoPino() {
		return posicaoPino;
	}
	public void setPosicaoPino(int posicaoPino) {
		this.posicaoPino = posicaoPino;
	}
	public float getSaldo() {
		return saldo;
	}
	public void setSaldo(float saldo) {
		this.saldo = saldo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void addCarta(CartaCompra carta) {
		saldo = saldo - carta.getValorCarta();
		cartasCompras.add(carta);
	}
	@Override
	public String toString() {
		return "ID: "+id+" Pos: "+posicaoPino+" Saldo: "+saldo;
	}
	public float getDivida() {
		return divida;
	}
	public void setDivida(float divida) {
		this.divida = divida;
	}
	public ArrayList<CartaCompra> getCartasCompras() {
		return cartasCompras;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
}
