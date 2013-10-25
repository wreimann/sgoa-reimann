package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import model.Base.BaseEntidade;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "ordemservico")
public class OrdemServico extends BaseEntidade<OrdemServico> {

    public OrdemServico() {
        if (etapas == null) {
            etapas = new ArrayList<OrdemServicoEtapa>();
        }
    }
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
    @Size(max = 250)
    @Column(name = "Obs")
    private String obs;

    public String getObs() {
        return obs;
    }
    @JoinColumn(name = "idEtapaAtual")
    @ManyToOne(optional = true)
    private OrdemServicoEtapa etapaAtual;

    public OrdemServicoEtapa getEtapaAtual() {
        return etapaAtual;
    }

    public void setEtapaAtual(OrdemServicoEtapa etapaAtual) {
        this.etapaAtual = etapaAtual;
    }
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "idordemservico")
    @OrderBy("dataCadastro DESC")
    private List<OrdemServicoEtapa> etapas;

    public List<OrdemServicoEtapa> getEtapas() {
        return etapas;
    }

    public void setEtapas(List<OrdemServicoEtapa> etapas) {
        this.etapas = etapas;
    }
}
