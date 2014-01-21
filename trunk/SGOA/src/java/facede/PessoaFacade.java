package facede;

import facede.base.BaseFacade;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Criteria;
import model.Pessoa;
import model.PessoaEndereco;
import model.PessoaFisica;
import model.PessoaJuridica;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class PessoaFacade extends BaseFacade<Pessoa> {

    @PersistenceContext(unitName = "SGOAPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PessoaFacade() {
        super(Pessoa.class);
    }

    public Pessoa selecionarPorNumeroDocumento(Session sessao, boolean pessoaFisica, String numero) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        Criteria c = null;
        if (pessoaFisica) {
            c = sessao.createCriteria(PessoaFisica.class);
            c.add(Restrictions.eq("cpf", numero));
        } else {
            c = sessao.createCriteria(PessoaJuridica.class);
            c.add(Restrictions.eq("cnpj", numero));
        }
        Pessoa retorno = null;
        try {
            retorno = (Pessoa) c.uniqueResult();
        } catch (Exception e) {
            throw new Exception("Erro ao buscar pessoa por número de documento. \n Mensagem original:" + e.getLocalizedMessage());
        }
        return retorno;

    }

    public Pessoa obterPorEmail(Session sessao, String email) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        Pessoa retorno = null;
        if (!email.isEmpty()) {
            try {
                Criteria c = sessao.createCriteria(Pessoa.class, "pes");
                c.add(Restrictions.eq("pes.email", email));
                retorno = (Pessoa) c.uniqueResult();
            } catch (Exception ex) {
                throw new Exception("E-mail informado já eta sendo utilizado.\n Mensagem Original:" + ex.getLocalizedMessage());
            }
        }
        return retorno;
    }

    public void validarEmail(Session sessao, String email, Integer idPessoa) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        if (email != null && !email.isEmpty()) {
            try {
                Pessoa pessoa = obterPorEmail(sessao, email);
                //valida inclusão
                if (idPessoa == null && pessoa != null) {
                    throw new Exception("E-mail informado já esta sendo utilizado.");
                }
                //valida edicao
                if (pessoa != null && idPessoa != null && pessoa.getId() != idPessoa) {
                    throw new Exception("E-mail informado já esta sendo utilizado.");
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }

    public void validarDocumento(Session sessao, String doc, boolean pessoaFisica, String desc) throws Exception {
        if (doc != null && !doc.isEmpty()) {
            if (pessoaFisica) {
                if (!util.Comum.isValidoCPF(doc)) {
                    throw new Exception("CPF inválido.");
                }
            } else {
                if (!util.Comum.isValidoCNPJ(doc)) {
                    throw new Exception("CNPJ inválido.");
                }
            }
            Pessoa pessoa = selecionarPorNumeroDocumento(sessao, pessoaFisica, doc);
            if (pessoa != null) {
                throw new Exception("Já existe um " + desc + " cadastrada com o número de documento informado.");
            }
        }
    }
    
     public PessoaEndereco obterPorIdEndereco(Session sessao, int id) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        PessoaEndereco entidade = (PessoaEndereco) sessao.get(PessoaEndereco.class, id);
        return entidade;
    }
}