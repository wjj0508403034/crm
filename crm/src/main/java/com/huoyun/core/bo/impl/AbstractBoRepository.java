package com.huoyun.core.bo.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.Assert;

import com.huoyun.core.bo.BoRepository;
import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.BoMeta;

public abstract class AbstractBoRepository<T extends BusinessObject> implements
		BoRepository<T> {

	protected final BusinessObjectFacade boFacade;
	protected final Class<T> boType;
	protected final BoMeta boMeta;

	// protected final JpaContext jpaContext;

	public AbstractBoRepository(Class<T> boType, BusinessObjectFacade boFacade,
			BoMeta boMeta) {
		this.boFacade = boFacade;
		this.boType = boType;
		this.boMeta = boMeta;
	}

	@Override
	public T load(Long id) {
		T bo = this.boFacade.getEntityManager().find(this.boType, id);
		if (bo != null) {
			bo.setBoFacade(this.boFacade);
		}
		return bo;
	}

	@Override
	public void save(T bo) {
		this.boFacade.getEntityManager().persist(bo);
	}

	@Override
	public void update(T bo) {
		this.boFacade.getEntityManager().merge(bo);
	}

	@Override
	public void delete(T bo) {
		this.boFacade.getEntityManager().remove(bo);
	}

	@Override
	public void flush() {
		this.boFacade.getEntityManager().flush();
	}

	@Override
	public List<T> query(Specification<T> spec) {
		CriteriaBuilder builder = this.boFacade.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(this.boType);

		Root<T> root = applySpecificationToCriteria(spec, criteriaQuery);
		criteriaQuery.select(root);
		
		TypedQuery<T> query =  this.boFacade.getEntityManager().createQuery(criteriaQuery);
//		
//		TypedQuery<T> query = this.boFacade.getEntityManager().createQuery(
//				"select t from Contact t", this.boType);
		query.setMaxResults(10);

		List<T> list = query.getResultList();
		for (T bo : list) {
			bo.setBoFacade(this.boFacade);
		}

		return list;
	}
	
	@Override
	public Page<T> pageableQuery(Pageable pageable){

		
		return null;
	}
	
	private <S> Root<T> applySpecificationToCriteria(Specification<T> spec, CriteriaQuery<S> query) {

		Assert.notNull(query);
		Root<T> root = query.from(this.boType);

		if (spec == null) {
			return root;
		}

		CriteriaBuilder builder = this.boFacade.getEntityManager().getCriteriaBuilder();
		Predicate predicate = spec.toPredicate(root, query, builder);

		if (predicate != null) {
			query.where(predicate);
		}

		return root;
	}

	@Override
	public Long count(Specification<T> spec) {


//		if (sort != null) {
//			query.orderBy(toOrders(sort, root, builder));
//		}

		
//		JpaSpecificationExecutor 
//		Specification<T> spec = new Specification<T>(){
//			 @Override
//			   public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//				   return null;
//			   }
//		};

		TypedQuery<Long> query = this.boFacade.getEntityManager().createQuery(
				"select count(t) from Contact t", Long.class);
		return query.getSingleResult();
	}
}
