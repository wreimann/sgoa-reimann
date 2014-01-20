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
import model.Cliente;
import model.PessoaFisica;
import model.PessoaJuridica;
import model.Veiculo;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

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

    @Override
    public void incluir(Session sessao, Cliente item) throws Exception {
        if (item.getPessoa() instanceof PessoaFisica) {
            validarDocumento(sessao, ((PessoaFisica) item.getPessoa()).getCpf(), true);
        } else {
            validarDocumento(sessao, ((PessoaJuridica) item.getPessoa()).getCnpj(), false);
        }
        validarEmail(sessao, item);
        super.incluir(sessao, item);
    }

    @Override
    public void alterar(Session sessao, Cliente item) throws Exception {
        validarEmail(sessao, item);
        super.alterar(sessao, item);
    }

    private void validarDocumento(Session sessao, String numeroDocumento, boolean pessoaFisica) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        PessoaFacade pessoaFacade = new PessoaFacade();
        pessoaFacade.validarDocumento(sessao, numeroDocumento, pessoaFisica);
    }

    public List<Cliente> selecionarPorParametros(Session sessao, String sort, SortOrder order, Integer page, Integer maxPage, String nomeFiltro, String placaFiltro) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        //  busca o total de registro que atendam o filtro da pesquisa
        Criteria c = sessao.createCriteria(Cliente.class, "cli");
        if (nomeFiltro != null && !nomeFiltro.isEmpty()) {
            c.createCriteria("pessoa", "pessoa");
            c.add(Restrictions.like("pessoa.nome", nomeFiltro, MatchMode.ANYWHERE).ignoreCase());
        }
        if (placaFiltro != null && !placaFiltro.isEmpty()) {
            DetachedCriteria veiculosCriteria = DetachedCriteria.forClass(Veiculo.class, "veiculos");
            veiculosCriteria.add(Restrictions.like("veiculos.placa", placaFiltro, MatchMode.EXACT).ignoreCase());
            veiculosCriteria.add(Restrictions.eqProperty("veiculos.pessoa.id", "cli.pessoa.id"));
            c.add(Subqueries.exists(veiculosCriteria.setProjection(Projections.property("veiculos.id"))));
        }
        super.setRowCount((Long) c.setProjection(Projections.rowCount()).uniqueResult());
        c = sessao.createCriteria(Cliente.class, "cli");
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
        if (placaFiltro != null && !placaFiltro.isEmpty()) {
            DetachedCriteria veiculosCriteria = DetachedCriteria.forClass(Veiculo.class, "veiculos");
            veiculosCriteria.add(Restrictions.like("veiculos.placa", placaFiltro, MatchMode.EXACT).ignoreCase());
            veiculosCriteria.add(Restrictions.eqProperty("veiculos.pessoa.id", "cli.pessoa.id"));
            c.add(Subqueries.exists(veiculosCriteria.setProjection(Projections.property("veiculos.id"))));
        }
        List<Cliente> lista = c.list();
        return lista;
    }

    public List<Cliente> selecionarPorNomeAutoComplete(Session sessao, String query) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        //  busca o total de registro que atendam o filtro da pesquisa
        Criteria c = sessao.createCriteria(Cliente.class, "Cliente");
        if (query != null && !query.isEmpty()) {
            c.createCriteria("pessoa", "pessoa");
            c.add(Restrictions.like("pessoa.nome", query, MatchMode.ANYWHERE).ignoreCase());
        }
        List<Cliente> lista = c.list();
        return lista;
    }

    private void validarEmail(Session sessao, Cliente item) throws Exception {
        if (item == null) {
            throw new EntityNotFoundException("Objeto nulo.");
        }
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        PessoaFacade pessoaFacade = new PessoaFacade();
        pessoaFacade.validarEmail(sessao, item.getPessoa().getEmail(), item.getPessoa().getId());
    }
}