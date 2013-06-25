package model;

import javax.persistence.Entity;
import javax.persistence.Table;
import model.Base.BaseEntidadeDescricao;

@Entity
@Table(name = "marca")
public class Marca extends BaseEntidadeDescricao<Marca> {
    
}
