package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.Jogador;
import network.Cliente;
import network.Cliente.OnRequest;

public class JanelaSalaDeEspera extends JPanel {

	private JFrame frame;
	private JList list;
	private DefaultListModel model;
	private JPanel topPanel;

	private JTextField ipText, portText;
	private JButton conButton;

	private JLabel info;

	private boolean conectado = false;
	
	private OnRequest requestCallback;
	
	public JanelaSalaDeEspera(OnRequest requestCallback) {
		this.requestCallback = requestCallback;
	
		setLayout(new BorderLayout());
		model = new DefaultListModel();
		list = new JList(model);
		JScrollPane pane = new JScrollPane(list);

		topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());

		ipText = new JTextField(6);
		ipText.setText("127.0.0.1");
		topPanel.add(ipText);

		portText = new JTextField(6);
		portText.setText("7070");
		topPanel.add(portText);

		conButton = new JButton("Conectar");
		topPanel.add(conButton);

		info = new JLabel();

		add(topPanel, BorderLayout.NORTH);
		add(pane, BorderLayout.CENTER);
		add(info, BorderLayout.SOUTH);

		setListeners();
		//conectar();
	}

	public void atualizarListaJogadores(final Jogador euJogador, final ArrayList<Jogador> adversarios) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				model.clear();
				model.addElement("Jogador " + euJogador.getId() + " (Você) [Saldo: " + euJogador.getSaldo() + "]");
				for (Jogador adv : adversarios) {
					model.addElement("Jogador " + adv.getId() + " / [Saldo: " + adv.getSaldo() + "]");
				}
			}});
	}

	public void setInfo(final String msg) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				info.setText(msg);
			}
		});
	}
	public void conectar() {
		String ip = ipText.getText();
		String porta = portText.getText();
		System.out.println("CONECTANDO PARA " + ip + ":" + porta);
		if (!conectado) {
			Cliente cliente = new Cliente();
			cliente.requisitar(ip, Integer.valueOf(porta), requestCallback);
			conButton.setText("Desconectar");
		}
		setConetado(true);  
	}
	
	public void setConetado(boolean conectado) {
		this.conectado = conectado;
		if (!this.conectado) {
			conButton.setText("Conectar");
		}
	}

	private void setListeners() {
		conButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conectar();
			}
		});
	}

	public void iniciar() {
		frame = new JFrame("Jogo da Mesada - Sala de Espera");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//	frame.setContentPane(new JanelaSalaDeEspera());
		frame.setContentPane(this);
		frame.setSize(300, 220);
		frame.setVisible(true);	 
	}

	public void fechar() {
		if(frame == null) {
			System.out.println("o frame � nulo");
		}
		frame.setVisible(false);
		frame.dispose();
	}

//	public static void main(String s[]) {
//		JanelaSalaDeEspera j = new JanelaSalaDeEspera();
//		j.iniciar();
//	}
}