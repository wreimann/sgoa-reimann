package facede;

import facede.base.BaseFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.ConfigOrdemServico;

@Stateless
public class ConfigOrdemServicoFacade extends BaseFacade<ConfigOrdemServico> {

    @PersistenceContext(unitName = "SGOAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfigOrdemServicoFacade() {
        super(ConfigOrdemServico.class);
    }

   
}