package model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import model.Base.BaseEntidade;


@Entity
@Table(name = "pessoaendereco")
public class PessoaEndereco extends BaseEntidade<PessoaEndereco> {
    
    public PessoaEndereco() {
    
    }
    
    @OneToOne
    @JoinColumn(name="idpessoa")
    private Pessoa pessoa;
    

    @Size(max = 250)
    @Column(name = "Logradouro")
    private String logradouro;
    
    @Column(name = "Predical")
    private Integer predical;
    
    @Size(max = 12)
    @Column(name = "CEP")
    private String cep;
    
    @Size(max = 200)
    @Column(name = "Bairro")
    private String bairro;
    
    @Size(max = 200)
    @Column(name = "Municipio")
    private String municipio;
    
    @Size(max = 2)
    @Column(name = "UF")
    private String uf;
    
    @Size(max = 200)
    @Column(name = "Complemento")
    private String complemento;

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
  
    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Integer getPredical() {
        return predical;
    }

    public void setPredical(Integer predical) {
        this.predical = predical;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

   
    @Override
    public String toString() {
        return getLogradouro() + "," + getPredical() + " - "+ getMunicipio();
    }
    
}
