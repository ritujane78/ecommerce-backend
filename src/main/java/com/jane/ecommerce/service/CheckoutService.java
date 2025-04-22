package com.jane.ecommerce.service;

import com.jane.ecommerce.dto.PaymentInfo;
import com.jane.ecommerce.dto.Purchase;
import com.jane.ecommerce.dto.PurchaseResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface CheckoutService {
    PurchaseResponse placeOrder(Purchase purchase);

    PaymentIntent createPaymentIntent(PaymentInfo paymentInfo ) throws StripeException;
}
