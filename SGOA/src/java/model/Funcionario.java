package model;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import model.Base.BaseEntidadeAtivo;
import org.hibernate.annotations.Fetch;

@Entity
@Table(name = "funcionario")
public class Funcionario extends BaseEntidadeAtivo<Funcionario> {

     public Funcionario() {
        pessoa = new PessoaFisica();
    }
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade={CascadeType.ALL})
    @JoinColumn(name = "idpessoa", insertable = true, updatable = true)
    @Fetch(org.hibernate.annotations.FetchMode.JOIN)
    private PessoaFisica pessoa;
 
    public PessoaFisica getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaFisica pessoa) {
        this.pessoa = pessoa;
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
    
    
    @JoinColumn(name = "idperfil")
    @ManyToOne(optional = true)
    private Perfil perfilAcesso;

    public Perfil getPerfilAcesso() {
        return perfilAcesso;
    }

    public void setPerfilAcesso(Perfil perfil) {
        this.perfilAcesso = perfil;
    }
    
    @Column(name = "Senha")
    private String senha;

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    @JoinColumn(name = "idprofissao")
    @ManyToOne(optional = false)
    private Profissao profissao;

    public Profissao getProfissao() {
        return profissao;
    }

    public void setProfissao(Profissao profissao) {
        this.profissao = profissao;
    }
    
    @JoinColumn(name = "idsetor")
    @ManyToOne(optional = false)
    private Setor setorTrabalho;

    public Setor getSetorTrabalho() {
        return setorTrabalho;
    }

    public void setSetorTrabalho(Setor setor) {
        this.setorTrabalho = setor;
    }
    
    @Override
    public String toString() {
        return super.toString() + " - " + getPessoa().getNome();
    }
}
