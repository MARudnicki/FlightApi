/**
 * 
 */
package com.example.SepcificationExtension;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.example.viewmodel.*;

import static org.springframework.data.jpa.domain.Specifications.where;
import javax.persistence.criteria.*;


///import java.util.*;
////
////import javax.persistence.criteria.CriteriaBuilder;
////import javax.persistence.criteria.CriteriaQuery;
////import javax.persistence.criteria.Predicate;
////import javax.persistence.criteria.Root;
//
//import org.springframework.data.jpa.domain.Specification;
//
//import net.kaczmarzyk.spring.data.jpa.domain.*;
//import net.kaczmarzyk.spring.data.jpa.web.annotation.*;
//
///**
// * @author zero
// *
// */
//@And({
//    @Spec(path = "departure", spec = Like.class),
//    @Spec(path = "arrival", spec = Like.class),
//    @Spec(path = "departureTime", spec = GreaterThan.class, config="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
//    @Spec(path = "arrivalTime", spec = LessThan.class, config="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//})
@Component
public class FlightSpecification extends BaseSpecification<Flight, FlightSearchViewModel> {

	@Override
	public Specification<Flight> getFilter(FlightSearchViewModel request) {

		return null;
	}

	private Specification<Flight> departureEquals(String departure) {
        return departureAttributeEquals("departure", departure);
    }
	
	private Specification<Flight> departureAttributeEquals(String attribute, String value) {
		
		return (root, query, cb) -> {
			if(value == null || value.isEmpty())
				return null;
			
			return cb.equal(cb.lower(root.get(attribute)), value.toLowerCase() );
		};
	}
}
