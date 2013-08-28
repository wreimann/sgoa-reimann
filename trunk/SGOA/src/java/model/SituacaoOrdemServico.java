package model;

public enum SituacaoOrdemServico {
    EmExecucao("A"),
    Cancelado("C"),
    AguardandoPagamento("P"),
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
