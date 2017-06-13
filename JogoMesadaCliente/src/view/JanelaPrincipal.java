package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import controller.Controller;
import model.Jogador;

public class JanelaPrincipal {

	private static JButton btnRodarDado;
	private static JLabel sorteGrandeLabel;
	private static JFrame frame;
	private static ArrayList<Pino> pinos;
	private static JList listaJogadores;
	private DefaultListModel modelJogadores;
	
	private OnJogo jogoListener;

	/**
	 * Interface utilizada para comunicar a UI com a logica de jogo
	 */
	public interface OnJogo { // TODO Impelementar metodos para o jogo
		void onDadoLancado();
		void onAcaoCorreio(String carta);
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
		frame.getContentPane().add(listaJogadores);
		
		
		//----------------------------------------------------------
		
		sorteGrandeLabel = new JLabel("Sorte Grande: 0");
		sorteGrandeLabel.setBounds(927, 355, 222, 14);
		frame.getContentPane().add(sorteGrandeLabel);
		
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
		frame.setBounds(100, 100, 1178, 811);
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
		btnRodarDado.setEnabled(false);
		btnRodarDado.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				jogoListener.onDadoLancado(); // Avisa a logica de jogo que o dado foi lancado
			}
			
		});
		btnRodarDado.setBounds(927, 266, 113, 51);
		frame.getContentPane().add(btnRodarDado);
		
		

	}
	
	public void atualizaJogadores(final Jogador euJogador, final ArrayList<Jogador> adversarios) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				modelJogadores.clear();
				
				modelJogadores.addElement(euJogador.toString() + " (Você)");
				anda(euJogador.getPosicaoPino(), euJogador.getId());
				
				for (Jogador adv : adversarios) {
					modelJogadores.addElement(adv.toString());
					anda(adv.getPosicaoPino(), adv.getId());
				}
			}
		});
	}
	
	public void atualizaJogadores(final Jogador euJogador, final ArrayList<Jogador> adversarios, final int idJogadorAtual) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				modelJogadores.clear();
				
				if (euJogador.getId() == idJogadorAtual) {
					modelJogadores.addElement(">" + euJogador.toString() + " (Você)");
				} else {
					modelJogadores.addElement(euJogador.toString() + " (Você)");
				}
				anda(euJogador.getPosicaoPino(), euJogador.getId());
				
				for (Jogador adv : adversarios) {
					if (adv.getId() == idJogadorAtual) {
						modelJogadores.addElement(">" + adv.toString());
					} else {
						modelJogadores.addElement(adv.toString());
					}
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
	
	
	public void habilitaJogar(final boolean habilitar) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				btnRodarDado.setEnabled(habilitar);
			}
		});
	}
	
	public void setQuantiaSorteGrande(final float quantia) {
	//	System.out.println("SORTE GRANDE ATUALIZADA: R$" + quantia); // FIXME Remover!
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				sorteGrandeLabel.setText("Sorte Grande: " + quantia);
			}
		});
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
			JOptionPane.showMessageDialog(null, "Voce achou o comprador pra a carta:\n" +achouComprador, "Parab�ns!", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void casaVendese() {
		JOptionPane.showMessageDialog(null, "Opa, Negocio de ocasiao, foi seu por 100X O numero do dado. A seguir, pegue uma carta Compra e Entretenimento!", "Acao", JOptionPane.INFORMATION_MESSAGE);
	}

	public void aniversario() {
		JOptionPane.showMessageDialog(null, "Feliz aniversario! Voce recebera 100$ de cada jogador!", "Eba!", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showDialogSorteGrande(String nomeCasa, float quantia) {
		JOptionPane.showMessageDialog(null, 
				"Voce caiu na casa " + "\"" + nomeCasa + "\"!. Um valor de R$" + quantia + " sera descontado.", 
				"Acao", 
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showDialogEmprestimo(float quantia) {
		JOptionPane.showMessageDialog(null, 
				"Seu saldo é insuficiente. É necessário realizar um emprestimo de R$" + quantia, 
				"Emprestimo", 
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showDialogInicioMaratonaBeneficente() {
		JOptionPane.showMessageDialog(null, 
				"Início da Maratona Beneficiente!\n" +
				"Todos os seus adversários devem contribuir para o montante do Sorte Grande.\n" +
				"O próximo jogador que tirar um número 6 no lançamento do dado, na sua vez de jogar, leva a bolada.\n" +
				"Boa Sorte!\n", 
				"Maratona Beneficente", 
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showDialogVencedorMaratonaBeneficente() {
		JOptionPane.showMessageDialog(null, 
				"Parabéns você tirou um 6 e venceu a Maratona Beficente!", 
				"Vencedor!", 
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showDialogJogadorVencedorMaratonaBeneficente(int id) {
		JOptionPane.showMessageDialog(null, 
				"O jogador " + id + " venceu a Maratona Beficente!", 
				"Maratona Beneficente", 
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showJogarMaratonaBeneficente() {
		JOptionPane.showMessageDialog(null, 
				"Início da Maratona Beneficiente!\n" +
				"Todos os jogadores devem contribuir para o montante do Sorte Grande.\n" +
				"Clique em Ok para lançar o dado e contribuir para a Sorte Grande\n " +
				"Boa Sorte!", 
				"Jogar Maratona Beneficente", 
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showJogarMaratonaBeneficente(int dadoValor, float quantia) {
		JOptionPane.showMessageDialog(null, 
				"Você tirou " + dadoValor + " no dado. Você contribuirá com R$" + quantia, 
				"Resultado do Lançamento nos Dados", 
				JOptionPane.INFORMATION_MESSAGE);
	}


	public void casaCorreio(ArrayList<String> cartas) {
		while(!cartas.isEmpty()) {
			JComboBox comboCartas = new JComboBox(cartas.toArray());
			int opcao = JOptionPane.showConfirmDialog(null, comboCartas, "Selecione a carta", JOptionPane.OK_CANCEL_OPTION);
			if(opcao == JOptionPane.OK_OPTION) {
				String cartaEscolhida = (String) comboCartas.getSelectedItem();
				jogoListener.onAcaoCorreio(cartaEscolhida);
				cartas.remove(cartaEscolhida);
			}
		}
	}

	public Integer pagueUmVizinhoAgora() {
		JComboBox comboIds = new JComboBox(Controller.getInstance().getListaIdsAdversarios().toArray());
		int opcao = JOptionPane.showConfirmDialog(null, comboIds, "Selecione o adversario", JOptionPane.OK_CANCEL_OPTION);
		if(opcao == JOptionPane.OK_OPTION) {
			Integer idEscolhido = (Integer) comboIds.getSelectedItem();
			return idEscolhido;
		}
		return null;
	}

	public void conta(String conta) {
		JOptionPane.showMessageDialog(null, "Voc� teve que pagar a conta: " +conta, "Oops", JOptionPane.INFORMATION_MESSAGE);
	}
}