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
import model.Cliente;
import model.Orcamento;
import model.Veiculo;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import util.DateUtils;
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
    
    public List<Orcamento> selecionarPorParametros(Session sessao, String sort, SortOrder order,
            Integer page, Integer maxPage, String numero, String situacao, Cliente cliente,
            String placa, Date dataInicial, Date dataFinal) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        //  busca o total de registro que atendam o filtro da pesquisa
        Criteria c = sessao.createCriteria(Orcamento.class, "orc");
        if (numero != null && !numero.isEmpty()) {
            c.add(Restrictions.eq("ano", Integer.parseInt(numero.substring(0, 4))));
            c.add(Restrictions.eq("numero", Integer.parseInt(numero.substring(5))));
        }
        if (situacao != null && !situacao.isEmpty()) {
            c.add(Restrictions.eq("situacao", situacao.charAt(0)));
        }
        if (cliente != null) {
            c.add(Restrictions.eq("cliente", cliente));
        }
        if (placa != null && !placa.isEmpty()) {
            c.createCriteria("cliente", "cli").createCriteria("pessoa", "pes");
            DetachedCriteria veiculosCriteria = DetachedCriteria.forClass(Veiculo.class, "veiculos");
            veiculosCriteria.add(Restrictions.like("veiculos.placa", placa, MatchMode.EXACT).ignoreCase());
            veiculosCriteria.add(Restrictions.eqProperty("veiculos.pessoa.id", "pes.id"));
            c.add(Subqueries.exists(veiculosCriteria.setProjection(Projections.property("veiculos.id"))));
        }
        if (dataInicial != null) {
            c.add(Restrictions.ge("dataCadastro", DateUtils.lowDateTime(dataInicial)));
        }
        if (dataFinal != null) {
            c.add(Restrictions.le("dataCadastro", DateUtils.highDateTime(dataFinal)));
        }
        super.setRowCount((Long) c.setProjection(Projections.rowCount()).uniqueResult());
        c = sessao.createCriteria(Orcamento.class, "orc");
        c.setFirstResult(page).setMaxResults(maxPage);
        if (sort != null) {
            if (order.equals(SortOrder.ASCENDING)) {
                c.addOrder(Order.asc(sort));
                if (sort.equals("ano")) {
                    c.addOrder(Order.asc("numero"));
                }
            } else {
                c.addOrder(Order.desc(sort));
                if (sort.equals("ano")) {
                    c.addOrder(Order.asc("numero"));
                }
            }
        }
        if (numero != null && !numero.isEmpty()) {
            c.add(Restrictions.eq("ano", Integer.parseInt(numero.substring(0, 4))));
            c.add(Restrictions.eq("numero", Integer.parseInt(numero.substring(5))));
        }
        if (situacao != null && !situacao.isEmpty()) {
            c.add(Restrictions.eq("situacao", situacao.charAt(0)));
        }
        if (cliente != null) {
            c.add(Restrictions.eq("cliente", cliente));
        }
        if (placa != null && !placa.isEmpty()) {
            c.createCriteria("cliente", "cli").createCriteria("pessoa", "pes");
            DetachedCriteria veiculosCriteria = DetachedCriteria.forClass(Veiculo.class, "veiculos");
            veiculosCriteria.add(Restrictions.like("veiculos.placa", placa, MatchMode.EXACT).ignoreCase());
            veiculosCriteria.add(Restrictions.eqProperty("veiculos.pessoa.id", "pes.id"));
            c.add(Subqueries.exists(veiculosCriteria.setProjection(Projections.property("veiculos.id"))));
        }
        if (dataInicial != null) {
            c.add(Restrictions.ge("dataCadastro", DateUtils.lowDateTime(dataInicial)));
        }
        if (dataFinal != null) {
            c.add(Restrictions.le("dataCadastro", DateUtils.highDateTime(dataFinal)));
        }
        List<Orcamento> lista = c.list();
        for (Orcamento orcamento : lista) {
            Hibernate.initialize(orcamento.getAnexos());
            Hibernate.initialize(orcamento.getServicos());
        }
        return lista;
    }
}
