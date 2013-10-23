package model;

public enum SituacaoOrdemServico {
    EmExecucao("E"),
    Cancelado("C"),
    FimReparo("R"),
    Finalizado("F");
    
    private String value;

    private SituacaoOrdemServico(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
