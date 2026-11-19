import java.util.ArrayList;
public class PrevisaoComparacao extends EstrategiaPrevisao {

    private int tamanhoPeriodo;

    public PrevisaoComparacao(int tamanhoPeriodo) {
        super("Comparação de Períodos", tamanhoPeriodo * 2);
        this.tamanhoPeriodo = tamanhoPeriodo;
    }

    public int getTamanhoPeriodo() {
        return this.tamanhoPeriodo;
    }

    @Override
    public double prever(ArrayList<Double> leituras) {
        double ultimaLeitura = super.prever(leituras);
        if (ultimaLeitura == SEM_PREVISAO) {
            return SEM_PREVISAO;
        }

        int fim = leituras.size();
        double somaAtual = 0;
        double somaAnterior = 0;

        for (int i = 0; i < this.tamanhoPeriodo; i++) {
            somaAtual += leituras.get(fim - 1 - i);
            somaAnterior += leituras.get(fim - 1 - this.tamanhoPeriodo - i);
        }

        double mediaAtual = somaAtual / this.tamanhoPeriodo;
        double mediaAnterior = somaAnterior / this.tamanhoPeriodo;

        double mudancaPorLeitura = (mediaAtual - mediaAnterior) / this.tamanhoPeriodo;
        double previsao = ultimaLeitura + mudancaPorLeitura;
        if (previsao < 0) {
            previsao = 0;
        }
        return previsao;
    }

    @Override
    public String getDescricao() {
        return "última leitura + variação por leitura entre os últimos " + this.tamanhoPeriodo
                + " valores e os " + this.tamanhoPeriodo + " anteriores";
    }
}