/**
 * 
 */
package com.example.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.domain.*;
import com.example.viewmodel.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author zero
 *
 */
@Service
public class MopdataService {
	
	private final Logger logger = LogManager.getLogger(MopdataService.class);
	
	@Value("${mopdata}")
	private Boolean mopdata ;
	
	private final List<String> urls ;
	
	@Autowired
	public MopdataService(Environment env) {
		Binder binder = Binder.get(env);
		urls = binder.bind("external.url", Bindable.listOf(String.class)).get();
	}
	
	public CheapClass[] loadJsonCheap() throws Exception {
		ObjectMapper mapper = new ObjectMapper() ;
		mapper.findAndRegisterModules();
		TypeReference<CheapClass[]> typeReference1 = new TypeReference<CheapClass[]>() {} ;
		InputStream inputStream1 = TypeReference.class.getResourceAsStream("/mopdata/Cheap.json") ;
		try {
			return mapper.readValue(inputStream1, typeReference1);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
			return null;
		}
	}
	
	public CheapClass[] loadCheap() throws Exception {
		String url = "";
		for(String u: urls) {
			if(u.toLowerCase().trim().endsWith(ClassType.Cheap.toString().toLowerCase())) {
				url = u;
				break;
			}
		}
		
		if(url.isEmpty()) return null ;
		
		RestTemplate rest = new RestTemplate() ;
		
		try {
			CheapClass[] list = rest.getForObject(url, CheapClass[].class) ;
			return list;
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
			return null ;
		}
	}
	
	public BusinessClass[] loadJsonBusiness() throws Exception {
		ObjectMapper mapper = new ObjectMapper() ;
		mapper.findAndRegisterModules();
		TypeReference<BusinessClass[]> typeReference2 = new TypeReference<BusinessClass[]>() {} ;
		InputStream inputStream2 = TypeReference.class.getResourceAsStream("/mopdata/Business.json") ;
		try {
			return mapper.readValue(inputStream2, typeReference2);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
			return null;
		}
	}
	
	public BusinessClass[] loadBusiness() throws Exception {
		String url = "";
		for(String u: urls) {
			if(u.toLowerCase().trim().endsWith(ClassType.Business.toString().toLowerCase())) {
				url = u;
				break;
			}
		}
		
		if(url.isEmpty()) return null ;
		
		RestTemplate rest = new RestTemplate() ;
		
		try {
			BusinessClass[] list = rest.getForObject(url, BusinessClass[].class) ;
			return list;
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			logger.error(e.getMessage());
			return null ;
		}
	}
	
	public List<Flight> loadMopdata(ClassType type) throws Exception {
		if (type == null) return null;
		
		CheapClass[] cheap = null ;
		BusinessClass[] business = null;
		
		if(this.mopdata) {
			cheap = type == ClassType.Cheap || type == ClassType.All ? loadJsonCheap() : null ;
			business = type == ClassType.Business || type == ClassType.All ? loadJsonBusiness() : null;
		}
		else {
			cheap = type == ClassType.Cheap || type == ClassType.All ? loadCheap() : null;
			business = type == ClassType.Business || type == ClassType.All ? loadBusiness() : null;
		}
		
		List<Flight> flights = new ArrayList<Flight>() ;
		
		if(cheap != null && cheap.length > 0) {
			for(CheapClass c: cheap) {
				Flight f = new Flight();
				f.departure = c.departure ;
				f.arrival = c.arrival;
				f.arrivalTime = c.arrivalTime;
				f.departureTime = c.departureTime;
				f.type = ClassType.Cheap ;
				
				flights.add(f);
			}
		}
		
		if(business != null && business.length > 0) {
			for(BusinessClass b: business) {
				Flight f = new Flight();
				f.departure = b.getDeparture() ;
				f.arrival = b.getArrival();
				f.arrivalTime = b.arrivalTime;
				f.departureTime = b.departureTime;
				f.type = ClassType.Business ;
				
				flights.add(f);
			}
		}
		
		return flights ;
	}
	
	public Page<Flight> loadMopdata(ClassType type, FlightSearchViewModel search, Pageable pageable) throws Exception {
		List<Flight> flights = this.loadMopdata(type) ;
		
		if(search.departure != null && !search.departure.isEmpty())
			flights = flights
						.stream()
						.filter(f -> f.departure.contains(search.departure))
						.sorted((l, r) -> l.departure.compareTo(r.departure))
						.collect(Collectors.toList()) ;
		
		if(search.arrival!=null && !search.arrival.isEmpty())
			flights = flights
						.stream()
						.filter(f -> f.arrival.contains(search.arrival))
						.sorted((l, r) -> l.arrival.compareTo(r.arrival))
						.collect(Collectors.toList()) ;
		
		if(search.departureTime != null)
			flights = flights
						.stream()
						.filter(f -> f.departureTime.compareTo(search.departureTime) >= 0)
						.sorted((l,r) -> l.departureTime.compareTo(r.departureTime))
						.collect(Collectors.toList()) ;
		
		if(search.arrivalTime != null)
			flights = flights
						.stream()
						.filter(f -> f.arrivalTime.compareTo(search.arrivalTime) <= 0)
						.sorted((l,r) -> l.arrivalTime.compareTo(r.arrivalTime))
						.collect(Collectors.toList()) ;
		
		int start = (int) pageable.getOffset();
	    int end = (start + pageable.getPageSize()) > flights.size() ? flights.size() : (start + pageable.getPageSize());
	    
	    Page<Flight> pages = new PageImpl<Flight>(flights.subList(start, end), pageable, flights.size());
		return pages;
	}
	
	static Map<Integer, List<Flight>> partition(List<Flight> list, int pagesize) {
		return null;
	}
}
