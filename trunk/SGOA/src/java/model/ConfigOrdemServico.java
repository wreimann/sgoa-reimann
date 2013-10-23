package model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import model.Base.BaseEntidade;

@Entity
@Table(name = "config_ordemservico")
public class ConfigOrdemServico extends BaseEntidade<ConfigOrdemServico> {
    
    @JoinColumn(name = "idEtapaInicial")
    @ManyToOne(optional = true)
    private Etapa etapaInicial;

    public Etapa getEtapaInicial() {
        return etapaInicial;
    }

    public void setEtapaInicial(Etapa etapaInicial) {
        this.etapaInicial = etapaInicial;
    }
    
    @JoinColumn(name = "idEtapaInicialSeguradora")
    @ManyToOne(optional = true)
    private Etapa etapaInicialSeguradora;

    public Etapa getEtapaInicialSeguradora() {
        return etapaInicialSeguradora;
    }

    public void setEtapaInicialSeguradora(Etapa etapaInicialSeguradora) {
        this.etapaInicialSeguradora = etapaInicialSeguradora;
    }
    
    @JoinColumn(name = "idEtapaFimConcerto")
    @ManyToOne(optional = true)
    private Etapa etapaFimConcerto;

    public Etapa getEtapaFimConcerto() {
        return etapaFimConcerto;
    }

    public void setEtapaFimConcerto(Etapa etapaFimConcerto) {
        this.etapaFimConcerto = etapaFimConcerto;
    }
    
    @JoinColumn(name = "idEtapaCancelamentoConcerto")
    @ManyToOne(optional = true)
    private Etapa etapaCancelamentoConcerto;

    public Etapa getEtapaCancelamentoConcerto() {
        return etapaCancelamentoConcerto;
    }

    public void setEtapaCancelamentoConcerto(Etapa etapaCancelamentoConcerto) {
        this.etapaCancelamentoConcerto = etapaCancelamentoConcerto;
    }
    
    @JoinColumn(name = "idEtapaConclusaoOrdemServico")
    @ManyToOne(optional = true)
    private Etapa etapaConclusaoOrdemServico;

    public Etapa getEtapaConclusaoOrdemServico() {
        return etapaConclusaoOrdemServico;
    }

    public void setEtapaConclusaoOrdemServico(Etapa etapaConclusaoOrdemServico) {
        this.etapaConclusaoOrdemServico = etapaConclusaoOrdemServico;
    }
    
}
