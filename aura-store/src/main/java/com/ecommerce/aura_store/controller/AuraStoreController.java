package com.ecommerce.aura_store.controller;

import com.ecommerce.aura_store.dto.*;
import com.ecommerce.aura_store.entity.enums.PaymentStatus;
import com.ecommerce.aura_store.entity.enums.ShipmentStatus;
import com.ecommerce.aura_store.service.AuraStoreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class AuraStoreController {

    private final AuraStoreService service;

    public AuraStoreController(AuraStoreService service) {
        this.service = service;
    }

    @PostMapping("user")
    public void createUser(@RequestBody UserDTO dto){
         service.createUser(dto);
    }

    @PostMapping("address/add/{userId}")
    public AddressDTO addAddress(@RequestBody AddressDTO dto, @PathVariable Long userId){
        return service.addAddress( dto, userId);
    }

    @GetMapping("/products")
    public List<ProductDTO> getAllProducts(){
        return service.getAllProducts();
    }

    @GetMapping("products/{productId}")
    public ProductDTO getProductById(@PathVariable Long productId){
        return service.getProductById(productId);
    }

    @GetMapping("/cart/{userId}")
    public CartDTO getCart(@PathVariable Long userId){
        return service.getCartByUserId(userId);
    }

    @PostMapping("/cart/add")
    public CartDTO addToCart(@RequestBody AddToCartRequest dto){
        return service.addToCart(dto.getUserId(), dto.getProductId(), dto.getQuantity());
    }

    @PutMapping("/cart/item/{cartItemId}")
    public void updateCartItemQuantity(@PathVariable Long cartItemId, @RequestBody UpdateCartItemRequest request){
        service.updateCartItemQuantity(cartItemId, request.getQuantity());
    }

    @DeleteMapping("/cart/item/{cartItemId}")
    public void deleteCartItem(@PathVariable Long cartItemId){
        service.removeCartItem(cartItemId);
    }

    @PostMapping("/orders/place")
    public OrderDTO placeOrder(@RequestBody PlaceOrderRequest request){
        return service.placeOrder(request.getUserId(), request.getAddressId());
    }

    @GetMapping("/orders/{orderId}")
    public OrderDTO getOrder(@PathVariable Long orderId){
        return service.getOrder(orderId);
    }

    @PostMapping("/payments/process")
    public PaymentStatus processPayment(@RequestBody PaymentRequest request){
        return service.processPayment(request.getOrderId(), request.getPaymentMethod());
    }


    @PutMapping("/shipments/{shipmentId}")
    public ShipmentStatus updateShipmentStatus(@PathVariable Long shipmentId){
        return service.updateShipmentStatus(shipmentId);
    }

    @GetMapping("/orders/{orderId}/track")
    public TrackOrder trackOrder(@PathVariable Long orderId){
        return service.trackOrder(orderId);
    }




}
