package converter;

import facede.OrcamentoFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.Orcamento;
import org.hibernate.Session;
import util.HibernateFactory;

@FacesConverter(value = "orcamentoConverter", forClass = Orcamento.class)
public class OrcamentoConverter implements Converter {

    @Override
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
            OrcamentoFacade ebjOrcamento = new OrcamentoFacade();
            return ebjOrcamento.obterPorId(sessao, getKey(value));
        } catch (Exception ex) {
            Logger.getLogger(OrcamentoConverter.class.getName()).log(Level.SEVERE, null, ex);
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
        StringBuilder sb = new StringBuilder();
        sb.append(value);
        return sb.toString();
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
        if (object == null || object == "") {
            return null;
        }
        if (object instanceof Orcamento) {
            Orcamento o = (Orcamento) object;
            return getStringKey(o.getId());
        } else {
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName());
        }
    }
}
