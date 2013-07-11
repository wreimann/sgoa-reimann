package model;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
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
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;

@Entity
@Table(name = "cliente")
public class Cliente extends BaseEntidadeAtivo<Cliente> {

     public Cliente() {
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
    
    @OneToMany(cascade={javax.persistence.CascadeType.ALL}, orphanRemoval=true, fetch= FetchType.LAZY)
    @JoinColumn(name="idpessoa")
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