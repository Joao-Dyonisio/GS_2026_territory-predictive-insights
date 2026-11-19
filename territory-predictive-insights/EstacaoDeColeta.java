import java.util.ArrayList;

public class EstacaoDeColeta extends ComponenteTerritorial {

    private ArrayList<Sensor> sensores;

    public EstacaoDeColeta(String id, String nome, String regiao) {
        super(id, nome, regiao);
        this.sensores = new ArrayList<>();
    }

    public void adicionarSensor(Sensor sensor) {
        this.sensores.add(sensor);
    }

    public ArrayList<Sensor> getSensores() {
        return this.sensores;
    }

    public void coletarLeituras() {
        if (!this.isAtivo()) {
            System.out.println("A estação " + this.getNome() + " está inativa. Nenhuma leitura foi coletada.");
            return;
        }
        for (Sensor sensor : this.sensores) {
            sensor.lerValor();
        }
    }

    @Override
    public String verificarStatus() {
        int funcionando = 0;
        for (Sensor sensor : this.sensores) {
            if (sensor.verificarFuncionamento()) {
                funcionando++;
            }
        }
        return "Estação " + this.getNome() + " [" + this.getStatus() + "] - sensores funcionando: "
                + funcionando + " de " + this.sensores.size();
    }
}