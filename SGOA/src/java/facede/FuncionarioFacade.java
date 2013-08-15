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
import model.Funcionario;
import model.PessoaFisica;
import model.PessoaJuridica;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

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
        PessoaFacade pessoaFacade = new PessoaFacade();
        if (!util.Comum.isValidoCPF(item.getPessoa().getCpf())) {
            throw new Exception("CPF inválido.");
        }
        if (item.getId() == null || item.getId() == 0) {
            List<Pessoa> pessoas = pessoaFacade.selecionarPorNumeroDocumento(sessao, true, item.getPessoa().getCpf());
            pessoas.remove(item.getPessoa());
            if (pessoas != null && !pessoas.isEmpty()) {
                throw new Exception("Já existe um cliente cadastrado com o mesmo número de CPF.");
            }
        }
    }

    public List<Funcionario> selecionarPorParametros(Session sessao, String sort, SortOrder order, Integer page, Integer maxPage, String nomeFiltro) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");


        }
        //  busca o total de registro que atendam o filtro da pesquisa
        Criteria c = sessao.createCriteria(Funcionario.class, "cli");

        if (nomeFiltro
                != null && !nomeFiltro.isEmpty()) {
            c.createCriteria("pessoa", "pessoa");
            c.add(Restrictions.like("pessoa.nome", nomeFiltro, MatchMode.ANYWHERE).ignoreCase());
        }


        super.setRowCount(
                (Long) c.setProjection(Projections.rowCount()).uniqueResult());
        c.setProjection(
                null).setResultTransformer(Criteria.ROOT_ENTITY);
        c.setFirstResult(page).setMaxResults(maxPage);

        if (sort
                != null) {
            if (order.equals(SortOrder.ASCENDING)) {
                c.addOrder(Order.asc(sort));
            } else {
                c.addOrder(Order.desc(sort));
            }
        }

        if (nomeFiltro
                != null && !nomeFiltro.isEmpty()) {
            c.createCriteria("pessoa", "pessoa");
            c.add(Restrictions.like("pessoa.nome", nomeFiltro, MatchMode.ANYWHERE).ignoreCase());
        }
        List<Funcionario> lista = c.list();

        return lista;
    }
}