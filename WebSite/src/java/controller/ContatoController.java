package controller;

import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
        FacesContext context = FacesContext.getCurrentInstance();
        if (getMensagem().isEmpty() || getAssunto().isEmpty() || getEmail().isEmpty() || getNome().isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Campos obrigatórios não preenchidos.", ""));
        } else {
            try {
                enviarEmail(getMensagem(), getAssunto(), getEmail(), getNome());
                setNome(null);
                setEmail(null);
                setAssunto(null);
                setMensagem(null);
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensagem enviado com sucesso!", ""));
            } catch (Exception ex) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Infelizmente ocorreu um erro ao enviar sua mensagem. Tente novamente mais tarde.", ""));
            }
        }
    }

    public static void enviarEmail(String mensagem, String assunto, String email, String Nome) throws Exception {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "mail.reimanscar.com.br");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("contato@reimanscar.com.br", "taxi1010");
                }
            });
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("contato@reimanscar.com.br"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("contato@reimanscar.com.br"));
            message.setSubject("Contato WebSite / " + assunto);
            message.setText("Contato enviado por: " + Nome + "\nE-mail: " + email + "\nMensagem: \n" + mensagem);
            Transport.send(message);
        } catch (Exception ex) {
            throw ex;
        }
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
