package view;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import network.Servidor;
import network.Servidor.OnServidor;

import controller.Controller;
import excpeptions.IdNaoEncontradoException;

public class JanelaPrincipal implements OnServidor {

	private Controller controller;

	private JFrame frame;
	private ArrayList<Pino> pinos;
	//	private static JanelaPrincipal instanciaJanelaPrincipal;
	private ObserverJogador observerJogador;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) { 
		JanelaPrincipal.iniciar();
	}
	
	public JanelaPrincipal() throws IdNaoEncontradoException {
		controller = Controller.getInstance();
		initialize();
	};

	public static void iniciar() {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) { // handle exception
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaPrincipal window = new JanelaPrincipal();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/**
	 * Initialize the contents of the frame.
	 * @throws IdNaoEncontradoException 
	 */
	private void initialize() throws IdNaoEncontradoException {

		pinos = new ArrayList<Pino>();
		Pino pinoAmarelo = new Pino("Amarelo.png");
		Pino pinoAzul = new Pino("Azul.png");
		Pino pinoRosa = new Pino("Rosa.png");
		Pino pinoRoxo = new Pino("Roxo.png");
		Pino pinoVerde = new Pino("Verde.png");
		Pino pinoVermelho = new Pino("Vermelho.png");

		//TESTE
		pinoVermelho.setIdJogador(1);
		pinos.add(pinoVermelho);
		pinos.add(pinoVerde);
		pinos.add(pinoAmarelo);
		pinos.add(pinoRoxo);
		pinos.add(pinoRosa);
		pinos.add(pinoAzul);
		//TESTE

		JPanel panel = new JPanel();
		JLayeredPane jLay = new JLayeredPane();
		JLabel imagemTabuleiro = new JLabel(new ImageIcon("Tabuleiro.png"));
		frame = new JFrame();

		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setBounds(100, 100, 1190, 871);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		panel.setBounds(10, 39, 896, 685);

		ObserverJogador observerJogador = new ObserverJogador(frame, pinos);
		controller.register(observerJogador);

		jLay.setPreferredSize(new Dimension(896, 685));
		jLay.add(imagemTabuleiro, new Integer(10));

		jLay.add(pinoAmarelo.getLabel(), new Integer(20));
		jLay.add(pinoAzul.getLabel(), new Integer(30));
		jLay.add(pinoRosa.getLabel(), new Integer(40));
		jLay.add(pinoRoxo.getLabel(), new Integer(50));
		jLay.add(pinoVerde.getLabel(), new Integer(60));
		jLay.add(pinoVermelho.getLabel(), new Integer(70));

		imagemTabuleiro.setBounds(0, 0, 896, 685);
		frame.getContentPane().add(panel);
		panel.add(jLay);

		JButton btnRodarDado = new JButton("Jogar Dado");
		btnRodarDado.addActionListener(new ActionListener() {
			int valorDado = 0;

			public void actionPerformed(ActionEvent arg0) {;
			try {
				controller.metodoTeste();
			} catch (IdNaoEncontradoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//				JOptionPane.showMessageDialog(null, "Valor do dado foi de = "
			//						+ valorDado);
			//				System.out.println("Valor dado: " + valorDado);
			}
		});
		btnRodarDado.setBounds(927, 266, 113, 51);
		frame.getContentPane().add(btnRodarDado);

		JButton btnConsultarSaldos = new JButton("Mudar Saldo");
		btnConsultarSaldos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					controller.metodoTeste();
				} catch (IdNaoEncontradoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnConsultarSaldos.setBounds(927, 216, 113, 23);
		frame.getContentPane().add(btnConsultarSaldos);

		JButton btnConsultarCartas = new JButton("Consultar Cartas");
		btnConsultarCartas.setBounds(1050, 216, 113, 23);
		frame.getContentPane().add(btnConsultarCartas);


		JList listaJogadores = new JList(Controller.getInstance().getAdversarios().toArray());
		listaJogadores.setBounds(927, 39, 222, 101);

	}
	
	@Override
	public void onDadoRecebido(String data) {
		
	}
	@Override
	public void onErro() { }
}
