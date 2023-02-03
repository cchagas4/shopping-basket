package com.interview.shoppingbasket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CheckoutPipelineTest {

    CheckoutPipeline checkoutPipeline;

    PricingService pricingService;

    PromotionsService promotionsService;

    @Mock
    Basket basket;

    @Mock
    CheckoutStep checkoutStep1;

    @Mock
    CheckoutStep checkoutStep2;

    @BeforeEach
    void setup() {
        checkoutPipeline = new CheckoutPipeline();
        promotionsService = Mockito.mock(PromotionsService.class);
        pricingService = Mockito.mock(PricingService.class);
        basket = new Basket();
    }

    @Test
    void returnZeroPaymentForEmptyPipeline() {
        PaymentSummary paymentSummary = checkoutPipeline.checkout(basket);

        assertEquals(0.0, paymentSummary.getRetailTotal());
    }

    @Test
    void executeAllPassedCheckoutSteps() {
        double price1 = 3.99;
        double price2 = 13.99;

        List<Promotion> promotions = new ArrayList<>();
        promotions.add(new Promotion(1, 1));
        promotions.add(new Promotion(50));
        promotions.add(new Promotion(10));

        basket.add("prodCode1", "prod name 1", 10);
        basket.add("prodCode2", "prod name 2", 5);

        when(promotionsService.getPromotions(any(Basket.class))).thenReturn(promotions);
        when(pricingService.getPrice("prodCode1")).thenReturn(price1);
        when(pricingService.getPrice("prodCode2")).thenReturn(price2);

        BasketConsolidationCheckoutStep consolidationCheckoutStep = new BasketConsolidationCheckoutStep();
        PromotionRetrieveCheckoutStep promotionCheckoutStep = new PromotionRetrieveCheckoutStep(promotionsService);
        RetailPriceCheckoutStep retailPriceCheckoutStep = new RetailPriceCheckoutStep(pricingService);

        checkoutPipeline.addStep(consolidationCheckoutStep);
        checkoutPipeline.addStep(promotionCheckoutStep);
        checkoutPipeline.addStep(retailPriceCheckoutStep);


        PaymentSummary paymentSummary = checkoutPipeline.checkout(basket);

        Mockito.verify(promotionsService).getPromotions(basket);
        Mockito.verify(pricingService).getPrice("prodCode1");
        Mockito.verify(pricingService).getPrice("prodCode2");


        assertEquals(98.86500000000001, paymentSummary.getRetailTotal());

    }

}
