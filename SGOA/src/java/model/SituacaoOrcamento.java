package model;

public enum SituacaoOrcamento {
    EmAberto("E"),
    Cancelado("C"),
    Aprovado("A");
    private String value;

    private SituacaoOrcamento(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
