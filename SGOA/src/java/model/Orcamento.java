package model;

import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import model.Base.BaseEntidade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "orcamento")
public class Orcamento extends BaseEntidade<Orcamento> {

    public Orcamento() {
       
    }
     
    @Column(name = "numero")
    private int numero;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "DataCadastro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;
    
    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
     
    @ManyToOne(optional = false)
    @JoinColumn(name = "idcliente")
    private Cliente cliente;
 
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "idveiculo")
    private Veiculo veiculo;
 
    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }
    
    @JoinColumn(name = "idseguradora")
    @ManyToOne(optional = true)
    private Seguradora seguradora;

    public Seguradora getSeguradora() {
        return seguradora;
    }

    public void setSeguradora(Seguradora seguradora) {
        this.seguradora = seguradora;
    }
    
    @Column(name = "terceiro")
    private boolean terceiro;

    public boolean getTerceiro() {
        return terceiro;
    }

    public void setTerceiro(boolean terceiro) {
        this.terceiro = terceiro;
    }
    
    @Size(max = 200)
    @Column(name = "Obs")
    private String obs;

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
    
    @Column(name = "situacao")
    private String situacao;

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
    
    @OneToMany(cascade= CascadeType.ALL, orphanRemoval=true, fetch= FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name="idorcamento")
    private List<OrcamentoTipoServico> servicos;
 
     public List<OrcamentoTipoServico> getServicos() {
        return servicos;
    }

    public void setServicos(List<OrcamentoTipoServico> servicos) {
        this.servicos = servicos;
    }
    
     @Column(name = "valorAdicional")
    private double valorAdicional;

    public double getValorAdicional() {
        return valorAdicional;
    }

    public void setValorAdicional(double valorAdicional) {
        this.valorAdicional = valorAdicional;
    }
    
    @Column(name = "valorDesconto")
    private double valorDesconto;

    public double getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(double valorDesconto) {
        this.valorDesconto = valorDesconto;
    }
    
    @Column(name = "valorTotal")
    private double valorTotal;

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }
    
    @Column(name = "valorPecas")
    private double valorPecas;

    public double getValorPecas() {
        return valorPecas;
    }

    public void setValorPecas(double valorPecas) {
        this.valorPecas = valorPecas;
    }
}
