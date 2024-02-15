package com.programmingtechi.orderservice.controller;

import com.programmingtechi.orderservice.dto.OrderRequest;
import com.programmingtechi.orderservice.model.Order;
import com.programmingtechi.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallBackMethod")
    @TimeLimiter(name="inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){
        //placeOrder method will be executed in other thread.
        return CompletableFuture.supplyAsync(()->orderService.placeOrder(orderRequest));
        //return "Order placed successfully";
    }
    @GetMapping
    public List<Order> getAllOrder(){
        return orderService.getAllOrder();
    }

    public CompletableFuture<String> fallBackMethod(OrderRequest orderRequest, RuntimeException exp){
        return  CompletableFuture.supplyAsync(()->"!!! oops order item is not available. Please try again later");
    }
}
