package model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import model.Base.BaseEntidadeAtivo;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;

@Entity
@Table(name = "veiculo")
public class Veiculo extends BaseEntidadeAtivo<Veiculo>  {
    
     public Veiculo() {
        pessoa = new Pessoa();
    }
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "idpessoa", insertable = true, updatable = true)
    @Fetch(org.hibernate.annotations.FetchMode.JOIN)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Pessoa pessoa;
 
    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    
    @JoinColumn(name = "idmodelo")
    @NotNull
    @ManyToOne(optional = false)
    private Modelo modelo;

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }
    
    @JoinColumn(name = "idcor")
    @NotNull
    @ManyToOne(optional = false)
    private Cor cor;

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "placa")
    private String placa;
    
    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "anoFabricacao")
    private int anoFabricacao;
    
    public int getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(int ano) {
        this.anoFabricacao = ano;
    }
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "anoModelo")
    private int anoModelo;
    
    public int getAnoModelo() {
        return anoModelo;
    }

    public void setAnoModelo(int ano) {
        this.anoModelo = ano;
    }
}
