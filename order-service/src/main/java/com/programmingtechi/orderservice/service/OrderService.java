package com.programmingtechi.orderservice.service;

import com.programmingtechi.orderservice.dto.OrderLineItemsDto;
import com.programmingtechi.orderservice.dto.OrderRequest;
import com.programmingtechi.orderservice.model.Order;
import com.programmingtechi.orderservice.model.OrderLineItems;
import com.programmingtechi.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream()
                .map(orderLineItemsDto -> mapToOrderLineItems(orderLineItemsDto)).toList();
        order.setOrderLineItemsList(orderLineItems);

        orderRepository.save(order);
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
