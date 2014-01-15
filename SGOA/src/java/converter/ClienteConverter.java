package converter;

import facede.ClienteFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.Cliente;
import org.hibernate.Session;
import util.HibernateFactory;

@FacesConverter(value = "clienteConverter", forClass = Cliente.class)
public class ClienteConverter implements Converter {

    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
        if (value == null || value.length() == 0) {
            return null;
        }
        try {
            Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
        try {
            Session sessao = HibernateFactory.currentSession();
            ClienteFacade ebjCliente = new ClienteFacade();
            return ebjCliente.obterPorId(sessao, getKey(value));
        } catch (Exception ex) {
            HibernateFactory.closeSession();
            return null;
        } finally {
            HibernateFactory.closeSession();
        }
    }

    java.lang.Integer getKey(String value) {
        java.lang.Integer key;
        key = Integer.valueOf(value);
        return key;
    }

    String getStringKey(java.lang.Integer value) {
        StringBuffer sb = new StringBuffer();
        sb.append(value);
        return sb.toString();
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null || object == "") {
            return null;
        }
        if (object instanceof Cliente) {
            Cliente o = (Cliente) object;
            return getStringKey(o.getId());
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + ";");
        }
    }
}
