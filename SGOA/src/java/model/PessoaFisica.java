package model;

import java.util.ArrayList;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "pessoafisica")
@DiscriminatorValue(value = "F")
@PrimaryKeyJoinColumn(name = "id")
public class PessoaFisica extends Pessoa {
    
    public PessoaFisica() {
        super();
     
    }

    @Size(min = 1, max = 15)
    @NotNull
    @Column(name = "CPF")
    private String cpf;
    
    @Column(name = "Sexo")
    private char sexo;
    @Column(name = "DataNascimento")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    
   
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    
}
