package com.ecommerce.aura_store.service;

import com.ecommerce.aura_store.dto.CartDTO;
import com.ecommerce.aura_store.dto.TrackOrder;
import com.ecommerce.aura_store.entity.Cart;
import com.ecommerce.aura_store.entity.CartItem;
import com.ecommerce.aura_store.entity.Order;
import com.ecommerce.aura_store.entity.Payment;
import com.ecommerce.aura_store.entity.Product;
import com.ecommerce.aura_store.entity.User;
import com.ecommerce.aura_store.entity.enums.OrderStatus;
import com.ecommerce.aura_store.entity.enums.PaymentMethod;
import com.ecommerce.aura_store.entity.enums.PaymentStatus;
import com.ecommerce.aura_store.repository.AddressRepository;
import com.ecommerce.aura_store.repository.CartItemRepository;
import com.ecommerce.aura_store.repository.CartRepository;
import com.ecommerce.aura_store.repository.OrderRepository;
import com.ecommerce.aura_store.repository.PaymentRepository;
import com.ecommerce.aura_store.repository.ProductRepository;
import com.ecommerce.aura_store.repository.ShipmentRepository;
import com.ecommerce.aura_store.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuraStoreServiceTests {

    @Mock
    private UserRepository userRepo;
    @Mock
    private ProductRepository productRepo;
    @Mock
    private CartRepository cartRepo;
    @Mock
    private CartItemRepository itemRepo;
    @Mock
    private AddressRepository addressRepo;
    @Mock
    private OrderRepository orderRepo;
    @Mock
    private PaymentRepository paymentRepo;
    @Mock
    private ShipmentRepository shipmentRepo;

    @InjectMocks
    private AuraStoreService service;

    @Test
    void addToCartRejectsMergedQuantityThatExceedsStock() {
        User user = new User();
        user.setUserId(1L);

        Product product = new Product();
        product.setProductId(10L);
        product.setActive(true);
        product.setStockQuantity(5);
        product.setPrice(BigDecimal.TEN);

        CartItem existingItem = new CartItem();
        existingItem.setCartItemId(99L);
        existingItem.setProduct(product);
        existingItem.setQuantity(4);
        existingItem.setPriceAtAdd(BigDecimal.TEN);

        Cart cart = new Cart();
        cart.setCartId(7L);
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>());
        cart.getCartItems().add(existingItem);
        existingItem.setCart(cart);

        when(userRepo.findUsersByUserId(1L)).thenReturn(user);
        when(productRepo.findProductByProductId(10L)).thenReturn(product);
        when(cartRepo.findByUserUserId(1L)).thenReturn(cart);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.addToCart(1L, 10L, 2));

        assertEquals("Out of Stock", ex.getMessage());
    }

    @Test
    void addToCartReturnsCartItemIdsForFrontendMutations() {
        User user = new User();
        user.setUserId(1L);

        Product product = new Product();
        product.setProductId(10L);
        product.setName("Watch");
        product.setImageUrl("img");
        product.setPrice(BigDecimal.TEN);
        product.setActive(true);
        product.setStockQuantity(5);

        Cart cart = new Cart();
        cart.setCartId(7L);
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>());

        CartItem existingItem = new CartItem();
        existingItem.setCartItemId(99L);
        existingItem.setProduct(product);
        existingItem.setQuantity(1);
        existingItem.setPriceAtAdd(BigDecimal.TEN);
        existingItem.setCart(cart);
        cart.getCartItems().add(existingItem);

        when(userRepo.findUsersByUserId(1L)).thenReturn(user);
        when(productRepo.findProductByProductId(10L)).thenReturn(product);
        when(cartRepo.findByUserUserId(1L)).thenReturn(cart);
        when(cartRepo.save(cart)).thenReturn(cart);

        CartDTO dto = service.addToCart(1L, 10L, 1);

        assertEquals(99L, dto.getItems().get(0).getCartItemId());
        assertEquals(2, dto.getItems().get(0).getQuantity());
    }

    @Test
    void trackOrderHandlesOrdersWithoutShipmentYet() {
        Order order = new Order();
        order.setOrderId(15L);
        order.setOrderStatus(OrderStatus.Waiting_For_Payment);

        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.INITIATED);
        order.setPayment(payment);

        when(orderRepo.findById(15L)).thenReturn(Optional.of(order));

        TrackOrder trackOrder = service.trackOrder(15L);

        assertEquals(OrderStatus.Waiting_For_Payment, trackOrder.getOrderStatus());
        assertEquals(PaymentStatus.INITIATED, trackOrder.getPaymentStatus());
        assertNull(trackOrder.getShipmentStatus());
        assertNull(trackOrder.getTrackingNumber());
    }

    @Test
    void processPaymentPersistsSelectedPaymentMethod() {
        Order order = new Order();
        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.INITIATED);
        order.setPayment(payment);

        when(orderRepo.findById(22L)).thenReturn(Optional.of(order));

        service.processPayment(22L, PaymentMethod.COD);

        assertEquals(PaymentMethod.COD, payment.getPaymentMethod());
        assertNotNull(payment.getPaymentDate());
        verify(orderRepo).save(order);
        verify(paymentRepo).save(payment);
    }
}
