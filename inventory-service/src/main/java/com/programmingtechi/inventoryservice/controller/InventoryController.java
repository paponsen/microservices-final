package com.programmingtechi.inventoryservice.controller;

import com.programmingtechi.inventoryservice.dto.InventoryResponse;
import com.programmingtechi.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/inventory")
@RestController
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // http://localhost:8082/api/inventory/Galaxy-S
    @GetMapping("/{sku-code}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("sku-code") String skuCOde){
        return  inventoryService.isInStock(skuCOde);
    }

    // But we need to check list of items to avoid multiple hit for a single order
    // http://localhost:8082/api/inventory/skuCode=Galaxy-S&skuCode=Galaxy-A
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){
        return inventoryService.isInStock(skuCode);
    }
}
