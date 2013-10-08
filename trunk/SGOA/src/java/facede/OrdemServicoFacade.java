package facede;

import facede.base.BaseFacade;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.OrdemServico;
import org.hibernate.Session;
import util.HibernateFactory;

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
    
    @Override
    public void incluir(Session sessao, OrdemServico item) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        try {
            HibernateFactory.beginTransaction();
            Calendar cal = Calendar.getInstance();
            Date date = new Date();
            cal.setTime(date);
            item.setDataAprovacao(cal.getTime());
            item.setSituacao('A'); //Em Aberto 
            sessao.save(item);
            HibernateFactory.currentSession().update(item);
            HibernateFactory.commitTransaction();
        } catch (Exception e) {
            HibernateFactory.rollbackTransaction();
            throw e;
        }
    }
    
    
}
