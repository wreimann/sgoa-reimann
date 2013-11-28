package controller;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;


@ManagedBean
@ViewScoped
public class ContatoController implements Serializable {
    
    private String email;
    private String assunto;
    private String nome;
    private String mensagem;
    private MapModel simpleModel; 

    
    public ContatoController() {
        simpleModel = new DefaultMapModel();  
        LatLng coord1 = new LatLng(-25.490953207571426, -49.228888249999954);  
        simpleModel.addOverlay(new Marker(coord1, "Reiman´s Car Recuperadora de Veículos"));  
    }

    public void enviar() {
        setNome(null);
        setEmail(null);
        setAssunto(null);
        setMensagem(null);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensagem enviado com sucesso!", ""));        
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the assunto
     */
    public String getAssunto() {
        return assunto;
    }

    /**
     * @param assunto the assunto to set
     */
    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the mensagem
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * @param mensagem the mensagem to set
     */
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public MapModel getSimpleModel() {  
        return simpleModel;  
    } 
}
