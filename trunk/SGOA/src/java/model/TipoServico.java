package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import model.Base.BaseEntidadeDescricao;

@Entity
@Table(name = "tiposervico")
public class TipoServico extends BaseEntidadeDescricao<TipoServico> {
    
    @Column(name = "ValorHoraPadrao")
    private double valorHoraPadrao;
    
    public double getValorHoraPadrao() {
        return valorHoraPadrao;
    }

    public void setValorHoraPadrao(double valor) {
        this.valorHoraPadrao = valor;
    }
    
    
}
