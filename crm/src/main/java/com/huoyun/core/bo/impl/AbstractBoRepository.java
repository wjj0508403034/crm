package com.huoyun.core.bo.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import com.huoyun.core.bo.BoRepository;
import com.huoyun.core.bo.BusinessObject;
import com.huoyun.core.bo.BusinessObjectFacade;
import com.huoyun.core.bo.metadata.BoMeta;
import com.huoyun.core.bo.query.BoSpecification;
import com.huoyun.exception.BusinessException;

public abstract class AbstractBoRepository<T extends BusinessObject> implements
		BoRepository<T> {

	protected final BusinessObjectFacade boFacade;
	protected final Class<T> boType;
	protected final BoMeta boMeta;

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
	public TypedQuery<T> newQuery(String sql) {
		return this.boFacade.getEntityManager().createQuery(sql, this.boType);
	}
	
	
	@Override
	public TypedQuery<Long> newCountQuery(String sql) {
		return this.boFacade.getEntityManager()
				.createQuery(sql, Long.class);
	}
	
	@Override
	public List<T> queryForList() {
		CriteriaBuilder builder = this.boFacade.getEntityManager()
				.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(this.boType);
		Root<T> root = criteriaQuery.from(this.boType);
		criteriaQuery.select(root);
		TypedQuery<T> query = this.boFacade.getEntityManager().createQuery(
				criteriaQuery);
		return query.getResultList();
	}

	@Override
	public Page<T> query(BoSpecification<T> spec, Pageable pageable)
			throws BusinessException {
		CriteriaBuilder builder = this.boFacade.getEntityManager()
				.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(this.boType);

		Root<T> root = applySpecificationToCriteria(spec, criteriaQuery);
		criteriaQuery.select(root);

		TypedQuery<T> query = this.boFacade.getEntityManager().createQuery(
				criteriaQuery);
		Page<T> page = pageable == null ? new PageImpl<T>(query.getResultList())
				: readPage(query, pageable, spec);

		for (T bo : page.getContent()) {
			bo.setBoFacade(this.boFacade);
		}

		return page;
	}

	@Override
	public Long count(BoSpecification<T> spec) throws BusinessException {
		return executeCountQuery(getCountQuery(spec));
	}

	private <S> Root<T> applySpecificationToCriteria(BoSpecification<T> spec,
			CriteriaQuery<S> query) throws BusinessException {

		Assert.notNull(query);
		Root<T> root = query.from(this.boType);

		if (spec == null) {
			return root;
		}

		CriteriaBuilder builder = this.boFacade.getEntityManager()
				.getCriteriaBuilder();
		Predicate predicate = spec.toPredicate(root, query, builder);

		if (predicate != null) {
			query.where(predicate);
		}

		return root;
	}

	private Page<T> readPage(TypedQuery<T> query, Pageable pageable,
			BoSpecification<T> spec) throws BusinessException {

		query.setFirstResult(pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());

		Long total = executeCountQuery(getCountQuery(spec));
		List<T> content = total > pageable.getOffset() ? query.getResultList()
				: Collections.<T> emptyList();

		return new PageImpl<T>(content, pageable, total);
	}

	private TypedQuery<Long> getCountQuery(BoSpecification<T> spec)
			throws BusinessException {

		CriteriaBuilder builder = this.boFacade.getEntityManager()
				.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);

		Root<T> root = applySpecificationToCriteria(spec, query);

		if (query.isDistinct()) {
			query.select(builder.countDistinct(root));
		} else {
			query.select(builder.count(root));
		}

		return this.boFacade.getEntityManager().createQuery(query);
	}

	private static Long executeCountQuery(TypedQuery<Long> query) {

		Assert.notNull(query);

		List<Long> totals = query.getResultList();
		Long total = 0L;

		for (Long element : totals) {
			total += element == null ? 0 : element;
		}

		return total;
	}
}
