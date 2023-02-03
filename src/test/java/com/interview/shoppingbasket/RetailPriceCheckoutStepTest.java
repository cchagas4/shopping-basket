package com.interview.shoppingbasket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RetailPriceCheckoutStepTest {

    PricingService pricingService;
    CheckoutContext checkoutContext;
    Basket basket;
    RetailPriceCheckoutStep retailPriceCheckoutStep;

    @BeforeEach
    void setup() {
        pricingService = Mockito.mock(PricingService.class);
        checkoutContext = Mockito.mock(CheckoutContext.class);
        basket = new Basket();
        retailPriceCheckoutStep = new RetailPriceCheckoutStep(pricingService);

        when(checkoutContext.getBasket()).thenReturn(basket);
    }

    @Test
    void setPriceZeroForEmptyBasket() {

        retailPriceCheckoutStep.execute(checkoutContext);

        Mockito.verify(checkoutContext).setRetailPriceTotal(0.0);
    }

    @Test
    void setPriceOfProductToBasketItem() {

        basket.add("product1", "myproduct1", 10);
        basket.add("product2", "myproduct2", 10);

        when(pricingService.getPrice("product1")).thenReturn(3.99);
        when(pricingService.getPrice("product2")).thenReturn(2.0);

        retailPriceCheckoutStep.execute(checkoutContext);
        Mockito.verify(checkoutContext).setRetailPriceTotal(3.99 * 10 + 2 * 10);

    }

    @Test
    void applyPromotionGetFreeItemsTest() {
        int quantity = 10;
        int totalItemsWillPay = 7;
        double price = 3.99;
        double expectedFinalValue = price * totalItemsWillPay;

        BasketItem basketItem = new BasketItem();
        basketItem.setProductCode("product1");
        basketItem.setProductName("product1");
        basketItem.setQuantity(quantity);

        Promotion promotion = new Promotion();
        promotion.createPromotionsFreeItems(2, 1);

        double finalPrice = retailPriceCheckoutStep.applyPromotion(promotion, basketItem, price);

        assertEquals(expectedFinalValue, finalPrice);
    }

    @Test
    void applyPromotionDirectDiscountTest() {
        int quantity = 3;
        BasketItem basketItem = new BasketItem();
        basketItem.setProductCode("product1");
        basketItem.setProductName("product1");
        basketItem.setQuantity(quantity);

        Promotion promotion1 = new Promotion();
        promotion1.createPromotionDirectDiscount(10);
        double price1 = 9.99;
        double expectedFinalValue1 = price1 * quantity * 0.9;

        Promotion promotion2 = new Promotion();
        promotion2.createPromotionDirectDiscount(65);
        double price2 = 9.99;
        double expectedFinalValue2 = price1 * quantity * 0.35;


        double tenPercentFinalPrice = retailPriceCheckoutStep.applyPromotion(promotion1, basketItem, price1);
        double fiftyPercentFinalPrice = retailPriceCheckoutStep.applyPromotion(promotion2, basketItem, price2);

        assertEquals(expectedFinalValue1, tenPercentFinalPrice);
        assertEquals(expectedFinalValue2, fiftyPercentFinalPrice);
    }

    @Test
    void applyPromotionNotValid() {
        int quantity = 3;

        BasketItem basketItem = new BasketItem();
        basketItem.setProductCode("product1");
        basketItem.setProductName("product1");
        basketItem.setQuantity(quantity);

        Promotion promotion = new Promotion();
        promotion.createPromotionsFreeItems(5, 1);
        double price = 3.99;
        double expectedFinalValue = price * quantity;

        double finalPrice = retailPriceCheckoutStep.applyPromotion(promotion, basketItem, price);

        assertEquals(expectedFinalValue, finalPrice);
    }
}
