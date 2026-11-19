public class DadosTerritorio {

    private static final int POPULACAO_MAXIMA = 5000000;
    private static final int TAMANHO_MINIMO_REGIAO = 3;

    private static final double CHUVA_MINIMA = 10.0;
    private static final double CHUVA_MAXIMA = 100.0;
    private static final double RIO_MINIMO = 1.0;
    private static final double RIO_MAXIMO = 8.0;
    private static final double UMIDADE_MINIMA = 50.0;
    private static final double UMIDADE_MAXIMA = 95.0;

    private String regiao;
    private int populacao;

    private double limiarChuva;
    private double limiarNivelRio;
    private double limiarUmidade;

    private String codigoAcesso;

    public DadosTerritorio(String regiao, int populacao, String codigoAcesso) {
        this.regiao = regiao;
        this.populacao = populacao;
        this.codigoAcesso = codigoAcesso;
        this.limiarChuva = 40.0;
        this.limiarNivelRio = 3.0;
        this.limiarUmidade = 80.0;
    }

    public String getRegiao() {
        return this.regiao;
    }

    public int getPopulacao() {
        return this.populacao;
    }

    public double getLimiarChuva() {
        return this.limiarChuva;
    }

    public double getLimiarNivelRio() {
        return this.limiarNivelRio;
    }

    public double getLimiarUmidade() {
        return this.limiarUmidade;
    }

    public void setRegiao(String novaRegiao) {
        if (novaRegiao == null || novaRegiao.trim().length() < TAMANHO_MINIMO_REGIAO) {
            System.out.println("Erro de Validação: a região precisa ter pelo menos "
                    + TAMANHO_MINIMO_REGIAO + " caracteres. Região mantida: " + this.regiao);
        } else {
            this.regiao = novaRegiao.trim();
            System.out.println("Região atualizada para: " + this.regiao);
        }
    }

    public void setPopulacao(int novaPopulacao) {
        if (novaPopulacao <= 0 || novaPopulacao > POPULACAO_MAXIMA) {
            System.out.println("Erro de Validação: a população deve ser maior que 0 e no máximo "
                    + POPULACAO_MAXIMA + ". População mantida: " + this.populacao);
        } else {
            this.populacao = novaPopulacao;
            System.out.println("População atualizada para: " + this.populacao);
        }
    }

    public void setLimiarChuva(double novoLimiar, String codigoInformado) {
        if (!this.codigoCorreto(codigoInformado)) {
            return;
        }
        if (novoLimiar >= CHUVA_MINIMA && novoLimiar <= CHUVA_MAXIMA) {
            this.limiarChuva = novoLimiar;
            System.out.println(String.format("Limiar de chuva atualizado para %.1f mm/h.", novoLimiar));
        } else {
            System.out.println(String.format("Erro de Validação: o limiar de chuva deve ficar entre %.1f e %.1f mm/h. Valor mantido: %.1f",
                    CHUVA_MINIMA, CHUVA_MAXIMA, this.limiarChuva));
        }
    }

    public void setLimiarNivelRio(double novoLimiar, String codigoInformado) {
        if (!this.codigoCorreto(codigoInformado)) {
            return;
        }
        if (novoLimiar >= RIO_MINIMO && novoLimiar <= RIO_MAXIMO) {
            this.limiarNivelRio = novoLimiar;
            System.out.println(String.format("Limiar do nível do rio atualizado para %.1f m.", novoLimiar));
        } else {
            System.out.println(String.format("Erro de Validação: o limiar do rio deve ficar entre %.1f e %.1f m. Valor mantido: %.1f",
                    RIO_MINIMO, RIO_MAXIMO, this.limiarNivelRio));
        }
    }

    public void setLimiarUmidade(double novoLimiar, String codigoInformado) {
        if (!this.codigoCorreto(codigoInformado)) {
            return;
        }
        if (novoLimiar >= UMIDADE_MINIMA && novoLimiar <= UMIDADE_MAXIMA) {
            this.limiarUmidade = novoLimiar;
            System.out.println(String.format("Limiar de umidade atualizado para %.1f %%.", novoLimiar));
        } else {
            System.out.println(String.format("Erro de Validação: o limiar de umidade deve ficar entre %.1f e %.1f %%. Valor mantido: %.1f",
                    UMIDADE_MINIMA, UMIDADE_MAXIMA, this.limiarUmidade));
        }
    }

    public void exibirDados() {
        System.out.println("Região: " + this.regiao);
        System.out.println("População: " + this.populacao + " habitantes");
        System.out.println(String.format("Limiar de chuva: %.1f mm/h", this.limiarChuva));
        System.out.println(String.format("Limiar do nível do rio: %.1f m", this.limiarNivelRio));
        System.out.println(String.format("Limiar de umidade do solo: %.1f %%", this.limiarUmidade));
        System.out.println("Código de acesso: ******** (protegido)");
    }

    private boolean codigoCorreto(String codigoInformado) {
        if (codigoInformado != null && codigoInformado.equals(this.codigoAcesso)) {
            return true;
        }
        System.out.println("Código de acesso incorreto. Alteração negada.");
        return false;
    }
}