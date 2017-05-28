package view;

import java.util.ArrayList;

public class ControllerView {
	private ArrayList<Pino> pinos;
	private ControllerView() {
		pinos = new ArrayList<Pino>();
		Pino pinoAmarelo = new Pino("pinoAmarelo.png");
		Pino pinoAzul = new Pino("pinoAzul.png");
		Pino pinoRosa = new Pino("pinoRosa.png");
		Pino pinoRoxo = new Pino("pinoRoxo.png");
		Pino pinoVerde = new Pino("pinoVerde.png");
		Pino pinoVermelho = new Pino("pinoVermelho.png");
		
		//TESTE
		pinoVermelho.setIdJogador(1);
		pinos.add(pinoVermelho);
		pinos.add(pinoVerde);
		pinos.add(pinoAmarelo);
		pinos.add(pinoRoxo);
		pinos.add(pinoRosa);
		pinos.add(pinoAzul);
		//TESTE
	}
	
}
