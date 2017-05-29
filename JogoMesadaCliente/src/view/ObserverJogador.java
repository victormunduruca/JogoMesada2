package view;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;

import model.Jogador;
import model.Observer;

/**
 * Classe que implementa a classe Observer, atualizando os elementos da GUI quando requisitado
 * @author Victor
 *
 */
public class ObserverJogador implements Observer{
	private ArrayList<Pino> pinos; //Lista de pinos
	private JFrame frame; //Frame da janela principal
	private JLabel label; //Label com o saldo
	
	public ObserverJogador(JFrame frame, ArrayList<Pino> pinos) {
		this.frame = frame;
		this.pinos = pinos;
		label = new JLabel();

		label.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 22));
		label.setText("Saldo: 0");
		label.setBounds(77, 756, 360, 51);
		frame.getContentPane().add(label);
	}


	@Override
	public void update(int jogadaDado, Jogador jogador) {
		// TODO Auto-generated method stub
		Pino pino = getPino(jogador.getId());
		andaDado(jogadaDado, pino);
		label.setText("Saldo: " + jogador.getSaldo());
	}
	/**
	 * M�todo que move os pinos na GUI c
	 * @param N�mero retiradao no dado
	 * @param Pino que se deseja mover
	 */
	public void andaDado(int numeroDado, Pino pino) {
		for (int i = numeroDado; i > 0; i--) {
			System.out.println("\n i = " + i + "\n");
			System.out.println("X: " + pino.getX() + " Y: " + pino.getY());
			if (pino.getX() >= 384 && pino.getY() == 137 * 4) { // chega no
				// final
				pino.getLabel().setBounds(0, 0, 128, 137);
				pino.setY(0); //Atualiza valores dos pinos
				pino.setX(0);
				break;
			}
			if (pino.getX() >= 768) { // se for igual ao valor fora da borda ele
				// desce
				pino.getLabel().setBounds(0, pino.getY() + 137, 128, 137);
				pino.setY(pino.getY() + 137);
				pino.setX(0);
			} else {
				System.out.println("Entrou no else");
				pino.getLabel().setBounds(pino.getX() + 128, pino.getY(), 128,
						137);
				pino.setX(pino.getX() + 128);
				System.out.println("DEPOIS X: " + pino.getX() + " Y: "
						+ pino.getY());
			}
		}
	}
	/**
	 * 
	 * @param idJogador
	 * @return Pino correspondente ao id do jogador
	 */
	public Pino getPino(int idJogador){
		Iterator<Pino> it = (Iterator) pinos.iterator();
		while(it.hasNext()) {
			Pino pino = (Pino) it.next();
			if(pino.getIdJogador() == idJogador)
				return pino;
		}
		return null;
	}
	
} 
