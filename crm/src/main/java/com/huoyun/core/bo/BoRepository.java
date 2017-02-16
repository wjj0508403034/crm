package com.huoyun.core.bo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface BoRepository<T extends BusinessObject> {

	T load(Long id);

	void save(T bo);

	void update(T bo);

	void delete(T bo);

	void flush();

	Page<T> pageableQuery(Pageable pageabel);

	Long count(Specification<T> spec);

	List<T> query(Specification<T> spec);
}
