package uefs.br.ecomp.mi;

import uefs.br.ecomp.mi.model.Sala;

public interface OnSalaEspera {
	void onNovoJogadorEntrou(Sala sala, String nome);
	void onJogadorSaiu(Sala sala, String nome);
	void onJogoComecou(Sala sala);
}
