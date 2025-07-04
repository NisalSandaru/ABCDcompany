package com.springboot.order.service;

import com.springboot.inventory.dto.InventoryDTO;
import com.springboot.order.common.ErrorOrderResponse;
import com.springboot.order.common.OrderResponse;
import com.springboot.order.common.SuccessOrderResponse;
import com.springboot.order.dto.OrderDTO;
import com.springboot.order.model.Orders;
import com.springboot.order.repo.OrderRepo;
import com.springboot.product.dto.ProductDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
@Transactional
public class OrderService {
    private final WebClient inventoryWebClient;
    private final WebClient productWebClient;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private ModelMapper modelMapper;

    public OrderService(WebClient inventoryWebClient, WebClient productWebClient, OrderRepo orderRepo, ModelMapper modelMapper) {
        this.inventoryWebClient = inventoryWebClient;
        this.productWebClient = productWebClient;
        this.orderRepo = orderRepo;
        this.modelMapper = modelMapper;
    }

    public List<OrderDTO> getAllOrders() {
        List<Orders> orderList = orderRepo.findAll();
        return  modelMapper.map(orderList, new TypeToken<List<OrderDTO>>(){}.getType());
    }

    public OrderResponse saveOrder(OrderDTO orderDTO) {

        Integer itemId = orderDTO.getItemId();

        try {
            InventoryDTO inventoryResponse = inventoryWebClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/item/{itemId}").build(itemId))
                    .retrieve()
                    .bodyToMono(InventoryDTO.class)
                    .block();

            assert inventoryResponse != null;

            Integer productId = inventoryResponse.getProductId();

            ProductDTO productResponse = productWebClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/product/{productId}").build(productId))
                    .retrieve()
                    .bodyToMono(ProductDTO.class)
                    .block();

            assert productResponse != null;

            if (inventoryResponse.getQuantity() > 0){
                if (productResponse.getForSale() == 1){
                    orderRepo.save(modelMapper.map(orderDTO, Orders.class));
                }else {
                    return new ErrorOrderResponse("Item Not For Sale");
                }

                return new SuccessOrderResponse(orderDTO);
            }else {
                return new ErrorOrderResponse("Item Not Available, Please Try Again later");
            }

        }catch (WebClientResponseException e){
            if (e.getStatusCode().is5xxServerError()){
                return new ErrorOrderResponse("Item not Found");
            }
        }

        return null;
    }

    public OrderDTO getOrderById(int orderId) {
        Orders order = orderRepo.getOrderById(orderId);
        return  modelMapper.map(order, OrderDTO.class);
    }

    public OrderDTO updateOrder(OrderDTO OrderDTO) {
        orderRepo.save(modelMapper.map(OrderDTO, Orders.class));
        return OrderDTO;
    }

    public String deleteOrder(Integer orderId) {
        orderRepo.deleteById(orderId);
        return "Order deleted";
    }
}
