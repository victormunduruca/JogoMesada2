package network;

import java.util.ArrayList;

import model.Jogador;

public class Protocolo {
	public static class SalaEspera {
		public static String getAcao(String str) {
			return str.split("#")[0];
		}
		
		public static Jogador getEuJogador(String str) {
			str = str.split("#")[1];
			System.out.println("Antes: " + str);
			System.out.println("Size: " + str.split(" ").length);

			if (str.split(" ").length > 1) {
				return formatarJogador(str.split(" ")[0]);	

			} else {
				return formatarJogador(str);	
			}
		}

		public static ArrayList<Jogador> getAdversarios(String str) {
			ArrayList<Jogador> jogadores = new ArrayList<Jogador>();
			str = str.split("#")[1];
			String[] js = str.split(" ");
			for (int i = 1 ; i < js.length; i++) {
				jogadores.add(formatarJogador(js[i]));
			}
			return jogadores;
		}
		
		private static Jogador formatarJogador(String str) {
			String[] attrs = str.split(",");
			return new Jogador(Integer.valueOf(attrs[0]), Float.valueOf(attrs[1]), attrs[2]);
		}
	}
}
