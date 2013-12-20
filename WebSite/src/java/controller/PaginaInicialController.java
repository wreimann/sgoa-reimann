package controller;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;

@ManagedBean
@ViewScoped
public class PaginaInicialController implements Serializable {

    private String placa;
    private String documento;

    public PaginaInicialController() {
    }

    public void acompanhar() throws IOException {
        if (placa.isEmpty() || documento.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Paramêtros inválidos. Informe a placa e o documento.", ""));
        } else {

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect("http://reimanscar.com.br/SGOA/faces/acompanharservico.xhtml?placa=" + placa + "&doc=" + documento);
        }
    }

    /**
     * @return the placa
     */
    public String getPlaca() {
        return placa;
    }

    /**
     * @param placa the placa to set
     */
    public void setPlaca(String placa) {
        this.placa = placa;
    }

    /**
     * @return the documento
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * @param documento the documento to set
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }
}
