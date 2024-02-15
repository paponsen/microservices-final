package com.programmingtechi.orderservice.service;

import com.programmingtechi.orderservice.dto.InventoryResponse;
import com.programmingtechi.orderservice.dto.OrderLineItemsDto;
import com.programmingtechi.orderservice.dto.OrderRequest;
import com.programmingtechi.orderservice.event.OrderPlacedEvent;
import com.programmingtechi.orderservice.model.Order;
import com.programmingtechi.orderservice.model.OrderLineItems;
import com.programmingtechi.orderservice.repository.OrderRepository;
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
    public String placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream()
                .map(orderLineItemsDto -> mapToOrderLineItems(orderLineItemsDto)).toList();

        order.setOrderLineItemsList(orderLineItems);

        //Call the inventory service. And place order if item is in stock
        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(orderItem -> orderItem.getSkuCode()).toList();

        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",uriBuilder ->
                        uriBuilder.queryParam("skuCode",skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)// read the data from webclient response we need to use bodyToMono method and parameter will be the return type of the response
                .block();// synchronous communication

        boolean allProductInStore = Arrays.stream(inventoryResponseArray)
                .allMatch(inventoryResponse -> inventoryResponse.isInStock());

        if(allProductInStore){
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber(), order.getOrderLineItemsList()));
            return  "Order placed successfully";
        } else {
            throw new IllegalArgumentException("Product is not in stock. Please try again later");
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
