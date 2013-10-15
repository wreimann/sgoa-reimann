package facede;

import facede.base.BaseFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.model.SortOrder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import model.Funcionario;
import model.PessoaFisica;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

@Stateless
public class FuncionarioFacade extends BaseFacade<Funcionario> {

    @PersistenceContext(unitName = "SGOAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FuncionarioFacade() {
        super(Funcionario.class);
    }

    @Override
    public void incluir(Session sessao, Funcionario item) throws Exception {
        validarDocumento(sessao, item);
        super.incluir(sessao, item);
    }

    @Override
    public void alterar(Session sessao, Funcionario item) throws Exception {
        validarDocumento(sessao, item);
        super.alterar(sessao, item);
    }

    private void validarDocumento(Session sessao, Funcionario item) throws Exception {
        if (item == null) {
            throw new EntityNotFoundException("Objeto nulo.");
        }
        if (!util.Comum.isValidoCPF(item.getPessoa().getCpf())) {
            throw new Exception("CPF inválido.");
        }
        if (item.getId() == null || item.getId() == 0) {
            List<Funcionario> funcionarios = selecionarPorNumeroDocumento(sessao, item.getPessoa().getCpf());
            if (funcionarios != null && !funcionarios.isEmpty()) {
                throw new Exception("Já existe um funcionário cadastrado com o mesmo número de CPF.");
            }
        }
    }

    public List<Funcionario> selecionarPorParametros(Session sessao, String sort, SortOrder order, Integer page, Integer maxPage, String nomeFiltro) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        //  busca o total de registro que atendam o filtro da pesquisa
        Criteria c = sessao.createCriteria(Funcionario.class, "fun");
        if (nomeFiltro != null && !nomeFiltro.isEmpty()) {
            c.createCriteria("pessoa", "pessoa");
            c.add(Restrictions.like("pessoa.nome", nomeFiltro, MatchMode.ANYWHERE).ignoreCase());
        }
        super.setRowCount((Long) c.setProjection(Projections.rowCount()).uniqueResult());
        c = sessao.createCriteria(Funcionario.class, "fun");
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
        List<Funcionario> lista = c.list();
        return lista;
    }

    public List<Funcionario> selecionarPorNumeroDocumento(Session sessao, String numero) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        Criteria c = sessao.createCriteria(Funcionario.class, "fun");
        DetachedCriteria dc = DetachedCriteria.forClass(PessoaFisica.class, "pf");
        dc.add(Restrictions.eq("cpf", numero));
        dc.add(Restrictions.eqProperty("fun.id", "fun.pessoa.id"));
        c.add(Subqueries.exists(dc.setProjection(Projections.property("pf.id"))));
        List<Funcionario> lista = c.list();
        return lista;
    }

    @Override
    public List<Funcionario> selecionarTodosAtivos(Session sessao) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        Criteria c = sessao.createCriteria(Funcionario.class,"func").createCriteria("pessoa","pes");
        c.add(Restrictions.eq("func.ativo", true));
        c.addOrder(Order.asc("pes.nome"));
        List<Funcionario> lista = c.list();
        return lista;
    }
}