package com.ecommerce.aura_store.controller;

import com.ecommerce.aura_store.entity.Product;
import com.ecommerce.aura_store.entity.Shipment;
import com.ecommerce.aura_store.entity.User;
import com.ecommerce.aura_store.entity.enums.PaymentMethod;
import com.ecommerce.aura_store.entity.enums.ShipmentStatus;
import com.ecommerce.aura_store.repository.ProductRepository;
import com.ecommerce.aura_store.repository.ShipmentRepository;
import com.ecommerce.aura_store.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuraStoreControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Test
    void allEndpointsWorkForHappyPathFlow() throws Exception {
        Product product = new Product();
        product.setName("Aura Watch");
        product.setPrice(new BigDecimal("199.99"));
        product.setStockQuantity(10);
        product.setDescription("Premium wearable");
        product.setImageUrl("https://example.com/watch.png");
        product.setSku("AURA-WATCH-001");
        product.setActive(true);
        product = productRepository.save(product);

        String userRequest = """
                {
                  "name": "Aarav Sharma",
                  "email": "aarav@example.com",
                  "phone": "9876543210",
                  "password": "plain-text-demo"
                }
                """;

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequest))
                .andExpect(status().isOk());

        User user = userRepository.findAll().stream()
                .filter(savedUser -> "aarav@example.com".equals(savedUser.getEmail()))
                .findFirst()
                .orElseThrow();

        String addressRequest = """
                {
                  "addressLine1": "221B Baker Street",
                  "addressLine2": "Apartment 4",
                  "city": "Mumbai",
                  "state": "Maharashtra",
                  "pinCode": "400001",
                  "type": "HOME"
                }
                """;

        String addressResponse = mockMvc.perform(post("/api/address/add/{userId}", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressLine1").value("221B Baker Street"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode addressJson = objectMapper.readTree(addressResponse);
        long addressId = addressJson.path("addressId").asLong();
        assertFalse(addressJson.has("password"));

        String productsResponse = mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode productsJson = objectMapper.readTree(productsResponse);
        boolean foundProduct = false;
        for (JsonNode productJson : productsJson) {
            if (productJson.path("productId").asLong() == product.getProductId()) {
                assertEquals("Aura Watch", productJson.path("name").asText());
                foundProduct = true;
                break;
            }
        }
        assertEquals(true, foundProduct);

        mockMvc.perform(get("/api/products/{productId}", product.getProductId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(product.getProductId()))
                .andExpect(jsonPath("$.name").value("Aura Watch"));

        String addToCartRequest = """
                {
                  "userId": %d,
                  "productId": %d,
                  "quantity": 2
                }
                """.formatted(user.getUserId(), product.getProductId());

        String cartResponse = mockMvc.perform(post("/api/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addToCartRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productId").value(product.getProductId()))
                .andExpect(jsonPath("$.items[0].cartItemId").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode cartJson = objectMapper.readTree(cartResponse);
        long cartItemId = cartJson.path("items").get(0).path("cartItemId").asLong();

        mockMvc.perform(get("/api/cart/{userId}", user.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].cartItemId").value(cartItemId))
                .andExpect(jsonPath("$.items[0].quantity").value(2));

        String updateCartRequest = """
                {
                  "quantity": 3
                }
                """;

        mockMvc.perform(put("/api/cart/item/{cartItemId}", cartItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateCartRequest))
                .andExpect(status().isOk());

        String placeOrderRequest = """
                {
                  "userId": %d,
                  "addressId": %d
                }
                """.formatted(user.getUserId(), addressId);

        String orderResponse = mockMvc.perform(post("/api/orders/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(placeOrderRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus").value("Waiting_For_Payment"))
                .andExpect(jsonPath("$.orderItems[0].quantity").value(3))
                .andReturn()
                .getResponse()
                .getContentAsString();

        long orderId = objectMapper.readTree(orderResponse).path("orderId").asLong();

        mockMvc.perform(get("/api/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.orderItems[0].productName").value("Aura Watch"));

        mockMvc.perform(get("/api/orders/{orderId}/track", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus").value("Waiting_For_Payment"))
                .andExpect(jsonPath("$.paymentStatus").value("INITIATED"))
                .andExpect(jsonPath("$.shipmentStatus").doesNotExist());

        String paymentRequest = """
                {
                  "orderId": %d,
                  "paymentMethod": "%s"
                }
                """.formatted(orderId, PaymentMethod.COD.name());

        mockMvc.perform(post("/api/payments/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("SUCCESS"));

        Shipment shipment = shipmentRepository.findAll().stream().findFirst().orElseThrow();
        assertNotNull(shipment.getOrder());

        mockMvc.perform(put("/api/shipments/{shipmentId}", shipment.getShipmentId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(ShipmentStatus.SHIPPED.name()));

        mockMvc.perform(get("/api/orders/{orderId}/track", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trackingNumber").isString())
                .andExpect(jsonPath("$.shipmentStatus").value(ShipmentStatus.SHIPPED.name()))
                .andExpect(jsonPath("$.paymentStatus").value("SUCCESS"));
    }

    @Test
    void deleteCartItemWorksForExistingCartEntry() throws Exception {
        Product product = new Product();
        product.setName("Aura Ring");
        product.setPrice(new BigDecimal("49.99"));
        product.setStockQuantity(4);
        product.setDescription("Smart ring");
        product.setImageUrl("https://example.com/ring.png");
        product.setSku("AURA-RING-001");
        product.setActive(true);
        product = productRepository.save(product);

        User user = new User();
        user.setName("Delete Test");
        user.setEmail("delete@example.com");
        user.setPassword("password");
        user.setPhone("9999999999");
        user = userRepository.save(user);

        String addToCartRequest = """
                {
                  "userId": %d,
                  "productId": %d,
                  "quantity": 1
                }
                """.formatted(user.getUserId(), product.getProductId());

        String cartResponse = mockMvc.perform(post("/api/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addToCartRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long cartItemId = objectMapper.readTree(cartResponse).path("items").get(0).path("cartItemId").asLong();

        mockMvc.perform(delete("/api/cart/item/{cartItemId}", cartItemId))
                .andExpect(status().isOk());

        String cartAfterDelete = mockMvc.perform(get("/api/cart/{userId}", user.getUserId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode items = objectMapper.readTree(cartAfterDelete).path("items");
        assertEquals(0, items.size());
    }
}
