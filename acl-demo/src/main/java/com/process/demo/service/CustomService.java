package com.process.demo.service;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import com.process.demo.model.Ropa;

/**
 * A generic service for handling CRUD operations.
 * <p>
 * The method access-control expressions are specified in this interface.
 */
public interface CustomService {

//	@PostAuthorize("hasPermission(returnObject, 'WRITE')")
//	public Ropa getSingle(Long id);
//
//	@PostFilter("hasPermission(filterObject, 'READ')")
//	public List<Ropa> getAll();
//
//	public Boolean add(Ropa ropa);
//
//	@PreAuthorize("hasPermission(#ropa, 'WRITE')")
//	public Boolean edit(Ropa ropa);

	@PreAuthorize("hasPermission(#ropa, 'DELETE') "
			+ "or hasPermission(#ropa, 'ADMINISTRATION') ")
	public Boolean delete(Ropa ropa);

}