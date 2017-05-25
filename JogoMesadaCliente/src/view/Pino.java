package view;


import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Pino {

	private int idJogador;
	private JLabel label;
	private int x;
	private int y;
	
	public Pino(String nomeImagem) {
		label = new JLabel(new ImageIcon(nomeImagem));
		this.x = 0;
		this.y = 0;
	}
	
	public JLabel getLabel() {
		return label;
	}
	public void setLabel(JLabel label) {
		this.label = label;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getIdJogador() {
		return idJogador;
	}
	public void setIdJogador(int idJogador) {
		this.idJogador = idJogador;
	}
	
}
