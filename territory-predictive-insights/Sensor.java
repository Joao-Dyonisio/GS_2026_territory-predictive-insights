import java.util.ArrayList;

public interface Sensor {

    int MAXIMO_LEITURAS = 20;

    double lerValor();

    boolean verificarFuncionamento();

    String getTipo();

    String getUnidade();

    double getLimiteAlerta();

    boolean passouDoLimite(double valor);

    double getValorMaximo();

    ArrayList<Double> getLeituras();
}