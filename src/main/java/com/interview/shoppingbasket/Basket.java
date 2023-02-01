package com.interview.shoppingbasket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Basket {
    private List<BasketItem> items = new ArrayList<>();

    public void add(String productCode, String productName, int quantity) {
        BasketItem basketItem = new BasketItem();
        basketItem.setProductCode(productCode);
        basketItem.setProductName(productName);
        basketItem.setQuantity(quantity);

        items.add(basketItem);
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public void consolidateItems() {
        HashMap<String, Integer> quantityPerProductCode = new HashMap<>();
        HashMap<String, BasketItem> basketItemPerProductCode = new HashMap<>();

        for (BasketItem basketItem : items) {
            String productCode = basketItem.getProductCode();

            quantityPerProductCode.merge(productCode, basketItem.getQuantity(), Integer::sum);
            basketItem.setQuantity(quantityPerProductCode.get(productCode));

            basketItemPerProductCode.put(productCode, basketItem);
        }

        items = new ArrayList<>(basketItemPerProductCode.values());
    }
}
