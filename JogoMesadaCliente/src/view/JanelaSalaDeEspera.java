package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import model.Jogador;
import network.Cliente;
import network.Cliente.OnRequest;
import network.Protocolo;

import com.sun.security.ntlm.Client;

public class JanelaSalaDeEspera extends JPanel {

  private JList list;
  private DefaultListModel model;
  private JPanel topPanel;
  
  private JTextField ipText, portText;
  private JButton conButton;
  
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
    
    add(topPanel, BorderLayout.NORTH);
    add(pane, BorderLayout.SOUTH);
    
    setListeners();
  }
  
  private void atualizarListaJogadores(Jogador euJogador, ArrayList<Jogador> adversarios) {
	  model.clear();
	  model.addElement("Jogador " + euJogador.getId() + " (Você) / Saldo: " + euJogador.getSaldo());
	  for (Jogador adv : adversarios) {
		  model.addElement("Jogador " + adv.getId() + " / Saldo: " + adv.getSaldo());
	  }
  }
  
  private void setListeners() {
    conButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	String ip = ipText.getText();
        	String porta = portText.getText();
        	System.out.println("CONECTAR: " + ip + ":" + porta);
        	if (!conectado) {
	        	Cliente cliente = new Cliente();
	        	cliente.requisitar(ip, Integer.valueOf(porta), new RequestCallback());
	        	conButton.setText("Desconectar");
        	}
        	conectado = true;
        }
     });
  }
  
  private class RequestCallback implements OnRequest {

	@Override
	public void onUpdate(String data) {
		System.out.println("Recebido: " + data);
		Jogador euJogador = Protocolo.SalaEspera.getEuJogador(data);
		ArrayList<Jogador> adversarios = Protocolo.SalaEspera.getAdversarios(data);
		atualizarListaJogadores(euJogador, adversarios);
	}

	@Override
	public void onClose() {
		System.out.println("Início de jogo");
		// TODO Iniciar JanelaPrincipal e fechar esta janela
	}
  }

  public static void main(String s[]) {
    JFrame frame = new JFrame("Jogo da Mesada - Sala de Espera");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(new JanelaSalaDeEspera());
    frame.setSize(300, 220);
    frame.setVisible(true);
  }
}