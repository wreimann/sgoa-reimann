package facede;

import facede.base.BaseFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.model.SortOrder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import java.util.List;
import model.Marca;
import model.Modelo;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

@Stateless
public class ModeloFacade extends BaseFacade<Modelo> {

    @PersistenceContext(unitName = "SGOAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ModeloFacade() {
        super(Modelo.class);
    }

    public List<Modelo> selecionarPorParametros(Session sessao, String sort, SortOrder order, Integer page, Integer maxPage, String descFiltro, Marca marca) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        //  busca o total de registro que atendam o filtro da pesquisa
        Criteria c = sessao.createCriteria(Modelo.class);
        if (descFiltro != null && !descFiltro.isEmpty()) {
            c.add(Restrictions.like("descricao", descFiltro, MatchMode.ANYWHERE).ignoreCase());
        }
        if(marca != null){
            c.add(Restrictions.eq("marca", marca));
        }
        super.setRowCount((Long)c.setProjection(Projections.rowCount()).uniqueResult());
        // realizar a pesquisa por demanda
        c = sessao.createCriteria(Modelo.class);
        c.setProjection(null).setResultTransformer(Criteria.ROOT_ENTITY);
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
        if(marca != null){
            c.add(Restrictions.eq("marca", marca));
        }
        List<Modelo> lista = c.list();
        return lista;
    }
    
      public List<Modelo> selecionarAtivosPorMarca(Session sessao, Marca marca) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        if (marca != null) {
            Criteria c = sessao.createCriteria(Modelo.class);
            c.add(Restrictions.eq("marca", marca));
            c.addOrder(Order.asc("descricao"));
            List<Modelo> lista = c.list();
            return lista;
        } else {
            return null;
        }
    }
}