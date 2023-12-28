package com.programmingtechi.orderservice.service;

//import brave.Span;
//import brave.Tracer;
import com.programmingtechi.orderservice.dto.InventoryResponse;
import com.programmingtechi.orderservice.dto.OrderLineItemsDto;
import com.programmingtechi.orderservice.dto.OrderRequest;
import com.programmingtechi.orderservice.event.OrderPlacedEvent;
import com.programmingtechi.orderservice.model.Order;
import com.programmingtechi.orderservice.model.OrderLineItems;
import com.programmingtechi.orderservice.repository.OrderRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    private final Tracer tracer;
    public String placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream()
                .map(orderLineItemsDto -> mapToOrderLineItems(orderLineItemsDto)).toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> listSkuCode = order.getOrderLineItemsList().stream()
                .map(orderLineItem-> orderLineItem.getSkuCode()).toList();

        System.out.println("size of order items list: "+listSkuCode.size());
        // before take the order check if it is in inventory
        // we do it by using web-client. web-client will call inventory-service to check if the ordered
        // item is available or not
        // webClientBuilder is used for Load balancing
        // trace the request using our own span
        Span inventoryServiceLookup = tracer.nextSpan().name("InventoryServiceLookup");
        try (Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())){
            InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", listSkuCode).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            boolean isInStock = Arrays.stream(inventoryResponseArray).allMatch(inventoryResponse -> inventoryResponse.isInStock());

            if(isInStock){
                orderRepository.save(order);
                kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
                return "Order placed successfully";
            } else{
                System.out.println("Product is not in stock. Please try again later");
                throw new IllegalArgumentException("Product is not in stock. Please try again later");
            }
        } finally {
            inventoryServiceLookup.end();
        }

    }

    private OrderLineItems mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());

        return orderLineItems;

    }

    public List<Order> getAllOrder(){
        List<Order> orderList = orderRepository.findAll();
        return orderList;
    }
}
