package facede.base;

import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.model.SortOrder;
import util.HibernateFactory;

public abstract class BaseFacade<T> {

    private Class<T> entityClass;
    private long rowCount = 0;

    public BaseFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void incluir(Session sessao, T item) throws Exception {
        try {
            if (sessao == null) {
                throw new Exception("Sessão não iniciada.");
            }
            HibernateFactory.beginTransaction();
            sessao.save(item);
            HibernateFactory.commitTransaction();
        } catch (Exception e) {
            HibernateFactory.rollbackTransaction();
            throw e;
        }
    }

    public void alterar(Session sessao, T item) throws Exception {
        try {
            if (sessao == null) {
                throw new Exception("Sessão não iniciada.");
            }
            HibernateFactory.beginTransaction();
            //HibernateFactory.currentSession().update(item);
            sessao.update(item);
            HibernateFactory.commitTransaction();
        } catch (Exception e) {
            HibernateFactory.rollbackTransaction();
            throw e;
        }
        //HibernateFactory.closeSession();

    }

    public void excluir(Session sessao, T item) throws ConstraintViolationException, Exception {
        try {
            if (sessao == null) {
                throw new Exception("Sessão não iniciada.");
            }
            HibernateFactory.beginTransaction();
            sessao.delete(item);
            HibernateFactory.commitTransaction();
        } catch (ConstraintViolationException c) {
            HibernateFactory.rollbackTransaction();
            throw c;
        } catch (Exception ex) {
            HibernateFactory.rollbackTransaction();
            throw ex;
        }
    }

    public T obterPorId(Session sessao, int id) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        T entidade = (T) sessao.get(entityClass, id);
        return entidade;
    }

    public List<T> selecionarTodos(Session sessao) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        List<T> lista = sessao.createCriteria(entityClass).list();
        return lista;
    }

    public List<T> selecionarTodos(Session sessao, String sort, SortOrder order) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        Criteria c = sessao.createCriteria(entityClass);
        if (sort != null) {
            if (order.equals(SortOrder.DESCENDING)) {
                c.addOrder(Order.desc(sort));
            } else {
                c.addOrder(Order.asc(sort));
            }
        }
        List<T> lista = c.list();
        return lista;
    }

    public List<T> selecionarTodosAtivos(Session sessao) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        Criteria c = sessao.createCriteria(entityClass);
        c.add(Restrictions.eq("ativo", true));
        c.addOrder(Order.asc("descricao"));
        List<T> lista = c.list();
        return lista;
    }

    public List<T> selecionar(Session sessao, String sort, SortOrder order, Integer page, Integer maxPage) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        Criteria c = sessao.createCriteria(entityClass);
        setRowCount((Long) c.setProjection(Projections.rowCount()).uniqueResult());
        c = HibernateFactory.currentSession().createCriteria(entityClass);
        c.setFirstResult(page).setMaxResults(maxPage);
        if (sort != null) {
            if (order.equals(SortOrder.ASCENDING)) {
                c.addOrder(Order.asc(sort));
            } else {
                c.addOrder(Order.desc(sort));
            }
        }
        List<T> lista = c.list();
        return lista;
    }

    public int ObterTotal(Session sessao) throws Exception {
        if (sessao == null) {
            throw new Exception("Sessão não iniciada.");
        }
        Criteria c = sessao.createCriteria(entityClass);
        int count = ((Long) c.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        return count;
    }

    public Long getRowCount() {
        return rowCount;
    }

    public void setRowCount(Long rowCount) {
        if (rowCount == null) {
            this.rowCount = 0;
        } else {
            this.rowCount = rowCount;
        }
    }
}
