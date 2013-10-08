package model;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import model.Base.BaseEntidade;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "ordemservico")
public class OrdemServico extends BaseEntidade<OrdemServico> {
    
    @JoinColumn(name = "idorcamento")
    @ManyToOne(optional = false)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Orcamento orcamento;

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }
    
    @Column(name = "situacao")
    private char situacao;

    public char getSituacao() {
        return situacao;
    }

    public void setSituacao(char situacao) {
        this.situacao = situacao;
    }
    
    @Basic(optional = false)
    @Column(name = "DataAprovacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAprovacao;

    public Date getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(Date dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }
    
    @JoinColumn(name = "idfuncAprovacao")
    @ManyToOne(optional = true)
    private Funcionario funcionarioAprovacao;

    public Funcionario getFuncionarioAprovacao() {
        return funcionarioAprovacao;
    }

    public void setFuncionarioAprovacao(Funcionario funcionarioAprovacao) {
        this.funcionarioAprovacao = funcionarioAprovacao;
    }
    
    @Size(max = 200)
    @Column(name = "Obs")
    private String obs;

    public String getObs() {
        return obs;
    }
    
    @JoinColumn(name = "idfluxo")
    @ManyToOne(optional = false)
    private Fluxo fluxo;

    public Fluxo getFluxo() {
        return fluxo;
    }

    public void setFluxo(Fluxo fluxo) {
        this.fluxo = fluxo;
    }
}
