package model;

import javax.persistence.CascadeType;
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

    @OneToOne(optional=false, orphanRemoval=true)
    @JoinColumn(name="idpessoa")
    private Pessoa pessoa;

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    @Column(name = "Logradouro")
    @Size(max = 250)
    private String logradouro;
    @Column(name = "Predical")
    private Integer predical;
    @Column(name = "cep")
    @Size(max = 12)
    private String cep;
    @Column(name = "Bairro")
    @Size(max = 200)
    private String bairro;
    @Column(name = "Municipio")
    @Size(max = 200)
    private String municipio;
    @Column(name = "UF")
    @Size(max = 2)
    private String uf;
    @Column(name = "Complemento")
    @Size(max = 200)
    private String complemento;

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
        return getLogradouro() + "," + getPredical() + " - " + getMunicipio();
    }
}
