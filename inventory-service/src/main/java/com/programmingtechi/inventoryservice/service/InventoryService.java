package com.programmingtechi.inventoryservice.service;

import com.programmingtechi.inventoryservice.dto.InventoryResponse;
import com.programmingtechi.inventoryservice.model.Inventory;
import com.programmingtechi.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode){
        return inventoryRepository.findBySkuCode(skuCode).isPresent();
    }

    //@SneakyThrows   //this annotation is used to consume throws. don't use this in production code
    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> listSkuCode) {
        //below lines are used to slow down inventory service
        //log.info("wait started");
        //Thread.sleep(1000);
        //log.info("wait ended");
         return  inventoryRepository.findBySkuCodeIn(listSkuCode)
                 .stream()
                 .map(inventory ->
                         mapToInventoryResponse(inventory)).toList();

    }

    public InventoryResponse mapToInventoryResponse(Inventory inventory){
        return InventoryResponse.builder()
                .skuCode(inventory.getSkuCode())
                .isInStock(inventory.getQuantity()>0)
                .build();
    }
}
