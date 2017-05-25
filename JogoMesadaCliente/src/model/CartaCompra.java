package model;

import java.util.Random;

/**
 * Classe que representa cartas do tipo compras e entretenimento
 * @author victo
 *
 */
public class CartaCompra {
	private String nomeCarta;
	private int valorCarta;
	private int valorRevendaCarta;
	
	public CartaCompra() { //Toda vez q uma nova instância de uma Carta Compra e Entretenimento for criada isso vai ser feito de forma aleatória
		criaCartaAleatoria();
	}
	
	public String getNomeCarta() {
		return nomeCarta;
	}
	public void setNomeCarta(String nomeCarta) {
		this.nomeCarta = nomeCarta;
	}
	public int getValorCarta() {
		return valorCarta;
	}
	public void setValorCarta(int valorCarta) {
		this.valorCarta = valorCarta;
	}
	public int getValorRevendaCarta() {
		return valorRevendaCarta;
	}
	public void setValorRevendaCarta(int valorRevendaCarta) {
		this.valorRevendaCarta = valorRevendaCarta;
	}
	public void criaCartaAleatoria() {
		Random rand = new Random();
		int i = rand.nextInt(3);
		switch (i) {
		case 0:
			this.nomeCarta = "Item1";
			this.valorCarta = 600;
			this.valorRevendaCarta = 1000;
			break;
		case 1:
			this.nomeCarta = "Item2";
			this.valorCarta = 700;
			this.valorRevendaCarta = 800;
			break;
		default:
			break;
		}
	}
}
