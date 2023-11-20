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

    /**
     * This method take a single sku-code and check if it is in inventory.
     * if order consists of so many order items then we cannot use this endpoint
     * because then we need to execute request several times.
     *
     * @param skuCOde
     * @return
     */
    /*
    @GetMapping("/{sku-code}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("sku-code") String skuCOde){
        return  inventoryService.isInStock(skuCOde);
    }
*/
    /**
     * This method get list of all items in a single request.
     * all sku-cde are passed through request the request parameters.
     *
     * @param listSkuCode
     * @return
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam(name = "skuCode")List<String> listSkuCode){
        return inventoryService.isInStock(listSkuCode);

    }

}
