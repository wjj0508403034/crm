package com.huoyun.core.bo.impl;

import java.util.List;

import javax.persistence.TypedQuery;

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
	public List<T> query() {
		TypedQuery<T> query = this.boFacade.getEntityManager().createQuery("select t from Contact t", this.boType);
		query.setMaxResults(10);
		return query.getResultList();
	}
}
