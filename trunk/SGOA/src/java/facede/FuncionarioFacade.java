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
import javax.persistence.EntityNotFoundException;
import model.Funcionario;
import model.Pessoa;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;
import util.CriptografiaUtil;
import util.JsfUtil;

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
        validarDocumento(sessao, item.getPessoa().getCpf());
        validarEmail(sessao, item);
        item.setMatricula(obterMatricula(sessao));
        boolean enviarEmail = incluirAcessoAoSitema(item);
        super.incluir(sessao, item);
        if (enviarEmail) {
            enviarEmail(sessao, item.getPessoa(), item.getMatricula().toString());
        }
    }

    @Override
    public void alterar(Session sessao, Funcionario item) throws Exception {
        validarEmail(sessao, item);
        boolean enviarEmail = incluirAcessoAoSitema(item);
        super.alterar(sessao, item);
        if (enviarEmail) {
            enviarEmail(sessao, item.getPessoa(), item.getMatricula().toString());
        }
    }

    private boolean incluirAcessoAoSitema(Funcionario item) throws Exception {
        if (item == null) {
            throw new EntityNotFoundException("Objeto nulo.");
        }
        if (item.getSenha() == null && item.getPerfilAcesso() != null) {
            item.setSenha(CriptografiaUtil.encrypt(item.getMatricula().toString()));
            return true;
        } else {
            if (item.getPerfilAcesso() == null) {
                item.setSenha(null);
            }
            return false;
        }
    }

    private void enviarEmail(Session sessao, Pessoa pessoa, String senha) {
        if (pessoa != null && !pessoa.getEmail().isEmpty()) {
            JsfUtil.enviarEmail(sessao, pessoa, "Reiman´s Car - Senha de autenticação",
                    "Para acessar o Sistema Gerenciador de Oficinas Automotivas informe:"
                    + "E-mail: " + pessoa.getEmail()
                    + "Senha: " + senha.replaceAll("\\.", ""));
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

    @Override
    public List<Funcionario> selecionarTodosAtivos(Session sessao) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        Criteria c = sessao.createCriteria(Funcionario.class, "func").createCriteria("pessoa", "pes");
        c.add(Restrictions.eq("func.ativo", true));
        c.addOrder(Order.asc("pes.nome"));
        List<Funcionario> lista = c.list();
        return lista;
    }

    public Funcionario login(Session sessao, String email, String senha) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        Funcionario retorno = null;
        if (!email.isEmpty() && !senha.isEmpty()) {
            Criteria c = sessao.createCriteria(Funcionario.class, "func").createCriteria("pessoa", "pes");
            c.add(Restrictions.eq("pes.email", email));
            c.add(Restrictions.eq("func.senha", senha));
            retorno = (Funcionario) c.uniqueResult();
        }
        return retorno;
    }

    public void recuperarSenha(Session sessao, String email) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        PessoaFacade pessoaFacade = new PessoaFacade();
        Pessoa pessoa = pessoaFacade.obterPorEmail(sessao, email);
        if (pessoa != null) {
            Funcionario retorno = obterPorPessoa(sessao, pessoa.getId());
            if (retorno != null) {
                if (retorno.getPerfilAcesso() != null && retorno.getAtivo()) {
                    retorno.setSenha(CriptografiaUtil.encrypt(retorno.getMatricula().toString()));
                    alterar(sessao, retorno);
                    JsfUtil.enviarEmail(sessao, retorno.getPessoa(), "Reiman´s Car - Recuperação de senha de autenticação",
                            "Para acessar o Sistema Gerenciador de Oficinas Automotivas informe:"
                            + "E-mail: " + retorno.getPessoa().getEmail()
                            + "Senha: " + retorno.getMatricula().toString().replaceAll("\\.", ""));
                } else {
                    throw new Exception("Funcionário sem permissão ao sistema.");
                }
            } else {
                throw new Exception("E-mail não localizado.");
            }
        } else {
            throw new Exception("E-mail não localizado.");
        }
    }

    private Integer obterMatricula(Session sessao) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        Integer anoCadastro = cal.get(Calendar.YEAR);
        Criteria c = sessao.createCriteria(Funcionario.class);
        c.add(Restrictions.sqlRestriction("YEAR(dataCadastro) = ?", anoCadastro, IntegerType.INSTANCE));
        Long quantidade = ((Long) c.setProjection(Projections.count("id")).uniqueResult());
        if (quantidade == null) {
            quantidade = Long.parseLong(anoCadastro.toString() + "1");
        } else {
            quantidade++;
            quantidade = Long.parseLong(anoCadastro.toString() + quantidade.toString());
        }
        return (int) (long) quantidade;
    }

    private void validarEmail(Session sessao, Funcionario item) throws Exception {
        if (item == null) {
            throw new EntityNotFoundException("Objeto nulo.");
        }
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        PessoaFacade pessoaFacade = new PessoaFacade();
        pessoaFacade.validarEmail(sessao, item.getPessoa().getEmail(), item.getPessoa().getId());
    }

    private void validarDocumento(Session sessao, String numeroCPF) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        PessoaFacade pessoaFacade = new PessoaFacade();
        pessoaFacade.validarDocumento(sessao, numeroCPF, true);
    }

    public Funcionario obterPorPessoa(Session sessao, int idPessoa) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        Funcionario retorno = null;
        if (idPessoa > 0) {
            Criteria c = sessao.createCriteria(Funcionario.class, "func").createCriteria("pessoa", "pes");
            c.add(Restrictions.eq("pes.id", idPessoa));
            retorno = (Funcionario) c.uniqueResult();
        }
        return retorno;
    }
}