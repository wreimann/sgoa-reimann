package model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import model.Base.BaseEntidade;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "pessoa")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public class Pessoa extends BaseEntidade<Pessoa> {
    
     public Pessoa() {
        
    }

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "Nome")
    private String nome;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "Tipo")
    private char tipo;
    
    @Size(max = 250)
    @Column(name = "Email")
    private String email;
    
    @Size(max = 15)
    @Column(name = "TelefonePrimario")
    private String telefonePrimario;
    
    @Size(max = 15)
    @Column(name = "TelefoneSecundario")
    private String telefoneSecundario;
    
    @OneToOne(cascade = CascadeType.ALL, optional = true, orphanRemoval = true, mappedBy="pessoa")
    @PrimaryKeyJoinColumn
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private PessoaEndereco endereco;

  
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefonePrimario() {
        return telefonePrimario;
    }

    public void setTelefonePrimario(String telefonePrimario) {
        this.telefonePrimario = telefonePrimario;
    }
    
    public String getTelefoneSecundario() {
        return telefoneSecundario;
    }

    public void setTelefoneSecundario(String telefoneSecundario) {
        this.telefoneSecundario = telefoneSecundario;
    }

    @Override
    public String toString() {
        return getNome();
    }

    public PessoaEndereco getEndereco() {
        return endereco;
    }

    public void setEndereco(PessoaEndereco endereco) {
        this.endereco = endereco;
    }
}
