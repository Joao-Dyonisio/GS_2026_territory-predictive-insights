import java.util.ArrayList;

public class SensorChuva implements Sensor {

    private static final double VALOR_NORMAL = 5.0;       // chuva fraca
    private static final double VALOR_TEMPESTADE = 70.0;  // temporal forte
    private static final double VELOCIDADE = 0.4;         // quão rápido chega no valor da tempestade
    private static final double RUIDO = 6.0;
    private static final double VALOR_MAXIMO = 120.0;

    private ArrayList<Double> leituras;
    private DadosTerritorio dadosDoTerritorio;
    private ClimaSimulado clima;

    public SensorChuva(DadosTerritorio dadosDoTerritorio, ClimaSimulado clima) {
        this.leituras = new ArrayList<>();
        this.dadosDoTerritorio = dadosDoTerritorio;
        this.clima = clima;
    }

    @Override
    public double lerValor() {
        double valorAlvo = VALOR_NORMAL;
        if (this.clima.isTempestade()) {
            valorAlvo = VALOR_TEMPESTADE;
        }
        double ultimoValor = VALOR_NORMAL;
        if (this.leituras.size() > 0) {
            ultimoValor = this.leituras.get(this.leituras.size() - 1);
        }

        double novoValor = ultimoValor + (valorAlvo - ultimoValor) * VELOCIDADE + (Math.random() - 0.5) * RUIDO;
        if (novoValor < 0) {
            novoValor = 0;
        }
        if (novoValor > VALOR_MAXIMO) {
            novoValor = VALOR_MAXIMO;
        }
        novoValor = Math.round(novoValor * 10) / 10.0;

        this.leituras.add(novoValor);
        if (this.leituras.size() > MAXIMO_LEITURAS) {
            this.leituras.remove(0);
        }
        return novoValor;
    }

    @Override
    public boolean verificarFuncionamento() {
        if (this.leituras.size() == 0) {
            return false;
        }
        double ultimaLeitura = this.leituras.get(this.leituras.size() - 1);
        return ultimaLeitura >= 0 && ultimaLeitura <= VALOR_MAXIMO;
    }

    @Override
    public String getTipo() {
        return "Chuva";
    }

    @Override
    public String getUnidade() {
        return "mm/h";
    }

    @Override
    public double getLimiteAlerta() {
        return this.dadosDoTerritorio.getLimiarChuva();
    }

    @Override
    public boolean passouDoLimite(double valor) {
        return valor >= this.getLimiteAlerta();
    }

    @Override
    public double getValorMaximo() {
        return VALOR_MAXIMO;
    }

    @Override
    public ArrayList<Double> getLeituras() {
        return this.leituras;
    }
}