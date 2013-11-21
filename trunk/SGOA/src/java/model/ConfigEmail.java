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
    @Column(name = "IdentificacaoEmail")
    private String identificacaoEmail;
    public String getIdentificacaoEmail() {
        return identificacaoEmail;
    }

    public void setIdentificacaoEmail(String identificacaoEmail) {
        this.identificacaoEmail = identificacaoEmail;
    }
    
    @Size(max = 250)
    @Column(name = "EmailEnvio")
    private String emailEnvio;
    public String getEmailEnvio() {
        return emailEnvio;
    }

    public void setEmailEnvio(String email) {
        this.emailEnvio = email;
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
    
    @Size(max = 250)
    @Column(name = "EmailRecebCliente")
    private String emailRecebCliente;
    public String getEmailRecebCliente() {
        return emailRecebCliente;
    }

    public void setEmailRecebCliente(String email) {
        this.emailRecebCliente = email;
    }
    
}
