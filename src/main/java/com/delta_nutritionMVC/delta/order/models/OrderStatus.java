package com.delta_nutritionMVC.delta.order.models;

public enum OrderStatus {
    PAS_ENCORE_LIVREE("Pas encore livrée"),
    EN_ROUTE("En route"),
    LIVREE("Livrée"),
    ANNULEE("Annulée");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
