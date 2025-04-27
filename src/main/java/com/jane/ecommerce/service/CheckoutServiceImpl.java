package com.jane.ecommerce.service;

import com.jane.ecommerce.dao.CustomerRepository;
import com.jane.ecommerce.dto.PaymentInfo;
import com.jane.ecommerce.dto.Purchase;
import com.jane.ecommerce.dto.PurchaseResponse;
import com.jane.ecommerce.entity.Customer;
import com.jane.ecommerce.entity.Order;
import com.jane.ecommerce.entity.OrderItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class CheckoutServiceImpl implements CheckoutService {

    private CustomerRepository customerRepository;

    public CheckoutServiceImpl(CustomerRepository customerRepository,
                               @Value("${stripe.key.secret}")String secretKey){
        this.customerRepository = customerRepository;

        Stripe.apiKey = secretKey;
    }

    @Override
    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {
        //retrieve the order from the dto
        Order order = purchase.getOrder();

        // generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        //populate order with order items
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(item -> order.add(item));

        // populate billing and shipping addresses
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());

        // populate customer with order
        Customer customer = purchase.getCustomer();
        String theEmail = customer.getEmail();

        Customer customerFromDB = customerRepository.findByEmail(theEmail);

        if(customerFromDB !=null){
            customer = customerFromDB;
        }
        customer.add(order);

        //save to the database
        customerRepository.save(customer);

        return new PurchaseResponse(orderTrackingNumber);

    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {

        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("description", "Luv2Shop purchase");
        params.put("receipt_email", paymentInfo.getReceiptEmail());

        return PaymentIntent.create(params);
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
