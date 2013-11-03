package model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import model.Base.BaseEntidade;

@Entity
@Table(name = "ordemservico_foto")
public class OrdemServicoFoto extends BaseEntidade<OrdemServicoFoto> {

    @JoinColumn(name = "idordemservicoevento")
    @ManyToOne(optional = false)
    private OrdemServicoEvento evento;

    public OrdemServicoEvento getEvento() {
        return evento;
    }

    public void setEvento(OrdemServicoEvento evento) {
        this.evento = evento;
    }
   
    @Basic(optional = false)
    @NotNull
    @Column(name = "NomeArquivo")
    private String nomeArquivo;

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }
    
    @NotNull
    @Column(name = "imagem")
    private byte[] imagem;

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }
    
    
}
