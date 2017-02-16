package com.huoyun.core.bo;

import java.util.List;

public interface BoRepository<T extends BusinessObject> {

	T load(Long id);
	
	void save(T bo);

	void update(T bo);

	void delete(T bo);
	
	void flush();

	List<T> query();
}
