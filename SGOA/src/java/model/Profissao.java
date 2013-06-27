package model;

import javax.persistence.Entity;
import javax.persistence.Table;
import model.Base.BaseEntidadeDescricao;

@Entity
@Table(name = "profissao")
public class Profissao extends BaseEntidadeDescricao<Profissao> {
    
}
