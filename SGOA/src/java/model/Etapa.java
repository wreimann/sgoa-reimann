package model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import model.Base.BaseEntidadeDescricao;

@Entity
@Table(name = "etapa")
public class Etapa extends BaseEntidadeDescricao<Etapa> {

    @JoinColumn(name = "idtiposervico")
    @ManyToOne(optional = false)
    private TipoServico tipoServico;

    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }
   
    @JoinColumn(name = "idsetor")
    @ManyToOne(optional = false)
    private Setor setor;

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "VisivelWebSite")
    private boolean visivelWebSite;

    public boolean getVisivelWebSite() {
        return visivelWebSite;
    }

    public void setVisivelWebSite(boolean visivelWebSite) {
        this.visivelWebSite = visivelWebSite;
    }
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "EnviaEmailInicio")
    private boolean enviaEmailInicio;

    public boolean getEnviaEmailInicio() {
        return enviaEmailInicio;
    }

    public void setEnviaEmailInicio(boolean enviaEmailInicio) {
        this.enviaEmailInicio = enviaEmailInicio;
    }
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "EnviaEmailFim")
    private boolean enviaEmailFim;

    public boolean getEnviaEmailFim() {
        return enviaEmailFim;
    }

    public void setEnviaEmailFim(boolean enviaEmailFim) {
        this.enviaEmailFim = enviaEmailFim;
    }
    
    @JoinColumn(name = "IdImagem")
    @ManyToOne(optional = false)
    private ImagemEtapa imagem;

    public ImagemEtapa getImagem() {
        return imagem;
    }

    public void setImagem(ImagemEtapa imagem) {
        this.imagem = imagem;
    }
}
