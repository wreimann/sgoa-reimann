package model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import model.Base.BaseEntidade;

@Entity
@Table(name = "fluxo_etapa")
public class FluxoEtapa extends BaseEntidade<FluxoEtapa> {

    @JoinColumn(name = "idfluxo")
    @ManyToOne(optional = false)
    private Fluxo fluxo;

    public Fluxo getFluxo() {
        return fluxo;
    }

    public void setFluxo(Fluxo fluxo) {
        this.fluxo = fluxo;
    }
    
    @JoinColumn(name = "idetapa")
    @ManyToOne(optional = false)
    private Etapa etapa;

    public Etapa getEtapa() {
        return etapa;
    }

    public void setEtapa(Etapa etapa) {
        this.etapa = etapa;
    }
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "enviaEmailCliente")
    private boolean enviaEmailCliente;

    public boolean getEnviaEmailCliente() {
        return enviaEmailCliente;
    }

    public void setEnviaEmailCliente(boolean enviaEmailCliente) {
        this.enviaEmailCliente = enviaEmailCliente;
    }
    @Basic(optional = false)
    @NotNull
    @Column(name = "enviaEmailEspecifico")
    private boolean enviaEmailEspecifico;

    public boolean getEnviaEmailEspecifico() {
        return enviaEmailEspecifico;
    }

    public void setEnviaEmailEspecifico(boolean enviaEmailEspecifico) {
        this.enviaEmailEspecifico = enviaEmailEspecifico;
    }
    
    @Column(name = "emailEspecifico")
    private String emailEspecifico;

    public String getEmailEspecifico() {
        return emailEspecifico;
    }

    public void setEmailEspecifico(String emailEspecifico) {
        this.emailEspecifico = emailEspecifico;
    }
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "enviaEmailGerente")
    private boolean enviaEmailGerente;

    public boolean getEnviaEmailGerente() {
        return enviaEmailGerente;
    }

    public void setEnviaEmailGerente(boolean enviaEmailGerente) {
        this.enviaEmailGerente = enviaEmailGerente;
    }
}
