package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import model.Base.BaseEntidade;

@Entity
@Table(name = "orcamento_tiposervico")
public class OrcamentoTipoServico extends BaseEntidade<OrcamentoTipoServico> {
    
    @JoinColumn(name = "idOrcamento")
    @ManyToOne(optional = false)
    private Orcamento orcamento;

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }
    
    @JoinColumn(name = "idTipoServico")
    @ManyToOne(optional = false)
    private TipoServico tipoServico;

    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }
    
    @Column(name = "horas")
    private double horas;

    public double getHoras() {
        return horas;
    }

    public void setHoras(double horas) {
        this.horas = horas;
    }
    
    @Column(name = "valorHora")
    private double valorHoras;

    public double getValorHoras() {
        return valorHoras;
    }

    public void setValorHoras(double valorHoras) {
        this.valorHoras = valorHoras;
    }
    
    @Column(name = "desconto")
    private double desconto;

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }
    
    @Column(name = "total")
    private double total;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
