package facede;

import facede.base.BaseFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.model.SortOrder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import java.util.List;
import model.Cliente;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

@Stateless
public class ClienteFacade extends BaseFacade<Cliente> {

    @PersistenceContext(unitName = "SGOAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClienteFacade() {
        super(Cliente.class);
    }

    public List<Cliente> selecionarPorParametros(Session sessao, String sort, SortOrder order, Integer page, Integer maxPage, String nomeFiltro, String placaFiltro) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        //  busca o total de registro que atendam o filtro da pesquisa
        Criteria c = sessao.createCriteria(Cliente.class);
        c.createCriteria("pessoa", "pessoa");
        c.createCriteria("veiculos", "veiculos", JoinType.LEFT_OUTER_JOIN);
        if (nomeFiltro != null && !nomeFiltro.isEmpty()) {
            c.add(Restrictions.like("pessoa.nome", nomeFiltro, MatchMode.ANYWHERE).ignoreCase());
        }
       if (placaFiltro != null && !placaFiltro.isEmpty()) {
            c.add(Restrictions.like("veiculos.placa", placaFiltro, MatchMode.ANYWHERE).ignoreCase());
        }
        super.setRowCount((Long)c.setProjection(Projections.rowCount()).uniqueResult());
        // realizar a pesquisa por demanda
        c = sessao.createCriteria(Cliente.class);
        c.createCriteria("pessoa", "pessoa");
        c.createCriteria("veiculos", "veiculos", JoinType.LEFT_OUTER_JOIN);
        c.setProjection(null).setResultTransformer(Criteria.ROOT_ENTITY);
        c.setFirstResult(page).setMaxResults(maxPage);
        if (sort != null) {
            if (order.equals(SortOrder.ASCENDING)) {
                c.addOrder(Order.asc(sort));
            } else {
                c.addOrder(Order.desc(sort));
            }
        }
        if (nomeFiltro != null && !nomeFiltro.isEmpty()) {
            c.add(Restrictions.like("pessoa.nome", nomeFiltro, MatchMode.ANYWHERE).ignoreCase());
        }
       if (placaFiltro != null && !placaFiltro.isEmpty()) {
            c.add(Restrictions.like("veiculos.placa", placaFiltro, MatchMode.ANYWHERE).ignoreCase());
        }
        List<Cliente> lista = c.list();
        return lista;
    }
    
      
}