package com.programmingtechi.orderservice.controller;

import com.programmingtechi.orderservice.dto.OrderRequest;
import com.programmingtechi.orderservice.model.Order;
import com.programmingtechi.orderservice.service.OrderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        orderService.placeOrder(orderRequest);
        return "Order placed successfully";
    }
    @GetMapping
    public List<Order> getAllOrder(){
        return orderService.getAllOrder();
    }
}
