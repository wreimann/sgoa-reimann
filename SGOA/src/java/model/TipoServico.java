package model;

import javax.persistence.Entity;
import javax.persistence.Table;
import model.Base.BaseEntidadeDescricao;

@Entity
@Table(name = "tiposervico")
public class TipoServico extends BaseEntidadeDescricao<TipoServico> {
    
}
