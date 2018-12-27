/**
 * 
 */
package com.example.viewmodel;

import org.joda.time.DateTime;


/**
 * @author zero
 * Technologically speaking, we could implement Specification to build dynamic filter
 * For example: 
 * 
 * 		import org.springframework.data.domain.Page;
 *		import org.springframework.data.domain.Pageable;
 *
 * 		public class FlightSearchViewModel implements Specification<Flight> { }
 * 
 * This is to simplify the demo, no any implementation of Pageable & Specification  
 *
 */
public class FlightSearchViewModel {
	
	public String departure;
	
	public String arrival;
	
	public DateTime departureTime;
	
	public DateTime arrivalTime;
}