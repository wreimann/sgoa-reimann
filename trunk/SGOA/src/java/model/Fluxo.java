package model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import model.Base.BaseEntidadeDescricao;

@Entity
@Table(name = "fluxo")
public class Fluxo extends BaseEntidadeDescricao<Fluxo> {
    
    @OneToMany(cascade= CascadeType.ALL, orphanRemoval=true, fetch= FetchType.EAGER)
    @JoinColumn(name="idfluxo")
    private List<FluxoEtapa> etapas;
 
     public List<FluxoEtapa> getEtapas() {
        return etapas;
    }

    public void setEtapas(List<FluxoEtapa> etapas) {
        this.etapas = etapas;
    }
    
}
