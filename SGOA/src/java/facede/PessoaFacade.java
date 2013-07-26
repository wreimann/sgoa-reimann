package facede;

import facede.base.BaseFacade;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Criteria;
import java.util.List;
import model.Pessoa;
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

    public List<Pessoa> selecionarPorNumeroDocumento(Session sessao, boolean pessoaFisica, String numero) throws Exception {
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
        List<Pessoa> lista = c.list();
        return lista;
    }
}