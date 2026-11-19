import java.util.ArrayList;
import java.util.Scanner;

public class SistemaMonitoramento {

    private static final String[] NOMES_NIVEL = {"NORMAL", "ATENÇÃO", "ALERTA", "CRÍTICO"};
    private static final String[] SIGNIFICADOS = {"dentro do esperado", "se aproximando do limite", "limite atingido ou ultrapassado", "muito acima do limite"};
    private static final int NORMAL = 0;
    private static final int ATENCAO = 1;
    private static final int ALERTA = 2;
    private static final int CRITICO = 3;

    private static final double PROPORCAO_ATENCAO = 0.8;
    private static final double PROPORCAO_ALERTA = 1.0;
    private static final double PROPORCAO_CRITICO = 1.2;

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
            System.out.println("1 - Verificar sensores");
            System.out.println("2 - Rodar previsões");
            System.out.println("3 - Gerenciar dados do território");
            System.out.println("4 - Simular próximas leituras");
            System.out.println("7 - Ativar/desativar estação");
            System.out.println("0 - Sair");
            opcao = this.lerInteiro("Escolha uma opção: ");

            if (opcao == 1) {
                this.verificarSensores();
            } else if (opcao == 2) {
                this.rodarPrevisoes();
            } else if (opcao == 3) {
                this.gerenciarDados();
            } else if (opcao == 4) {
                this.simularLeituras();
            } else if (opcao == 7) {
                this.alternarEstacao();
            } else if (opcao != 0) {
                System.out.println("Opção inválida");
            }
        }
        System.out.println("Sistema encerrado");
    }

    private void verificarSensores() {
        System.out.println("\nVERIFICAÇÃO DOS SENSORES: ");
        System.out.println(this.estacao.verificarStatus());

        for (Sensor sensor : this.estacao.getSensores()) {
            ArrayList<Double> leituras = sensor.getLeituras();
            String funcionamento = "FALHA";
            if (sensor.verificarFuncionamento()) {
                funcionamento = "OK";
            }
            System.out.println("\nSensor de " + sensor.getTipo() + " - funcionamento: " + funcionamento);

            if (leituras.size() > 0) {
                double ultimaLeitura = leituras.get(leituras.size() - 1);
                System.out.println(String.format("Última leitura: %.1f %s | limite de alerta: %.1f %s",
                        ultimaLeitura, sensor.getUnidade(), sensor.getLimiteAlerta(), sensor.getUnidade()));
                if (sensor.passouDoLimite(ultimaLeitura)) {
                    System.out.println("A leitura atual já atingiu ou passou do limite!");
                }
            }
        }
    }

    private void rodarPrevisoes() {
        System.out.println("\nPREVISÕES: ");
        if (!this.estacao.isAtivo()) {
            System.out.println("Atenção: a estação está inativa, as previsões usam leituras antigas.");
        }
        boolean teveAlerta = false;

        for (Sensor sensor : this.estacao.getSensores()) {
            System.out.println(String.format("\nSensor de %s (limite de alerta: %.1f %s)",
                    sensor.getTipo(), sensor.getLimiteAlerta(), sensor.getUnidade()));

            for (EstrategiaPrevisao estrategia : this.estrategias) {
                double previsto = this.calcularPrevisao(sensor, estrategia);

                if (previsto == EstrategiaPrevisao.SEM_PREVISAO) {
                    System.out.println("  " + estrategia.getNome() + ": sem previsão. Precisa de "
                            + estrategia.getMinimoLeituras() + " leituras e há apenas "
                            + sensor.getLeituras().size() + ". Use a opção 4.");
                } else {
                    int nivel = this.calcularNivel(previsto, sensor.getLimiteAlerta());
                    double percentual = Math.floor(previsto / sensor.getLimiteAlerta() * 100);
                    System.out.println(String.format("  %s: previsto %.1f %s (%.0f%% do limite) -> %s: %s",
                            estrategia.getNome(), previsto, sensor.getUnidade(), percentual,
                            NOMES_NIVEL[nivel], SIGNIFICADOS[nivel]));
                    if (nivel > NORMAL) {
                        this.exibirAlerta(sensor, estrategia, previsto, nivel);
                        teveAlerta = true;
                    }
                }
            }
        }

        if (!teveAlerta) {
            System.out.println("\nNenhum alerta no momento.");
        }
    }

    private double calcularPrevisao(Sensor sensor, EstrategiaPrevisao estrategia) {
        double previsto = estrategia.prever(sensor.getLeituras());
        if (previsto > sensor.getValorMaximo()) {
            previsto = sensor.getValorMaximo();
        }
        return previsto;
    }

    private int calcularNivel(double previsto, double limite) {
        double proporcao = previsto / limite;
        if (proporcao >= PROPORCAO_CRITICO) {
            return CRITICO;
        }
        if (proporcao >= PROPORCAO_ALERTA) {
            return ALERTA;
        }
        if (proporcao >= PROPORCAO_ATENCAO) {
            return ATENCAO;
        }
        return NORMAL;
    }

    private void exibirAlerta(Sensor sensor, EstrategiaPrevisao estrategia, double previsto, int nivel) {
        String borda = "----------------------------------------";
        if (nivel == ALERTA) {
            borda = "========================================";
        } else if (nivel == CRITICO) {
            borda = "########################################";
        }

        System.out.println(borda);
        System.out.println("[" + NOMES_NIVEL[nivel] + "] Sensor de " + sensor.getTipo());
        if (previsto >= sensor.getLimiteAlerta()) {
            System.out.println(String.format("Por quê: previsão de %.1f %s (%s) passa do limite de %.1f %s.",
                    previsto, sensor.getUnidade(), estrategia.getNome(), sensor.getLimiteAlerta(), sensor.getUnidade()));
        } else {
            System.out.println(String.format("Por quê: previsão de %.1f %s (%s) está perto do limite de %.1f %s.",
                    previsto, sensor.getUnidade(), estrategia.getNome(), sensor.getLimiteAlerta(), sensor.getUnidade()));
        }
        System.out.println("O alerta apoia a decisão.");
        System.out.println(borda);
    }

    private void gerenciarDados() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\nDADOS DO TERRITÓRIO: ");
            System.out.println("1 - Ver dados");
            System.out.println("2 - Alterar população");
            System.out.println("3 - Alterar região");
            System.out.println("4 - Alterar limiar de chuva (exige código)");
            System.out.println("5 - Alterar limiar do nível do rio (exige código)");
            System.out.println("6 - Alterar limiar de umidade do solo (exige código)");
            System.out.println("0 - Voltar");
            opcao = this.lerInteiro("Opção: ");

            if (opcao == 1) {
                this.dados.exibirDados();
            } else if (opcao == 2) {
                int novaPopulacao = this.lerInteiro("Nova população: ");
                this.dados.setPopulacao(novaPopulacao);
            } else if (opcao == 3) {
                String novaRegiao = this.lerTexto("Nova região: ");
                this.dados.setRegiao(novaRegiao);
            } else if (opcao == 4) {
                double novoLimiar = this.lerDecimal("Novo limiar de chuva (mm/h): ");
                String codigo = this.lerTexto("Código de acesso: ");
                this.dados.setLimiarChuva(novoLimiar, codigo);
            } else if (opcao == 5) {
                double novoLimiar = this.lerDecimal("Novo limiar do nível do rio (m): ");
                String codigo = this.lerTexto("Código de acesso: ");
                this.dados.setLimiarNivelRio(novoLimiar, codigo);
            } else if (opcao == 6) {
                double novoLimiar = this.lerDecimal("Novo limiar de umidade do solo (%): ");
                String codigo = this.lerTexto("Código de acesso: ");
                this.dados.setLimiarUmidade(novoLimiar, codigo);
            } else if (opcao != 0) {
                System.out.println("Opção inválida");
            }
        }
    }

    private void simularLeituras() {
        if (!this.estacao.isAtivo()) {
            System.out.println("A estação está inativa. Ative-a na opção 7");
            return;
        }

        int quantidade = this.lerInteiro("Quantas leituras simular (1 a " + Sensor.MAXIMO_LEITURAS + ")? ");
        if (quantidade < 1 || quantidade > Sensor.MAXIMO_LEITURAS) {
            System.out.println("Quantidade inválida.");
            return;
        }

        int tempo = this.lerInteiro("Tempo: 1 - normal ou 2 - tempestade chegando: ");
        if (tempo == 1) {
            this.clima.setTempestade(false);
        } else if (tempo == 2) {
            this.clima.setTempestade(true);
        } else {
            System.out.println("Opção inválida. Nada foi simulado");
            return;
        }

        for (int i = 1; i <= quantidade; i++) {
            this.estacao.coletarLeituras();
            System.out.println("Leitura " + i + ": " + this.descreverUltimasLeituras(this.estacao));
        }
        this.rodarPrevisoes();
    }

    private void alternarEstacao() {
        if (this.estacao.isAtivo()) {
            this.estacao.desativar();
        } else {
            this.estacao.ativar();
        }
    }

    private String descreverUltimasLeituras(EstacaoDeColeta estacaoDescrita) {
        String texto = "";
        for (Sensor sensor : estacaoDescrita.getSensores()) {
            ArrayList<Double> leituras = sensor.getLeituras();
            if (!texto.equals("")) {
                texto = texto + " | ";
            }
            if (leituras.size() > 0) {
                texto = texto + String.format("%s %.1f %s", sensor.getTipo(),
                        leituras.get(leituras.size() - 1), sensor.getUnidade());
            }
        }
        return texto;
    }

    private String lerTexto(String pergunta) {
        System.out.print(pergunta);
        if (!this.teclado.hasNextLine()) {
            return "";
        }
        return this.teclado.nextLine().trim();
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

    private double lerDecimal(String pergunta) {
        System.out.print(pergunta);
        while (!this.teclado.hasNextDouble()) {
            if (!this.teclado.hasNext()) {
                return 0;
            }
            this.teclado.next();
            System.out.println("Valor inválido: digite um número (ex: 45,5).");
            System.out.print(pergunta);
        }
        double valor = this.teclado.nextDouble();
        if (this.teclado.hasNextLine()) {
            this.teclado.nextLine();
        }
        return valor;
    }
}
