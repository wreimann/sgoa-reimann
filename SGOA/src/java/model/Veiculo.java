package model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import model.Base.BaseEntidadeAtivo;
import org.hibernate.annotations.Fetch;

@Entity
@Table(name = "veiculo")
public class Veiculo extends BaseEntidadeAtivo<Veiculo> {

    public Veiculo() {
        if (this.pessoa == null) {
            this.pessoa = new Pessoa();
        }
        if (this.modelo == null) {
            this.modelo = new Modelo();
        }
        placa = "";
    }
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade= CascadeType.MERGE)
    @JoinColumn(name = "idpessoa", insertable = true, updatable = true)
    @Fetch(org.hibernate.annotations.FetchMode.JOIN)
    private Pessoa pessoa;

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    
    @JoinColumn(name = "idmodelo")
    @ManyToOne(optional = false)
    private Modelo modelo;

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }
    
    @JoinColumn(name = "idcor")
    @ManyToOne(optional = false)
    private Cor cor;

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }
    
    @Column(name = "placa")
    private String placa;

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
    
    @Column(name = "anoFabricacao")
    private Integer anoFabricacao;

    public Integer getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(Integer ano) {
        this.anoFabricacao = ano;
    }
    
    @Column(name = "anoModelo")
    private Integer anoModelo;

    public Integer getAnoModelo() {
        return anoModelo;
    }

    public void setAnoModelo(Integer ano) {
        this.anoModelo = ano;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object.getClass().equals(this.getClass()))) {
            return false;
        }

        Veiculo other = (Veiculo) object;
        if ((this.placa.isEmpty() && !other.placa.isEmpty()) || (!this.placa.isEmpty() && !this.placa.equals(other.placa))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.placa != null ? this.placa.hashCode() : 0);
        return hash;
    }
}
