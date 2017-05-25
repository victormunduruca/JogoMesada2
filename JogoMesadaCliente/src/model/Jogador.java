package model;

import java.util.ArrayList;

public class Jogador {
	
	private int id; //Id identificador para cada jogador
	private int posicaoPino; //Posição no pino no tabuleiro, de 1 a 31
	private int saldo; //Saldo do jogador
	private ArrayList<CartaCompra> cartasCompras; //Cartas de compras, obtidas em casas do tipo "compras e entretenimento"
	
	public Jogador() {
		this.saldo = 3000; //Inicia o saldo com o valor especificado
		int posicaoPino = 0; 
		cartasCompras = new ArrayList<CartaCompra>();
	}
	
	public int getPosicaoPino() {
		return posicaoPino;
	}
	public void setPosicaoPino(int posicaoPino) {
		this.posicaoPino = posicaoPino;
	}
	public int getSaldo() {
		return saldo;
	}
	public void setSaldo(int saldo) {
		this.saldo = saldo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	
}
