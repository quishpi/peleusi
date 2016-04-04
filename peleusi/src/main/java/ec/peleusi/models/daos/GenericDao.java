package ec.peleusi.models.daos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import ec.peleusi.utils.HibernateUtil;

public abstract class GenericDao<T, ID extends Serializable> {

	private Class<T> persistentClass;

	public GenericDao(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	public void create(T entity) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.save(entity);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			if (session.getTransaction() != null)
				session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	public T read(ID id) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		T entity = null;
		try {
			session.beginTransaction();
			entity = (T) session.get(persistentClass, id);
			Hibernate.initialize(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return entity;
	}

	public void update(T entity) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			session.update(entity);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			if (session.getTransaction() != null)
				session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	public boolean deleteById(ID id) {
		boolean deleted = false;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			T entity = (T) session.load(persistentClass, id);
			session.delete(entity);
			session.getTransaction().commit();
			deleted = true;
		} catch (HibernateException e) {
			if (session.getTransaction() != null)
				session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return deleted;
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List<T> list = new ArrayList<T>();
		try {
			session.beginTransaction();
			list = session.createCriteria(persistentClass).list();
			Hibernate.initialize(list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return list;
	}

	public void query(String hql) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery(hql);
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}
}
