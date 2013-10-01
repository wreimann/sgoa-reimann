package facede;

import facede.base.BaseFacade;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.model.SortOrder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import java.util.List;
import model.Orcamento;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import util.HibernateFactory;

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

    @Override
    public void incluir(Session sessao, Orcamento item) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        try {
            HibernateFactory.beginTransaction();
            Calendar cal = Calendar.getInstance();
            Date date = new Date();
            cal.setTime(date);
            int ano = cal.get(Calendar.YEAR);
            item.setDataCadastro(cal.getTime());
            item.setAno(ano);
            item.setSituacao('A'); //Em Aberto 
            sessao.save(item);
            item.setNumero(ObterNumero(sessao, ano));
            HibernateFactory.currentSession().update(item);
            HibernateFactory.commitTransaction();
        } catch (Exception e) {
            HibernateFactory.rollbackTransaction();
            throw e;
        }
    }

    public Integer ObterNumero(Session sessao, int ano) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        Criteria c = sessao.createCriteria(Orcamento.class);
        c.add(Restrictions.eq("ano", ano));
        Integer numero = ((Integer) c.setProjection(Projections.max("numero")).uniqueResult());
        if (numero == null) {
            numero = 1;
        } else {
            numero++;
        }
        return numero;
    }

    public Orcamento ObterPorAnoENumero(Session sessao, int ano, int numero) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        Criteria c = sessao.createCriteria(Orcamento.class);
        c.add(Restrictions.eq("ano", ano));
        c.add(Restrictions.eq("numero", numero));
        Orcamento orcamento = ((Orcamento) c.uniqueResult());
        return orcamento;
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
                if(sort.equals("ano")){
                 c.addOrder(Order.asc("numero"));
                }
            } else {
                c.addOrder(Order.desc(sort));
                if(sort.equals("ano")){
                 c.addOrder(Order.asc("numero"));
                }
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
        for (Orcamento orcamento : lista) {
            Hibernate.initialize(orcamento.getAnexos());
            Hibernate.initialize(orcamento.getServicos());
        }
        return lista;
    }
}