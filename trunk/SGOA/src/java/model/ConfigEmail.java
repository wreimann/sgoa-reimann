package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import model.Base.BaseEntidade;

@Entity
@Table(name = "configemail")
public class ConfigEmail extends BaseEntidade<ConfigEmail> {

    @Size(max = 100)
    @Column(name = "AssuntoEmail")
    private String assuntoEmail;
    public String getAssuntoEmail() {
        return assuntoEmail;
    }

    public void setAssuntoEmail(String assuntoEmail) {
        this.assuntoEmail = assuntoEmail;
    }
    
    @Size(max = 1000)
    @Column(name = "TextoEmail")
    private String textoEmail;
    public String getTextoEmail() {
        return textoEmail;
    }

    public void setTextoEmail(String textoEmail) {
        this.textoEmail = textoEmail;
    }
    
    @Size(max = 100)
    @Column(name = "IdentificacaoEmail")
    private String identificacaoEmail;
    public String getIdentificacaoEmail() {
        return identificacaoEmail;
    }

    public void setIdentificacaoEmail(String identificacaoEmail) {
        this.identificacaoEmail = identificacaoEmail;
    }
    
    @Size(max = 250)
    @Column(name = "Email")
    private String email;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @Size(max = 20)
    @Column(name = "Senha")
    private String senha;
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    @Column(name = "ServidorNecessitaAutenticacao")
    private Boolean servidorNecessitaAutenticacao;
    public Boolean getServidorNecessitaAutenticacao() {
        return servidorNecessitaAutenticacao;
    }

    public void setServidorNecessitaAutenticacao(Boolean servidorNecessitaAutenticacao) {
        this.servidorNecessitaAutenticacao = servidorNecessitaAutenticacao;
    }
    
    @Size(max = 250)
    @Column(name = "ServidorSMTP")
    private String servidorSMTP;
    public String getServidorSMTP() {
        return servidorSMTP;
    }

    public void setServidorSMTP(String servidorSMTP) {
        this.servidorSMTP = servidorSMTP;
    }
    
    @Column(name = "Porta")
    private Integer porta;
    public Integer getPorta() {
        return porta;
    }
    public void setPorta(Integer porta) {
        this.porta = porta;
    }
}
