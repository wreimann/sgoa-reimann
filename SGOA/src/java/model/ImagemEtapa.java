package model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import model.Base.BaseEntidadeDescricao;

@Entity
@Table(name = "imagem_etapa")
public class ImagemEtapa extends BaseEntidadeDescricao<ImagemEtapa> {
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "titulo")
    private String titulo;   
    
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }  
    
}
