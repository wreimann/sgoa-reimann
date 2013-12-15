package converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "matriculaConverter")
public class MatriculaConverter implements Converter {

    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2)
            throws ConverterException {
        return arg2;
    }

    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2)
            throws ConverterException {
        if (arg2 == null) {
            return "";
        }

        String ano = arg2.toString().substring(0, 4);
        String sequencial = String.format("%05d", Integer.parseInt(arg2.toString().substring(4)));  
        return ano + "." + sequencial;

    }
}
