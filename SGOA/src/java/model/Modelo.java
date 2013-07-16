package model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import model.Base.BaseEntidadeDescricao;

@Entity
@Table(name = "modelo")
public class Modelo extends BaseEntidadeDescricao<Modelo> {

    public Modelo() {

    }
    
    @JoinColumn(name = "idmarca")
    @ManyToOne(optional = false)
    private Marca marca;

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    @Override
    public String toString() {
        if (getMarca() != null) {
            return marca.getDescricao() + " - " + super.getDescricao();
        } else {
            return super.toString();
        }
    }
}
