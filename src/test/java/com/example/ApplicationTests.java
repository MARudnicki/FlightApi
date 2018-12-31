package com.example;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.Service.MopdataService;
import com.example.controller.*;
import com.example.domain.*;
import com.example.viewmodel.FlightSearchViewModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTests {
	
	private MockMvc mvc ;
	
	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.standaloneSetup(new FlightController()).build() ;
	}
	
	@Autowired
	private Environment env;

	@Test
	public void loadConfigUrls() throws Exception {
		Binder binder = Binder.get(env);
		List<String> urls = binder.bind("external.url", Bindable.listOf(String.class)).get();

		Assert.assertTrue(urls != null);
		Assert.assertTrue(urls.size() == 2);
	}
	
	@Test
	public void loadMopdata() throws Exception {
		ObjectMapper mapper = new ObjectMapper() ;
		mapper.findAndRegisterModules();
		TypeReference<CheapClass[]> typeReference1 = new TypeReference<CheapClass[]>() {} ;
		InputStream inputStream1 = TypeReference.class.getResourceAsStream("/mopdata/Cheap.json") ;
		try {
			CheapClass[] cheap = mapper.readValue(inputStream1, typeReference1);
			
			Assert.assertTrue(cheap != null);
			Assert.assertTrue(cheap.length >= 1);
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
		
		TypeReference<BusinessClass[]> typeReference2 = new TypeReference<BusinessClass[]>() {} ;
		InputStream inputStream2 = TypeReference.class.getResourceAsStream("/mopdata/Business.json") ;
		try {
			BusinessClass[] business = mapper.readValue(inputStream2, typeReference2);
			
			Assert.assertTrue(business != null);
			Assert.assertTrue(business.length >= 1);
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Value("${mopdata}")
	private Boolean mopdata ;
	
	@Test
	public void getMopdataValue() throws Exception {
		Assert.assertTrue(mopdata);
	}
	
	
	@Autowired
	private MopdataService service ;
	
	@Test
	public void loadHttpCheap() throws Exception {
//		CheapClass[] list = service.loadCheap() ;
//		
//		Assert.assertTrue(list != null && list.length > 0);
	}
	
	@Test
	public void loadHttpBusiness() throws Exception {
//		BusinessClass[] list = service.loadBusiness();
//		
//		Assert.assertTrue(list != null && list.length > 0);
	}
	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	@Test
	public void testGetFlights() throws Exception {
		
		FlightSearchViewModel model = new FlightSearchViewModel();
		model.arrival = "Jeppener"; //try to search 
		
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson=ow.writeValueAsString(model);
	    
		//first --- get all flights
		mvc.perform(post("/flights/all").contentType(APPLICATION_JSON_UTF8).content(requestJson)) 
				.andExpect(status().isOk()) ;
				//.andExpect(content().string(equalTo("[]"))); 
	}

}

