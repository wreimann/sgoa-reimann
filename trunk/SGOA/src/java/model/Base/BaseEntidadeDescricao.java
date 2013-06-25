package model.Base;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@MappedSuperclass
public class BaseEntidadeDescricao<T extends BaseEntidadeDescricao> extends BaseEntidadeAtivo<BaseEntidadeAtivo> {
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "Descricao")
    private String descricao;   
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }  
    
    @Override
    public String toString() {
        return getId().toString() + " - " + getDescricao();
    }
}
