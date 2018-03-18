package com.jeno.demo.data.dao;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public class AbstractDao<T, F extends JpaRepository<T, Long>> {

	@Autowired
	protected F repository;

	public T add(T object) {
		Map<String, String> errorMap = Maps.newHashMap();
		validate(object, errorMap);
		if (errorMap.isEmpty()) {
			preAdd(object);
			return repository.save(object);
		} else {
			throw new ValidationException(errorMap);
		}
	}

	protected Map<String, String> validate(T object, Map<String, String> errorMap) {
		return errorMap;
	}

	protected void preAdd(T object) {
	}

}
