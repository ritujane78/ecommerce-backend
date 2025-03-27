package com.jane.ecommerce.service;

import com.jane.ecommerce.dto.Purchase;
import com.jane.ecommerce.dto.PurchaseResponse;

public interface CheckoutService {
    PurchaseResponse placeOrder(Purchase purchase);
}
