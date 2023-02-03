package com.interview.shoppingbasket;

import java.util.List;

public class PromotionRetrieveCheckoutStep implements CheckoutStep {

    PromotionsService promotionsService;

    public PromotionRetrieveCheckoutStep(PromotionsService promotionsService) {
        this.promotionsService = promotionsService;
    }

    @Override
    public void execute(CheckoutContext checkoutContext) {
        List<Promotion> promotions = promotionsService.getPromotions(checkoutContext.getBasket());

        checkoutContext.setPromotions(promotions);
    }
}
