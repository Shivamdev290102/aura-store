package com.ecommerce.aura_store.service;

import com.ecommerce.aura_store.dto.*;
import com.ecommerce.aura_store.entity.*;
import com.ecommerce.aura_store.entity.enums.OrderStatus;
import com.ecommerce.aura_store.entity.enums.PaymentMethod;
import com.ecommerce.aura_store.entity.enums.PaymentStatus;
import com.ecommerce.aura_store.entity.enums.ShipmentStatus;
import com.ecommerce.aura_store.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;


@Service
public class AuraStoreService {

    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;
    private final AddressRepository addressRepo;
    private final OrderRepository orderRepo;
    private final PaymentRepository paymentRepo;
    private final ShipmentRepository shipmentRepo;

    public AuraStoreService(UserRepository userRepo, ProductRepository productRepo, CartRepository cartRepo, CartItemRepository itemRepo, AddressRepository addressRepo, OrderRepository orderRepo, PaymentRepository paymentRepo, ShipmentRepository shipmentRepo) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.cartRepo = cartRepo;
        this.itemRepo = itemRepo;
        this.addressRepo = addressRepo;
        this.orderRepo = orderRepo;
        this.paymentRepo = paymentRepo;
        this.shipmentRepo = shipmentRepo;
    }

    private UserDTO mapUserEntityToDTO(User user){
        if(user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());

        return dto;
    }

    private User mapUserDTOToEntity(UserDTO dto){
        if(dto == null) return null;
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(dto.getPassword());

        return user;
    }

    private ProductDTO mapProductEntityToDTO(Product product){
        if(product == null) return null;
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());

        return dto;
    }

    private Address mapAddressDTOtoEntity(AddressDTO dto){
        Address add = new Address();
        add.setAddressLine1(dto.getAddressLine1());
        add.setAddressLine2(dto.getAddressLine2());
        add.setType(dto.getType());
        add.setCity(dto.getCity());
        add.setState(dto.getState());
        add.setPinCode(dto.getPinCode());
        add.setUser(dto.getUser());
        return add;
    }

    private AddressDTO mapAddressEntityToDTO(Address address) {
        if (address == null) return null;
        AddressDTO dto = new AddressDTO();
        dto.setAddressId(address.getAddressId());
        dto.setAddressLine1(address.getAddressLine1());
        dto.setAddressLine2(address.getAddressLine2());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPinCode(address.getPinCode());
        dto.setType(address.getType());
        return dto;
    }

    private Product mapProductDTOToEntity(ProductDTO dto){
        if(dto == null) return null;
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());
        product.setStockQuantity(0);
        product.setActive(true);

        return product;
    }

    private CartDTO mapCartEntityToDTO(Cart cart){
        if(cart == null) return null;
        CartDTO dto = new CartDTO();
        dto.setCartId(cart.getCartId());
        List<CartItemDTO> itemDTOS = new ArrayList<>();

        if(cart.getCartItems() != null){
            for(CartItem item : cart.getCartItems()){
                CartItemDTO itemDTO = new CartItemDTO();
                itemDTO.setCartItemId(item.getCartItemId());
                itemDTO.setQuantity(item.getQuantity());
                if (item.getProduct() != null) {
                    itemDTO.setPrice(item.getProduct().getPrice());
                    itemDTO.setProductName(item.getProduct().getName());
                    itemDTO.setProductId(item.getProduct().getProductId());
                    itemDTO.setImageUrl(item.getProduct().getImageUrl());
                }

                itemDTOS.add(itemDTO);
            }
        }
        dto.setItems(itemDTOS);
        return dto;
    }

    private OrderDTO mapOrderEntityToDTO(Order order){
        if(order == null) return null;
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setOrderDate(order.getOrderDate());
        dto.setUserName(order.getUser().getName());
        dto.setOrderItems(order.getOrderItems());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setAddress(order.getAddressLine1() + " " + order.getAddressLine2() + ", " + order.getCity() + ", " + order.getState() + "-" + order.getPinCode());
        return dto;
    }

    public CartDTO getCartByUserId(Long userId){
        Cart cart = cartRepo.findByUserUserId(userId);
        if(cart == null) {
            throw new RuntimeException("No cart associated with the user");
        }
        return mapCartEntityToDTO(cart);

    }

    public CartDTO addToCart(Long userId, Long productId, Integer quantity){
        if(quantity == null || quantity <= 0){
            throw new RuntimeException("Invalid quantity");
        }
        User user = userRepo.findUsersByUserId(userId);
        if(user == null){
            throw new RuntimeException("User not found");
        }

        Product product = productRepo.findProductByProductId(productId);
        if(product == null){
            throw new RuntimeException("Product not found");
        }
        if(!product.getActive()) {
            throw new RuntimeException("Product not active");
        }
        if(product.getStockQuantity() < quantity){
            throw new RuntimeException("Out of Stock");
        }


        Cart cart = cartRepo.findByUserUserId(userId);
        if(cart == null){
            cart = new Cart();
            cart.setUser(user);
            cart.setCartItems(new ArrayList<>());
        }
        if(cart.getCartItems() == null){
            cart.setCartItems(new ArrayList<>());
        }
        List<CartItem> items = cart.getCartItems();
        boolean quantityUpdated = false;
        for(CartItem item : items){
            if(item.getProduct() != null && Objects.equals(item.getProduct().getProductId(), productId)){
                int updatedQuantity = item.getQuantity() + quantity;
                if(product.getStockQuantity() < updatedQuantity){
                    throw new RuntimeException("Out of Stock");
                }
                item.setQuantity(updatedQuantity);
                quantityUpdated = true;
                break;
            }
        }
        if(!quantityUpdated){
            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setPriceAtAdd(product.getPrice());
            item.setCart(cart);
            items.add(item);
        }
        cartRepo.save(cart);

        return mapCartEntityToDTO(cart);

    }

    public void updateCartItemQuantity(Long cartItemId, Integer quantity){
        if(quantity == null || quantity <= 0){
            removeCartItem(cartItemId);
            return;
        }
        CartItem item =  itemRepo.findByCartItemId(cartItemId);
        if(item == null) throw new RuntimeException("Item not found");
        if (item.getProduct() != null && item.getProduct().getStockQuantity() >= quantity) {
            item.setQuantity(quantity);
        }
        else throw new RuntimeException("Out of Stock");
        itemRepo.save(item);


    }
    public void removeCartItem(Long cartItemId){
        CartItem item = itemRepo.findByCartItemId(cartItemId);
        if(item == null) throw new RuntimeException("Item not found");
        itemRepo.delete(item);
    }


    @Transactional
    public OrderDTO placeOrder(Long userId, Long addressId){
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepo.findByUserUserId(userId);
        if(cart == null || cart.getCartItems().isEmpty()) throw new RuntimeException("Cart or CartItem not found");

        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if(!address.getUser().getUserId().equals(userId)){
            throw new RuntimeException("User does not have any such address");
        }

        List<CartItem> items = cart.getCartItems();
        for(CartItem item : items){
            if(item.getQuantity()>item.getProduct().getStockQuantity()){
                throw new RuntimeException("The given Item is out of stock" + item.getProduct().getName());
            }
        }

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setUser(user);
        order.setCity(address.getCity());
        order.setAddressLine1(address.getAddressLine1());
        order.setAddressLine2(address.getAddressLine2());
        order.setPinCode(address.getPinCode());
        order.setState(address.getState());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.valueOf(0);

        for(CartItem item : items){
            OrderItem orderItem = new OrderItem();
            Product currProduct = item.getProduct();
            orderItem.setOrder(order);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setProductName(currProduct.getName());
            orderItem.setPriceAtPurchase(currProduct.getPrice());
            orderItem.setProduct(currProduct);
            BigDecimal itemTotal = currProduct.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
            currProduct.setStockQuantity(currProduct.getStockQuantity() - item.getQuantity());
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setOrderStatus(OrderStatus.Waiting_For_Payment);
        Order savedOrder = orderRepo.save(order);
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentStatus(PaymentStatus.INITIATED);
        payment.setPaymentMethod(PaymentMethod.UPI);
        payment.setTransactionId("123SUCCESS456");
        savedOrder.setPayment(payment);
        paymentRepo.save(payment);
        cart.getCartItems().clear();
        cartRepo.save(cart);
        return mapOrderEntityToDTO(savedOrder);

    }

    public PaymentStatus processPayment(Long orderId, PaymentMethod paymentMethod){
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        Payment payment = order.getPayment();
        int randomNum = ThreadLocalRandom.current().nextInt(1, 7);
        payment.setPaymentMethod(paymentMethod);
        if(paymentMethod == PaymentMethod.COD) {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setPaymentDate(LocalDateTime.now());
        }
        else {
            if(randomNum <5) {
                payment.setPaymentStatus(PaymentStatus.SUCCESS);
                payment.setPaymentDate(LocalDateTime.now());
                payment.setTransactionId("123SUCCESS456");
            }
            else{
                payment.setPaymentStatus(PaymentStatus.FAILED);
                payment.setPaymentDate(LocalDateTime.now());
                payment.setTransactionId("987FAILED654");
            }
        }
        if(payment.getPaymentStatus() == PaymentStatus.SUCCESS){
            order.setOrderStatus(OrderStatus.Ordered);
        }
        else{
            order.setOrderStatus(OrderStatus.Failed);
        }
        orderRepo.save(order);
        paymentRepo.save(payment);
        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            Shipment shipment = new Shipment();
            shipment.setOrder(order);
            shipment.setShipmentStatus(ShipmentStatus.CREATED);
            shipment.setTrackingNumber("345" + order.getOrderId() + "987");
            shipment.setDeliveryAgent("Delhivery");
            order.setShipment(shipment);
            shipmentRepo.save(shipment);
        }
        return payment.getPaymentStatus();

    }

    public ShipmentStatus updateShipmentStatus(Long shipmentId){
        Shipment shipment = shipmentRepo.findById(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));
        ShipmentStatus currStatus = shipment.getShipmentStatus();
        switch(currStatus){
            case FAILED -> shipment.setShipmentStatus(ShipmentStatus.CREATED);
            case CREATED -> {
                shipment.setShipmentStatus(ShipmentStatus.SHIPPED);
                shipment.setEstimatedDeliveryDate(LocalDate.now().plusDays(5));
            }
            case SHIPPED -> shipment.setShipmentStatus(ShipmentStatus.IN_TRANSIT);
            case IN_TRANSIT -> shipment.setShipmentStatus(ShipmentStatus.OUT_FOR_DELIVERY);
            case OUT_FOR_DELIVERY -> {
                shipment.setShipmentStatus(ShipmentStatus.DELIVERED);
                shipment.setDeliveredDate(LocalDate.now());
            }
            case DELIVERED -> shipment.setShipmentStatus(ShipmentStatus.DELIVERED);
            default -> shipment.setShipmentStatus(ShipmentStatus.FAILED);
        }
        shipmentRepo.save(shipment);
        mapShipmentStatusToOrder(shipment.getOrder());
        return shipment.getShipmentStatus();
    }

    public void mapShipmentStatusToOrder(Order order){
        switch(order.getShipment().getShipmentStatus()){
            case SHIPPED -> order.setOrderStatus(OrderStatus.Shipped);
            case DELIVERED -> order.setOrderStatus(OrderStatus.Delivered);
        }
        orderRepo.save(order);
    }

    private OrderStatus getStatusByOrder(Long orderId){
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return order.getOrderStatus();
    }


    public OrderDTO getOrder(Long orderId) {
        Order order =  orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return mapOrderEntityToDTO(order);
    }

    public TrackOrder trackOrder(Long orderId){
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not Found"));
        TrackOrder track = new TrackOrder();
        track.setOrderStatus(order.getOrderStatus());
        if (order.getPayment() != null) {
            track.setPaymentStatus(order.getPayment().getPaymentStatus());
        }
        if (order.getShipment() != null) {
            track.setTrackingNumber(order.getShipment().getTrackingNumber());
            track.setShipmentStatus(order.getShipment().getShipmentStatus());
        }

        return track;
    }

    public void createUser(UserDTO dto) {
        User user = mapUserDTOToEntity(dto);
        userRepo.save(user);
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepo.findAll();
        List<ProductDTO> productDTOS= new ArrayList<>();
        for(Product prod : products){
            productDTOS.add(mapProductEntityToDTO(prod));
        }
        return productDTOS;
    }

    public ProductDTO getProductById(Long productId) {
        Product prod = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return mapProductEntityToDTO(prod);
    }

    public AddressDTO addAddress(AddressDTO dto, Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("No user found"));
        dto.setUser(user);
        Address add = mapAddressDTOtoEntity(dto);
        Address savedAddress = addressRepo.save(add);
        return mapAddressEntityToDTO(savedAddress);
    }
}
