package facede;

import facede.base.BaseFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.model.SortOrder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import java.util.List;
import model.Orcamento;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

@Stateless
public class OrcamentoFacade extends BaseFacade<Orcamento> {

    @PersistenceContext(unitName = "SGOAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrcamentoFacade() {
        super(Orcamento.class);
    }

    public List<Orcamento> selecionarPorParametros(Session sessao, String sort, SortOrder order, Integer page, Integer maxPage, String nomeFiltro, String placaFiltro) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        //  busca o total de registro que atendam o filtro da pesquisa
        Criteria c = sessao.createCriteria(Orcamento.class, "orcamento");
        if (nomeFiltro != null && !nomeFiltro.isEmpty()) {
            c.createCriteria("pessoa", "pessoa");
            c.add(Restrictions.like("pessoa.nome", nomeFiltro, MatchMode.ANYWHERE).ignoreCase());
        }
        /*if (placaFiltro != null && !placaFiltro.isEmpty()) {
            DetachedCriteria veiculosCriteria = DetachedCriteria.forClass(Veiculo.class, "veiculos");
            veiculosCriteria.add(Restrictions.like("veiculos.placa", placaFiltro, MatchMode.EXACT).ignoreCase());
            veiculosCriteria.add(Restrictions.eqProperty("veiculos.pessoa.id", "cli.pessoa.id"));
            c.add(Subqueries.exists(veiculosCriteria.setProjection(Projections.property("veiculos.id"))));
        }*/
        super.setRowCount((Long) c.setProjection(Projections.rowCount()).uniqueResult());
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
            c.createCriteria("pessoa", "pessoa");
            c.add(Restrictions.like("pessoa.nome", nomeFiltro, MatchMode.ANYWHERE).ignoreCase());
        }
        /*if (placaFiltro != null && !placaFiltro.isEmpty()) {
            DetachedCriteria veiculosCriteria = DetachedCriteria.forClass(Veiculo.class, "veiculos");
            veiculosCriteria.add(Restrictions.like("veiculos.placa", placaFiltro, MatchMode.EXACT).ignoreCase());
            veiculosCriteria.add(Restrictions.eqProperty("veiculos.pessoa.id", "cli.pessoa.id"));
            c.add(Subqueries.exists(veiculosCriteria.setProjection(Projections.property("veiculos.id"))));
        }*/
        List<Orcamento> lista = c.list();
        return lista;
    }

    
}