package com.interview.shoppingbasket;

public class RetailPriceCheckoutStep implements CheckoutStep {
    private final PricingService pricingService;
    private double retailTotal;

    public RetailPriceCheckoutStep(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @Override
    public void execute(CheckoutContext checkoutContext) {
        Basket basket = checkoutContext.getBasket();
        retailTotal = 0.0;

        for (BasketItem basketItem : basket.getItems()) {
            int quantity = basketItem.getQuantity();
            double price = pricingService.getPrice(basketItem.getProductCode());
            basketItem.setProductRetailPrice(price);
            retailTotal += quantity * price;
        }

        checkoutContext.setRetailPriceTotal(retailTotal);
    }

    public double applyPromotion(Promotion promotion, BasketItem item, double price) {

        int currentQuantity = item.getQuantity();
        if (promotion.isDirectDiscount()) {
            double totalPrice = currentQuantity * price;
            retailTotal = totalPrice * promotion.getPercentage();
        } else if (promotion.isValid(currentQuantity)) {
            int promotionQuantity = promotion.getItemsQuantity() + promotion.getFreeItemsQuantity();
            int outPromotionItems = currentQuantity % promotionQuantity;
            int inPromotionItems = currentQuantity - outPromotionItems;

            int freeItems = inPromotionItems / promotionQuantity;
            int itemsWillPay = inPromotionItems - freeItems;

            double outPromotionPrice = outPromotionItems * price;
            double inPromotionPrice = itemsWillPay * price;

            retailTotal = inPromotionPrice + outPromotionPrice;
        } else {
            retailTotal = currentQuantity * price;
        }

        return retailTotal;
    }
}
