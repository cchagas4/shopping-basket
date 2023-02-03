package com.interview.shoppingbasket;

import lombok.Getter;

@Getter
public class Promotion {
    double discount;
    int itemsQuantity;
    int freeItemsQuantity;
    boolean directDiscount;

    public void createPromotionDirectDiscount(int discount) {
        this.discount = discount;
        this.directDiscount = true;
    }

    public void createPromotionsFreeItems(int itemsQuantity, int freeItemsQuantity) {
        this.itemsQuantity = itemsQuantity;
        this.freeItemsQuantity = freeItemsQuantity;
        this.directDiscount = false;
    }

    public double getPercentage() {
        return (1 - this.discount / 100);
    }

    public boolean isValid(int currentQuantity) {
        return currentQuantity >= this.itemsQuantity;
    }
}
