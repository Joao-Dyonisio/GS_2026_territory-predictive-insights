import java.util.ArrayList;

public class PrevisaoMediaMovel extends EstrategiaPrevisao {

    private int tamanhoJanela;

    public PrevisaoMediaMovel(int tamanhoJanela) {
        super("Média Móvel", tamanhoJanela);
        this.tamanhoJanela = tamanhoJanela;
    }

    public int getTamanhoJanela() {
        return this.tamanhoJanela;
    }

    @Override
    public double prever(ArrayList<Double> leituras) {
        if (super.prever(leituras) == SEM_PREVISAO) {
            return SEM_PREVISAO;
        }

        double soma = 0;
        for (int i = leituras.size() - this.tamanhoJanela; i < leituras.size(); i++) {
            soma += leituras.get(i);
        }
        return soma / this.tamanhoJanela;
    }

    @Override
    public String getDescricao() {
        return "média das últimas " + this.tamanhoJanela + " leituras";
    }
}