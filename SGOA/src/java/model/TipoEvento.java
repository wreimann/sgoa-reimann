package model;

public enum TipoEvento {

    InicioAtividade("Início da Atividade"),
    FimAtividade("Conclusão da Atividade"),
    Cancelamento("Cancelamento da Atividade"),
    InterrupcaoAtividade("Interrupção da Atividade"),
    ReinicioAtividade("Reinício da Atividade"),
    Informacao("Informação");
    private String value;

    private TipoEvento(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
