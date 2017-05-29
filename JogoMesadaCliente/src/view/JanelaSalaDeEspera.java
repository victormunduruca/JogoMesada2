package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class JanelaSalaDeEspera extends JPanel {

  private JList list;
  private DefaultListModel model;

  int counter = 15;

  public JanelaSalaDeEspera() {
    setLayout(new BorderLayout());
    model = new DefaultListModel();
    list = new JList(model);
    JScrollPane pane = new JScrollPane(list);

    for (int i = 0; i < 6; i++) {
      model.addElement("Jogador " + (i + 1));
    }

    add(pane, BorderLayout.CENTER);
  }

  public static void main(String s[]) {
    JFrame frame = new JFrame("Sala de Espera");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(new JanelaSalaDeEspera());
    frame.setSize(350, 250);
    frame.setVisible(true);
  }
}