package view;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

import model.Jogador;
import model.Observer;

public class ObserverJogador implements Observer{
	private JanelaPrincipal janelaPrincipal;
	private JFrame frame;
	private JLabel label;
	
	public ObserverJogador(JFrame frame, JanelaPrincipal j) {
	//	janelaPrincipal = JanelaPrincipal.getInstance();
		this.frame = frame;
		label = new JLabel();

		label.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 22));
		label.setText("Saldo: 0");
		label.setBounds(77, 756, 360, 51);
		frame.getContentPane().add(label);
	}


	@Override
	public void update(int jogadaDado, Jogador jogador) {
		// TODO Auto-generated method stub
		Pino pino = janelaPrincipal.getPino(jogador.getId());
		janelaPrincipal.andaDado(jogadaDado, pino);
		label.setText("Saldo: " + jogador.getSaldo());
	}
	
} 
