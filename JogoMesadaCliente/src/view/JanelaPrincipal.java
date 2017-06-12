package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import excpeptions.IdNaoEncontradoException;
import model.Jogador;

public class JanelaPrincipal {

	private static JButton btnRodarDado;
	private static JFrame frame;
	private static ArrayList<Pino> pinos;
	private static JList listaJogadores;
	private DefaultListModel modelJogadores;
	private static JLabel label; //Label com o saldo
	
	private OnJogo jogoListener;

	/**
	 * Interface utilizada para comunicar a UI com a logica de jogo
	 */
	public interface OnJogo { // TODO Impelementar metodos para o jogo
		void onDadoLancado();

		//void onAcaoCasaEntretenimento();
	}
	
	public JanelaPrincipal(OnJogo listener) {
		initialize(); // Constroi a UI
		this.jogoListener = listener;
	};

	public void iniciar() {
		frame.setVisible(true);
	}

	public void initialize() {
		frame = new JFrame();
		
		//----------------------------------------------------------
		
		// Configura a lista de jogadores 
		modelJogadores = new DefaultListModel();		
		listaJogadores = new JList(modelJogadores);
		listaJogadores.setBounds(927, 39, 222, 138); 
		
		label = new JLabel();

		label.setFont(new Font("Lucida Sans Typewriter", Font.BOLD, 22));
		label.setText("Saldo: 0");
		label.setBounds(77, 756, 360, 51);
		frame.getContentPane().add(label, BorderLayout.CENTER);
		frame.getContentPane().add(listaJogadores);
		
		
		//----------------------------------------------------------
		
		pinos = new ArrayList<Pino>();
		Pino pinoAmarelo = new Pino("Amarelo.png");
		Pino pinoAzul = new Pino("Azul.png");
		Pino pinoRosa = new Pino("Rosa.png");
		Pino pinoRoxo = new Pino("Roxo.png");
		Pino pinoVerde = new Pino("Verde.png");
		Pino pinoVermelho = new Pino("Vermelho.png");

		//TESTE
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

		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setBounds(100, 100, 1190, 871);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		panel.setBounds(10, 39, 896, 685);

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

		btnRodarDado = new JButton("Jogar Dado");
		btnRodarDado.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				jogoListener.onDadoLancado(); // Avisa a logica de jogo que o dado foi lancado
			}
			
		});
		btnRodarDado.setBounds(927, 266, 113, 51);
		frame.getContentPane().add(btnRodarDado);

		JButton btnConsultarSaldos = new JButton("Mudar Saldo");
//		btnConsultarSaldos.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				Controller.getInstance().metodoTeste();
//			}
//		});
		btnConsultarSaldos.setBounds(927, 216, 113, 23);
		frame.getContentPane().add(btnConsultarSaldos);

		JButton btnConsultarCartas = new JButton("Consultar Cartas");
		btnConsultarCartas.setBounds(1050, 216, 113, 23);
		frame.getContentPane().add(btnConsultarCartas);

	}
	
	public void atualizaJogadores(final Jogador euJogador, final ArrayList<Jogador> adversarios) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				modelJogadores.clear();
				
				modelJogadores.addElement(euJogador.toString() + " (VocÃª)");
				anda(euJogador.getPosicaoPino(), euJogador.getId());
				System.out.println("EU JOGADOR: " + euJogador.toString());
				
				for (Jogador adv : adversarios) {
					modelJogadores.addElement(adv.toString());
					anda(adv.getPosicaoPino(), adv.getId());
				}
			}
		});
	}
	
	//----------------------------------------------------------
	
	/**
	 * 
	 * @param posicao
	 * @param idJogador
	 */
	public void anda(int posicao, int idJogador) {
		Pino pino = getPino(idJogador);
		if (pino == null) {
			System.err.println("ERRO: Pino nao pode ser nulo");
			return;
		}
		
		pino.setX(0);
		pino.setY(0);
		if(posicao == 0) {
			pino.getLabel().setBounds(0, 0, 128, 137);
			return;
		}
		for (int i = posicao; i > 0; i--) {
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
				pino.getLabel().setBounds(pino.getX() + 128, pino.getY(), 128,
						137);
				pino.setX(pino.getX() + 128);
			}
		}
	}
	
	/**
 	 * 
	 * @param idJogador
	 * @return Pino correspondente ao id do jogador
	 */
	private Pino getPino(int idJogador) {
		Iterator<Pino> it = (Iterator) pinos.iterator();
		while(it.hasNext()) {
			Pino pino = (Pino) it.next();
			if(pino.getIdJogador() == idJogador)
				return pino;
		}
		return null;
	}
	
	//----------------------------------------------------------
	
	
	public void habilitaJogar() {
		btnRodarDado.setEnabled(true);
	}
	
//	public static void desabilitarBtnRodarDado() {
//		btnRodarDado.setEnabled(false);
//	}
//	public static void habilitarBtnRodarDado() {
//		btnRodarDado.setEnabled(true);
//	}
	
	public void eSuaVez() {
		JOptionPane.showMessageDialog(null, "Sua vez!", "Sua vez!", JOptionPane.INFORMATION_MESSAGE);
	}

	public void casaPremio() {
		JOptionPane.showMessageDialog(null, "Voce ganhou 5.000$", "Premio!", JOptionPane.INFORMATION_MESSAGE);
	}

	public boolean casaCompraEntretenimento(String cartaCompra, boolean eEmprestimo) {
		int opcao = JOptionPane.showConfirmDialog(null, "Voce retirou a carta: " + cartaCompra + "\n Deseja compra-la?", "Carta Compra!", JOptionPane.OK_CANCEL_OPTION);
		if(opcao == JOptionPane.OK_OPTION) {
			if(eEmprestimo) {
				switch (JOptionPane.showConfirmDialog(null, "Voce nao tem saldo suficiente, deseja realizar um emprestimo?", "Ops!", JOptionPane.OK_CANCEL_OPTION)) {
				case JOptionPane.OK_OPTION:
					break;
				default:
					return false;
				}
			} 
			return true;
		}
		return false;
	}

	public void casaAchouComprador(String achouComprador) {
		if(achouComprador == null) {
			JOptionPane.showMessageDialog(null, "Voce nao tem nenhuma carta Compra e Entretenimento", "Poxa :(", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Voce achou o comprador pra a carta:\n" +achouComprador, "Parabéns!", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void casaVendese() {
		JOptionPane.showMessageDialog(null, "Opa, Negocio de ocasiao, foi seu por 100X O numero do dado. A seguir, pegue uma carta Compra e Entretenimento!", "Acao", JOptionPane.INFORMATION_MESSAGE);
	}

	public void aniversario() {
		JOptionPane.showMessageDialog(null, "Feliz aniversário! Voce recebera 100$ de cada jogador!", "Eba!", JOptionPane.INFORMATION_MESSAGE);
	}
}