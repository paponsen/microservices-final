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
    @CircuitBreaker(name="inventory", fallbackMethod = "fallBackMethod")
    @TimeLimiter(name="inventory")
    @Retry(name = "inventory")
    /**
     * Whatever call inside the placeOrder method it will apply the circuit breaker pattern
     * we also need to write a fallback logic when circuit breaker fail
     */
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){
        //Now placeOrder method will be executed in separate thread
        return CompletableFuture.supplyAsync(()->orderService.placeOrder(orderRequest));
    }
    @GetMapping
    public List<Order> getAllOrder(){
        return orderService.getAllOrder();
    }

    public CompletableFuture<String> fallBackMethod(OrderRequest orderRequest, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(()->"Oops! Something went wrong, please wait after some times");
    }
}
