public abstract class ComponenteTerritorial {

    private String id;
    private String nome;
    private String regiao;
    private String status;

    public ComponenteTerritorial(String id, String nome, String regiao) {
        this.id = id;
        this.nome = nome;
        this.regiao = regiao;
        this.status = "INATIVO";
    }

    public void ativar() {
        this.status = "ATIVO";
        System.out.println("Componente " + this.nome + " ativado.");
    }

    public void desativar() {
        this.status = "INATIVO";
        System.out.println("Componente " + this.nome + " desativado.");
    }

    public boolean isAtivo() {
        return this.status.equals("ATIVO");
    }

    public abstract String verificarStatus();

    public String getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getRegiao() {
        return this.regiao;
    }

    public String getStatus() {
        return this.status;
    }
}