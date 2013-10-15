package facede;

import facede.base.BaseFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.OrdemServico;

@Stateless
public class OrdemServicoFacade extends BaseFacade<OrdemServico> {
    
    @PersistenceContext(unitName = "SGOAPU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public OrdemServicoFacade() {
        super(OrdemServico.class);
    }
}
