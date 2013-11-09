package facede;

import facede.base.BaseFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.ConfigEmail;

@Stateless
public class ConfigEmailFacade extends BaseFacade<ConfigEmail> {

    @PersistenceContext(unitName = "SGOAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfigEmailFacade() {
        super(ConfigEmail.class);
    }
}