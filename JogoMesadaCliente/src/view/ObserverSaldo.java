package view;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

import model.Observer;

public class ObserverSaldo implements Observer{
	private JFrame frame;
	private int saldo;
	private JLabel label;
	
	public ObserverSaldo(JFrame frame) {
		this.frame = frame;
		label = new JLabel();

		label.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 22));
		label.setText("Saldo: 0");
		label.setBounds(77, 756, 360, 51);
		frame.getContentPane().add(label);
	}
	@Override
	public void update(int saldo) {
		// TODO Auto-generated method stub
		label.setText("Saldo: " + saldo);
	}

}
