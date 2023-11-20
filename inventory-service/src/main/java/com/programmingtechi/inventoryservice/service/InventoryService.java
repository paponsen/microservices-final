package com.programmingtechi.inventoryservice.service;

import com.programmingtechi.inventoryservice.dto.InventoryResponse;
import com.programmingtechi.inventoryservice.model.Inventory;
import com.programmingtechi.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode){
        return inventoryRepository.findBySkuCode(skuCode).isPresent();
    }

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> listSkuCode){
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
