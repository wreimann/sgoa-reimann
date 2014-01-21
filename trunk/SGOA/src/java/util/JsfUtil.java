package util;

import facede.ConfigEmailFacade;
import java.io.PrintWriter;
import java.io.StringWriter;
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
import javax.mail.PasswordAuthentication;
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
        addErrorMessage(defaultMsg);
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            addErrorMessage(sw.toString());
        }
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
            Properties props = new Properties();
            ConfigEmailFacade ebjEmail = new ConfigEmailFacade();
            final ConfigEmail config = ebjEmail.obterPorId(sessao, 1);
            props.put("mail.smtp.host", config.getServidorSMTP());
            props.put("mail.smtp.port", config.getPorta().toString());
            props.put("mail.smtp.auth", "true");
            javax.mail.Session session = javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(config.getEmailEnvio(), config.getSenha());
                }
            });
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(config.getEmailEnvio(), config.getIdentificacaoEmail()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(pessoa.getEmail(), pessoa.getNome()));
            message.setSubject(assuntoEmail);
            message.setText(mensagem);
            Transport.send(message);
        } catch (Exception ex) {
            Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void enviarEmailOficina(Session sessao, String placa, String mensagem) {
        try {
            Properties props = new Properties();
            ConfigEmailFacade ebjEmail = new ConfigEmailFacade();
            final ConfigEmail config = ebjEmail.obterPorId(sessao, 1);
            props.put("mail.smtp.host", config.getServidorSMTP());
            props.put("mail.smtp.port", config.getPorta().toString());
            props.put("mail.smtp.auth", "true");
            javax.mail.Session session = javax.mail.Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(config.getEmailEnvio(), config.getSenha());
                }
            });
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(config.getEmailEnvio(), config.getIdentificacaoEmail()));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(config.getEmailRecebCliente()));
            msg.setSubject("Interação do cliente no serviço");
            msg.setText(mensagem + "<br/>" + "Mensagem enviado pelo cliente com veículo de placa: " + placa);
            Transport.send(msg);
        } catch (Exception ex) {
            Logger.getLogger(JsfUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}