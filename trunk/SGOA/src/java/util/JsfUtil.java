package util;

import facede.ConfigEmailFacade;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import model.ConfigEmail;
import model.Pessoa;
import org.hibernate.Session;
import org.primefaces.context.RequestContext;

public class JsfUtil {

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static void addErrorMessage(Exception ex, String defaultMsg) {
        /*String msg = ex.getLocalizedMessage();
         if (msg != null && msg.length() > 0) {
         addErrorMessage(msg);
         } else {*/
        addErrorMessage(defaultMsg);
        //}
        RequestContext.getCurrentInstance().addCallbackParam("exceptionThrown", ex);
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addErrorMessageExterna(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    public static void enviarEmail(Session sessao, Pessoa pessoa, String assuntoEmail, String mensagem) {
        try {
            ConfigEmailFacade ebjEmail = new ConfigEmailFacade();
            final ConfigEmail config = ebjEmail.obterPorId(sessao, 1);
            java.util.Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.host", config.getServidorSMTP());
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", config.getPorta().toString());
            props.setProperty("mail.mime.charset", "ISO-8859-1");
            // Cria a sessao passando as propriedades e a autenticação
            //javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, auth);
            javax.mail.Session session = javax.mail.Session.getDefaultInstance(props);
            // Gera um log no console referente ao processo de envio
            session.setDebug(true);
            //cria a mensagem setando o remetente e seus destinatários
            Message msg = new MimeMessage(session);
            //aqui seta o remetente
            msg.setFrom(new InternetAddress(config.getEmail(), config.getIdentificacaoEmail()));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(pessoa.getEmail(), pessoa.getNome()));
            if (assuntoEmail.isEmpty()) {
                msg.setSubject(config.getAssuntoEmail());
            } else {
                msg.setSubject(assuntoEmail);
            }
            if (mensagem.isEmpty()) {
                msg.setText(config.getTextoEmail());
            } else {
                msg.setText(mensagem);
            }
            Transport transportTLS = session.getTransport();
            transportTLS.connect(config.getServidorSMTP(), config.getPorta(), config.getEmail(), config.getSenha());
            transportTLS.sendMessage(msg, msg.getAllRecipients());
            transportTLS.close();
        } catch (Exception ex) {
            Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void enviarEmailOficina(Session sessao, String placa, String mensagem) {
        try {
            ConfigEmailFacade ebjEmail = new ConfigEmailFacade();
            final ConfigEmail config = ebjEmail.obterPorId(sessao, 1);
            java.util.Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.host", config.getServidorSMTP());
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", config.getPorta().toString());
            props.setProperty("mail.mime.charset", "ISO-8859-1");
            // Cria a sessao passando as propriedades e a autenticação
            //javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, auth);
            javax.mail.Session session = javax.mail.Session.getDefaultInstance(props);
            // Gera um log no console referente ao processo de envio
            session.setDebug(true);
            //cria a mensagem setando o remetente e seus destinatários
            Message msg = new MimeMessage(session);
            //aqui seta o remetente
            msg.setFrom(new InternetAddress(config.getEmail(), config.getIdentificacaoEmail()));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress("contato@reimanscar.com.br"));
            msg.setSubject("Interação do cliente no serviço");
            msg.setText(mensagem + "<br/>" + "Mensagem enviado pelo cliente com veículo de placa: " + placa);
            Transport transportTLS = session.getTransport();
            transportTLS.connect(config.getServidorSMTP(), config.getPorta(), config.getEmail(), config.getSenha());
            transportTLS.sendMessage(msg, msg.getAllRecipients());
            transportTLS.close();
        } catch (Exception ex) {
            Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}