/**
 * 
 */
package com.example.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.example.Service.*;
import com.example.domain.*;
import com.example.viewmodel.*;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author zero
 *
 */
@RestController
@RequestMapping(value = "/flights")
public class FlightController {
	
//	private static CheapClass[] cheap = null;
//	private static BusinessClass[] business = null;
	
	@Autowired
	private MopdataService service ;
	
	@ApiOperation(value="Use to pull the flight list", notes="Support filtering and sorting")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "type", value = "The class type. values would be one of 'All, Cheap, Business'", required = true, dataType = "String"),
        @ApiImplicitParam(name = "search", value = "The search criteria", required = false, dataType = "FlightSearchViewModel"),
        @ApiImplicitParam(name = "pageable", value = "The pagging info", required = false, dataType = "Pageable")
	})
	@PostMapping(value = "/{type}")
	@ResponseBody
	public Page<Flight> getFlights(@PathVariable String type, @RequestBody FlightSearchViewModel search, Pageable pageable) throws Exception {
		ClassType _type = searchEnum(ClassType.class, type) ;
		Page<Flight> flights =  service.loadMopdata(_type, search, pageable);

		return flights ;
	}
	
	private static <T extends Enum<?>> T searchEnum(Class<T> enumeration, String value) {
		for(T each: enumeration.getEnumConstants()) {
			if(each.name().compareToIgnoreCase(value) == 0) {
				return each;
			}
		}
		return null;
	}
}
