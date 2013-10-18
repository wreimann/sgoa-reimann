package model;

import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import model.Base.BaseEntidadeDescricao;

@Entity
@Table(name = "ordemservico_evento")
public class OrdemServicoEvento extends BaseEntidadeDescricao<OrdemServicoEvento> {

    @JoinColumn(name = "idordemservicoetapa")
    @ManyToOne(optional = false)
    private OrdemServicoEtapa etapa;

    public OrdemServicoEtapa getEtapa() {
        return etapa;
    }

    public void setEtapa(OrdemServicoEtapa etapa) {
        this.etapa = etapa;
    }
   
   @Basic(optional = false)
    @NotNull
    @Column(name = "DataOcorrencia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataOcorrencia;

    public Date getDataOcorrencia() {
        return dataOcorrencia;
    }

    public void setDataOcorrencia(Date dataOcorrencia) {
        this.dataOcorrencia = dataOcorrencia;
    }
   
    @Column(name = "tipoevento", nullable = false)  
    @Enumerated(EnumType.STRING)  
    private TipoEvento tipoEvento;

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }
    
    @JoinColumn(name = "idfuncionario")
    @ManyToOne(optional = true)
    private Funcionario funcionario;

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "idordemservicoetapa")
    private List<OrdemServicoFoto> fotos;

    public List<OrdemServicoFoto> getFotos() {
        return fotos;
    }

    public void setFotos(List<OrdemServicoFoto> fotos) {
        this.fotos = fotos;
    }
    
    
    
}
