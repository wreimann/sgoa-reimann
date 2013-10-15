package model;

import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import model.Base.BaseEntidade;

@Entity
@Table(name = "ordemservico_etapa")
public class OrdemServicoEtapa extends BaseEntidade<OrdemServicoEtapa> {

    @JoinColumn(name = "idordemservico")
    @ManyToOne(optional = false)
    private OrdemServico ordemservico;

    public OrdemServico getOrdemServico() {
        return ordemservico;
    }

    public void setOrdemServico(OrdemServico ordemservico) {
        this.ordemservico = ordemservico;
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
    @Column(name = "DataEntrada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEntrada;

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Date dataEntrada) {
        this.dataEntrada = dataEntrada;
    }
    
    @Column(name = "DataSaida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSaida;

    public Date getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
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
    
    @Column(name = "horasTrabalhadas")
    private double horasTrabalhadas;

    public double getHorasTrabalhadas() {
        return horasTrabalhadas;
    }

    public void setHorasTrabalhadas(double horasTrabalhadas) {
        this.horasTrabalhadas = horasTrabalhadas;
    }
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "obs")
    private String obs;

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
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
