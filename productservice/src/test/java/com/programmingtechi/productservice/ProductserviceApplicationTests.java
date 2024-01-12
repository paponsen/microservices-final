package com.programmingtechi.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.transport.DockerHttpClient;
import com.programmingtechi.productservice.dto.ProductRequest;
import com.programmingtechi.productservice.dto.ProductResponse;
import com.programmingtechi.productservice.model.Product;
import com.programmingtechi.productservice.repository.ProductRepository;
import com.programmingtechi.productservice.service.ProductService;
import org.apache.catalina.LifecycleState;
import org.apache.coyote.Response;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ProductserviceApplicationTests {
	/**
	 * download mongo:4.4.2 image
	 * Docker containers provide isolation, ensuring that your tests run in a clean environment.
	 * Each test starts with fresh MongoDB instance.
	 * provides more reliable and more reproducable environment.
	 */
	/*
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	MockMvc mockMvc;

	@Autowired
	ProductRepository productRepository;


	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add(
				"spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);

	}
	@Test
	void shouldCreateProduct() throws  Exception{
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		System.out.println("Product request string: "+productRequestString);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString))
				.andExpect(MockMvcResultMatchers.status().isCreated());
		Assertions.assertEquals(1, productRepository.findAll().size());

	}
	private ProductRequest getProductRequest(){
		return ProductRequest.builder()
				.name("iPhone 13")
				.description("apple")
				.price(BigDecimal.valueOf(1400)).build();
	}

*/
}
