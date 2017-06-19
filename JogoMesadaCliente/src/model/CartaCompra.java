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
	
	public CartaCompra() { //Toda vez q uma nova instancia de uma Carta Compra e Entretenimento for criada isso vai ser feito de forma aleatoria
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
	/**
	 * Metodo que configura uma nova carta compra e entretenimento aleatoria
	 */
	public void criaCartaAleatoria() {
		Random rand = new Random();
		int i = rand.nextInt(3);
		switch (i) {
		case 0:
			this.nomeCarta = "Ida ao medico";
			this.valorCarta = 600;
			this.valorRevendaCarta = 1000;
			break;
		case 1:
			this.nomeCarta = "Smartphone";
			this.valorCarta = 700;
			this.valorRevendaCarta = 800;
			break;
		case 2:
			this.nomeCarta = "Notebook";
			this.valorCarta = 1000;
			this.valorRevendaCarta = 1200;
			break;
		default:
			break;
		}
	}
	@Override
	public String toString() {
		return nomeCarta+"\n"+"Valor Carta: " +valorCarta+" Valor Revenda: "+valorRevendaCarta;
	}
}
