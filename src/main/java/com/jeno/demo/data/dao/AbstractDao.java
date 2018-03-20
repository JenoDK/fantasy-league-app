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
		validateAdd(object, errorMap);
		preAdd(object, errorMap);
		if (errorMap.isEmpty()) {
			return repository.saveAndFlush(object);
		} else {
			throw new ValidationException(errorMap);
		}
	}

	public T update(T object) {
		Map<String, String> errorMap = Maps.newHashMap();
		validateUpdate(object, errorMap);
		preUpdate(object, errorMap);
		if (errorMap.isEmpty()) {
			return repository.saveAndFlush(object);
		} else {
			throw new ValidationException(errorMap);
		}
	}

	protected void validateAdd(T object, Map<String, String> errorMap) {
	}

	protected void preAdd(T object, Map<String, String> errorMap) {
	}

	protected void validateUpdate(T object, Map<String, String> errorMap) {
	}

	protected void preUpdate(T object, Map<String, String> errorMap) {
	}

}
