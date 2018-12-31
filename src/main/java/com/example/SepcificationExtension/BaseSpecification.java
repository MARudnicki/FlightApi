/**
 * 
 */
package com.example.SepcificationExtension;

import org.springframework.data.jpa.domain.Specification;

/**
 * @author zero
 *
 */
public abstract class BaseSpecification<T, U> {
	
	public abstract Specification<T> getFilter(U request) ;
	
}


