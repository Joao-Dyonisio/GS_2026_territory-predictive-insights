import java.util.ArrayList;

public abstract class EstrategiaPrevisao {

    public static final double SEM_PREVISAO = -1;

    private String nome;
    private int minimoLeituras;

    public EstrategiaPrevisao(String nome, int minimoLeituras) {
        this.nome = nome;
        this.minimoLeituras = minimoLeituras;
    }
    public double prever(ArrayList<Double> leituras) {
        if (leituras.size() < this.minimoLeituras) {
            return SEM_PREVISAO;
        }
        return leituras.get(leituras.size() - 1);
    }

    public abstract String getDescricao();

    public String getNome() {
        return this.nome;
    }

    public int getMinimoLeituras() {
        return this.minimoLeituras;
    }
}
