import java.util.ArrayList;
import java.util.Scanner;

public class SistemaMonitoramento {

    private DadosTerritorio dados;
    private ClimaSimulado clima;
    private EstacaoDeColeta estacao;
    private ArrayList<EstrategiaPrevisao> estrategias;
    private Scanner teclado;

    public SistemaMonitoramento() {
        // Região fictícia. O código de acesso de demonstração é "chuva2026"
        this.dados = new DadosTerritorio("Ipiranga", 18500, "chuva2026");
        this.clima = new ClimaSimulado();
        this.estacao = this.criarEstacao(this.clima);
        this.estacao.ativar();

        this.estrategias = new ArrayList<>();
        this.estrategias.add(new PrevisaoMediaMovel(5));
        this.estrategias.add(new PrevisaoComparacao(3));

        this.teclado = new Scanner(System.in);

        for (int i = 0; i < 4; i++) {
            this.estacao.coletarLeituras();
        }
    }

    public static void main(String[] args) {
        SistemaMonitoramento sistema = new SistemaMonitoramento();
        sistema.iniciar();
    }

    private EstacaoDeColeta criarEstacao(ClimaSimulado climaDaEstacao) {
        EstacaoDeColeta novaEstacao = new EstacaoDeColeta("EST-01", "Córrego Seco", this.dados.getRegiao());
        novaEstacao.adicionarSensor(new SensorChuva(this.dados, climaDaEstacao));
        novaEstacao.adicionarSensor(new SensorNivelRio(this.dados, climaDaEstacao));
        novaEstacao.adicionarSensor(new SensorUmidade(this.dados, climaDaEstacao));
        return novaEstacao;
    }

    public void iniciar() {
        System.out.println("BEM VINDO AO SISTEMA TERRITORY PREDICTIVE INSIGHTS");
        System.out.println("Região: " + this.dados.getRegiao() + " (fictícia)");

        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--------- MENU ---------");
            System.out.println("0 - Sair");
            opcao = this.lerInteiro("Escolha uma opção: ");

            if (opcao != 0) {
                System.out.println("Opção inválida");
            }
        }
        System.out.println("Sistema encerrado");
    }

    private int lerInteiro(String pergunta) {
        System.out.print(pergunta);
        while (!this.teclado.hasNextInt()) {
            if (!this.teclado.hasNext()) {
                return 0;
            }
            this.teclado.next();
            System.out.println("Valor inválido: digite um número inteiro.");
            System.out.print(pergunta);
        }
        int valor = this.teclado.nextInt();
        if (this.teclado.hasNextLine()) {
            this.teclado.nextLine();
        }
        return valor;
    }
}
