package model;

import java.util.ArrayList;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "pessoajuridica")
@DiscriminatorValue(value = "J")
public class PessoaJuridica extends Pessoa {
    
    public PessoaJuridica() {
       super();
    }

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 18)
    @Column(name = "CNPJ")
    private String cnpj;  

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
