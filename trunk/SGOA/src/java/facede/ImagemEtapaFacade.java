package facede;

import facede.base.BaseFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.ImagemEtapa;

@Stateless
public class ImagemEtapaFacade extends BaseFacade<ImagemEtapa> {

    @PersistenceContext(unitName = "SGOAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ImagemEtapaFacade() {
        super(ImagemEtapa.class);
    }

}