package com.programmingtechi.inventoryservice.controller;

import com.programmingtechi.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/inventory")
@RestController
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    @GetMapping("/{sku-code}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStocke(@PathVariable("sku-code") String skuCOde){
        return  inventoryService.isInStock(skuCOde);
    }
}
