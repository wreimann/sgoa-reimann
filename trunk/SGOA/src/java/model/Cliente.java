package model;

import java.util.ArrayList;
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
import model.Base.BaseEntidadeAtivo;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "cliente")
public class Cliente extends BaseEntidadeAtivo<Cliente> {

     public Cliente() {
        pessoa = new Pessoa();
         if (veiculos == null) {
            veiculos = new ArrayList<Veiculo>();
        }
    }
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    @JoinColumn(name = "idpessoa", insertable = true, updatable = true)
    @Fetch(org.hibernate.annotations.FetchMode.JOIN)
    private Pessoa pessoa;
 
    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    
    @OneToMany(cascade= CascadeType.ALL, orphanRemoval=true, fetch= FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name="idpessoa", referencedColumnName="idpessoa")
    private List<Veiculo> veiculos;
 
     public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
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
    
    @Override
    public String toString() {
        return super.toString() + " - " + getPessoa().getNome();
    }
}
