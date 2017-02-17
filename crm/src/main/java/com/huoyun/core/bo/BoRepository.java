package com.huoyun.core.bo;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface BoRepository<T extends BusinessObject> {

	T load(Long id);

	void save(T bo);

	void update(T bo);

	void delete(T bo);

	void flush();

	Long count(Specification<T> spec);

	Page<T> query(Specification<T> spec, Pageable pageable);
	
	TypedQuery<T> newQuery(String sql);
}
