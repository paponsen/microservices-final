package com.programmingtechi.inventoryservice.service;

import com.programmingtechi.inventoryservice.dto.InventoryResponse;
import com.programmingtechi.inventoryservice.event.OrderLineItems;
import com.programmingtechi.inventoryservice.event.OrderPlacedEvent;
import com.programmingtechi.inventoryservice.model.Inventory;
import com.programmingtechi.inventoryservice.repository.InventoryRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode){
        return inventoryRepository.findBySkuCode(skuCode).isPresent();
    }

    // Generate slowness manually
    @Transactional(readOnly = true)
   // @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCode){
        //log.info("Start");
        //Thread.sleep(10000);
        //log.info("End");
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory -> InventoryResponse.builder().skuCode(inventory.getSkuCode())
                        .isInStock(inventory.getQuantity()>0).build()).toList();

    }

    public void updateQuantity(Inventory inventory, OrderLineItems orderItem){
        Integer updatedQuantity = inventory.getQuantity() - orderItem.getQuantity();
        System.out.println("Updated quantity for item "+orderItem.getSkuCode()+ " is "+updatedQuantity);
        inventory.setQuantity(updatedQuantity);
        inventoryRepository.save(inventory);
    }
    @KafkaListener(topics = "notificationTopic")
    public void handleKafkaNotification(OrderPlacedEvent orderPlacedEvent){
        log.info("Received notification for order {}", orderPlacedEvent.getOrderNumber());
        orderPlacedEvent.getOrderItems().forEach(orderItem-> {
            inventoryRepository.findBySkuCode(orderItem.getSkuCode())
                    .ifPresent(inventory -> updateQuantity(inventory, orderItem));
        });
    }

}
