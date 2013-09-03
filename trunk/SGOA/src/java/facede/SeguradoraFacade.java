package facede;

import facede.base.BaseFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Seguradora;

@Stateless
public class SeguradoraFacade extends BaseFacade<Seguradora> {

    @PersistenceContext(unitName = "SGOAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SeguradoraFacade() {
        super(Seguradora.class);
    }

}