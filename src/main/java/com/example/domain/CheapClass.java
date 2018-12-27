/**
 * 
 */
package com.example.domain;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zero
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class CheapClass {
	
	@JsonProperty("id")
	public Long id;
	
	@JsonProperty("departure")
	public String departure;
	
	@JsonProperty("arrival")
	public String arrival;
	
	@JsonProperty("departureTime")
	public DateTime departureTime;
	
	@JsonProperty("arrivalTime")
	public DateTime arrivalTime;
}
