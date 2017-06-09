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
import javax.swing.WindowConstants;

import model.Jogador;
import network.Cliente;
import network.Cliente.OnRequest;
import network.Protocolo;

import com.sun.security.ntlm.Client;

import controller.Controller;

public class JanelaSalaDeEspera extends JPanel {

	private JFrame frame;
	private JList list;
	private DefaultListModel model;
	private JPanel topPanel;

	private JTextField ipText, portText;
	private JButton conButton;

	private JLabel info;

	private boolean conectado = false;

	public JanelaSalaDeEspera() {
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
	}

	private void atualizarListaJogadores(final Jogador euJogador, final ArrayList<Jogador> adversarios) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				model.clear();
				model.addElement("Jogador " + euJogador.getId() + " (Você) [Saldo: " + euJogador.getSaldo() + "]");
				for (Jogador adv : adversarios) {
					model.addElement("Jogador " + adv.getId() + " / [Saldo: " + adv.getSaldo() + "]");
				}
			}});
	}

	private void setInfo(final String msg) {
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
			cliente.requisitar(ip, Integer.valueOf(porta), new RequestCallback());
			conButton.setText("Desconectar");
		}
		conectado = true;  
	}

	private void setListeners() {
		conButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				conectar();
			}
		});
	}

	private class RequestCallback implements OnRequest {

		@Override
		public void onUpdate(String data) {
			System.out.println("RECEBIDO: " + data);
			Controller controlador = Controller.getInstance();

			Jogador euJogador = Protocolo.SalaEspera.getEuJogador(data);
			ArrayList<Jogador> adversarios = Protocolo.SalaEspera.getAdversarios(data);

			controlador.setEuJogador(euJogador);
			controlador.setAdversarios(adversarios);

			atualizarListaJogadores(euJogador, adversarios);
		}

		@Override
		public void onClose() {
			JanelaPrincipal.iniciar();
			fechar();
		}

		@Override
		public void onErro(String erro) {
			setInfo("Erro de conexão");
		}
	}

	public void iniciar() {
		frame = new JFrame("Jogo da Mesada - Sala de Espera");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new JanelaSalaDeEspera());
		frame.setSize(300, 220);
		frame.setVisible(true);	  
	}

	public void fechar() {
		frame.setVisible(false);
		frame.dispose();
	}

	public static void main(String s[]) {
		JanelaSalaDeEspera j = new JanelaSalaDeEspera();
		j.iniciar();
	}
}