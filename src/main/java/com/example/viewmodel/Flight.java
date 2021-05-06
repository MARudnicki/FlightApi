
package com.example.viewmodel;

import org.joda.time.DateTime;

import com.example.domain.ClassType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flight {
	
	@JsonProperty("departure")
	public String departure;
	
	@JsonProperty("arrival")
	public String arrival;
	
	@JsonProperty("departureTime")
	public DateTime departureTime;
	
	@JsonProperty("arrivalTime")
	public DateTime arrivalTime;
	
	@JsonProperty("type")
	public ClassType type;
}