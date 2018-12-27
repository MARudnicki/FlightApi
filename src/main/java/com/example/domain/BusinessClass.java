/**
 * 
 */
package com.example.domain;

import com.fasterxml.jackson.annotation.*;

import org.joda.time.DateTime;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.util.StringUtils;

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
public class BusinessClass {
	
	@JsonProperty("uuid")
	public UUID uuid;
	
	@JsonProperty("flight")
	public String flight;
	
	@JsonProperty("departure")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	public DateTime departureTime;
	
	@JsonProperty("arrival")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	public DateTime arrivalTime;
	
	public String getDeparture() {
		if (StringUtils.isEmpty(this.flight))
			return null;
		
		return this.flight.split("->")[0] ;
	}
	
	public String getArrival() {
		if (StringUtils.isEmpty(this.flight))
			return null;
		String[] strArray = this.flight.split("->");
		
		if(strArray.length <= 1)
			return "";
		else 
			return this.flight.split("->")[1] ;
	}
}
