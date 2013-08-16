package facede;

import facede.base.BaseFacade;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Pessoa;
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
        validarDocumento(sessao, item);
        super.incluir(sessao, item);
    }

    @Override
    public void alterar(Session sessao, Cliente item) throws Exception {
        validarDocumento(sessao, item);
        super.alterar(sessao, item);
    }

    private void validarDocumento(Session sessao, Cliente item) throws Exception {
        if (item == null) {
            throw new EntityNotFoundException("Objeto nulo.");
        }
        if (item.getPessoa() instanceof PessoaFisica) {
            if (!util.Comum.isValidoCPF(((PessoaFisica) item.getPessoa()).getCpf())) {
                throw new Exception("CPF inválido.");
            }
            if (item.getId() == null || item.getId() == 0) {
                List<Cliente> clientes = selecionarPorNumeroDocumento(sessao, true, ((PessoaFisica) item.getPessoa()).getCpf());
                if (clientes != null && !clientes.isEmpty()) {
                    throw new Exception("Já existe um cliente cadastrado com o mesmo número de CPF.");
                }
            }
        } else {
            if (!util.Comum.isValidoCNPJ(((PessoaJuridica) item.getPessoa()).getCnpj())) {
                throw new Exception("CNPJ inválido.");
            }
            if (item.getId() == null || item.getId() == 0) {
                List<Cliente> clientes = selecionarPorNumeroDocumento(sessao, false, ((PessoaJuridica) item.getPessoa()).getCnpj());
                if (clientes != null && !clientes.isEmpty()) {
                    throw new Exception("Já existe um cliente cadastrado com o mesmo número de CNPJ.");
                }
            }
        }
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
        if (placaFiltro != null && !placaFiltro.isEmpty()) {
            DetachedCriteria veiculosCriteria = DetachedCriteria.forClass(Veiculo.class, "veiculos");
            veiculosCriteria.add(Restrictions.like("veiculos.placa", placaFiltro, MatchMode.EXACT).ignoreCase());
            veiculosCriteria.add(Restrictions.eqProperty("veiculos.pessoa.id", "cli.pessoa.id"));
            c.add(Subqueries.exists(veiculosCriteria.setProjection(Projections.property("veiculos.id"))));
        }
        List<Cliente> lista = c.list();
        return lista;
    }

    public List<Cliente> selecionarPorNumeroDocumento(Session sessao, boolean pessoaFisica, String numero) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        Criteria c = sessao.createCriteria(Cliente.class, "cli");
        if (pessoaFisica) {
            DetachedCriteria dc = DetachedCriteria.forClass(PessoaFisica.class, "pf");
            dc.add(Restrictions.eq("cpf", numero));
            dc.add(Restrictions.eqProperty("pf.id", "cli.pessoa.id"));
            c.add(Subqueries.exists(dc.setProjection(Projections.property("pf.id"))));

        } else {
            DetachedCriteria dc = DetachedCriteria.forClass(PessoaJuridica.class, "pj");
            dc.add(Restrictions.eq("cnpj", numero));
            dc.add(Restrictions.eqProperty("pj.id", "cli.pessoa.id"));
            c.add(Subqueries.exists(dc.setProjection(Projections.property("pj.id"))));
        }
        List<Cliente> lista = c.list();
        return lista;
    }
}