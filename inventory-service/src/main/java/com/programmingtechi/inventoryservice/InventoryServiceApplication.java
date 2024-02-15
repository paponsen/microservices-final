package com.programmingtechi.inventoryservice;

import com.programmingtechi.inventoryservice.model.Inventory;
import com.programmingtechi.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}
	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("Galaxy-S");
			inventory1.setQuantity(10);

			Inventory inventory2 = new Inventory();
			inventory2.setSkuCode("Galaxy-Z");
			inventory2.setQuantity(0);

			Inventory inventory3 = new Inventory();
			inventory2.setSkuCode("Galaxy-A");
			inventory2.setQuantity(100);

			inventoryRepository.save(inventory1);
			inventoryRepository.save(inventory2);
			inventoryRepository.save(inventory3);
		};
	}
}
