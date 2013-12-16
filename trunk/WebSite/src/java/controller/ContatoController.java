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
        try {
            enviarEmail(getMensagem(), getAssunto(), getEmail(), getNome());
            setNome(null);
            setEmail(null);
            setAssunto(null);
            setMensagem(null);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensagem enviado com sucesso!", ""));
        } catch (Exception ex) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Infelizmente ocorreu um erro ao enviar sua mensagem. Tente novamente mais tarde.", ""));
            Logger.getLogger(ContatoController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void enviarEmail(String mensagem, String assunto, String email, String Nome) throws Exception {
        try {
            java.util.Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.host", "mail.reimanscar.com.br");
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "21");
            props.setProperty("mail.mime.charset", "ISO-8859-1");
            // Cria a sessao passando as propriedades e a autenticação
            //javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, auth);
            javax.mail.Session session = javax.mail.Session.getDefaultInstance(props);
            // Gera um log no console referente ao processo de envio
            session.setDebug(false);
            Message msg = new MimeMessage(session);
            //remetente
            msg.setFrom(new InternetAddress("suporte@reimanscar.com.br", "Reiman´s Car"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress("contato@reimanscar.com.br"));
            msg.setSubject("Contato WebSite - " + assunto);
            msg.setText("Contato enviado por: " + Nome + "\nE-mail: " + email + "\nMensagem: \n" + mensagem);
            Transport transportTLS = session.getTransport();
            transportTLS.connect("mail.reimanscar.com.br", 541, "suporte@reimancar.com.br", "suporteReimansCar");
            transportTLS.sendMessage(msg, msg.getAllRecipients());
            transportTLS.close();
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
