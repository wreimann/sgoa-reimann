package model;

public enum TipoEvento {
    InicioAtividade("I"),
    FimAtividade("F"),
    Cancelamento("C"),
    InterrupcaoAtividade("I"),
    RetomadaAtividade("R");
    
    private String value;

    private TipoEvento(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
