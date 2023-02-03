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

            if ((checkoutContext.getPromotions()).isEmpty()) {
                retailTotal += quantity * price;
            } else {
                double finalPrice = 0.0;
                for (Promotion promotion : checkoutContext.getPromotions()) {
                    finalPrice = applyPromotion(promotion, basketItem, price);
                }
                retailTotal += finalPrice;
            }
            basketItem.setProductRetailPrice(price);
        }
        checkoutContext.setRetailPriceTotal(retailTotal);
    }

    public double applyPromotion(Promotion promotion, BasketItem item, double price) {
        double promotionPrice;

        int currentQuantity = item.getQuantity();
        if (promotion.isDirectDiscount()) {
            double totalPrice = currentQuantity * price;
            promotionPrice = totalPrice * promotion.getPercentage();
        } else if (promotion.isValid(currentQuantity)) {
            int promotionQuantity = promotion.getItemsQuantity() + promotion.getFreeItemsQuantity();
            int outPromotionItems = currentQuantity % promotionQuantity;
            int inPromotionItems = currentQuantity - outPromotionItems;

            int freeItems = inPromotionItems / promotionQuantity;
            int itemsWillPay = inPromotionItems - freeItems;

            double outPromotionPrice = outPromotionItems * price;
            double inPromotionPrice = itemsWillPay * price;

            promotionPrice = inPromotionPrice + outPromotionPrice;
        } else {
            promotionPrice = currentQuantity * price;
        }

        return promotionPrice;
    }
}
