package facede;

import facede.base.BaseFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.model.SortOrder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import java.util.List;
import model.Etapa;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

@Stateless
public class EtapaFacade extends BaseFacade<Etapa> {

    @PersistenceContext(unitName = "SGOAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EtapaFacade() {
        super(Etapa.class);
    }

    public List<Etapa> selecionarPorDescricao(Session sessao, String sort, SortOrder order, Integer page, Integer maxPage, String descFiltro) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        //  busca o total de registro que atendam o filtro da pesquisa
        Criteria c = sessao.createCriteria(Etapa.class);
        if (descFiltro != null && !descFiltro.isEmpty()) {
            c.add(Restrictions.like("descricao", descFiltro, MatchMode.ANYWHERE).ignoreCase());
        }
        super.setRowCount((Long) c.setProjection(Projections.rowCount()).uniqueResult());
        // realizar a pesquisa por demanda
        c = sessao.createCriteria(Etapa.class);
        c.setFirstResult(page).setMaxResults(maxPage);
        if (sort != null) {
            if (order.equals(SortOrder.ASCENDING)) {
                c.addOrder(Order.asc(sort));
            } else {
                c.addOrder(Order.desc(sort));
            }
        }
        if (descFiltro != null && !descFiltro.isEmpty()) {
            c.add(Restrictions.like("descricao", descFiltro, MatchMode.ANYWHERE).ignoreCase());
        }
        List<Etapa> lista = c.list();
        return lista;
    }

}