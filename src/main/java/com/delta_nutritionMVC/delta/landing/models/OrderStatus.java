package com.delta_nutritionMVC.delta.landing.models;

public enum OrderStatus {
    PAS_ENCORE_LIVREE("Pas encore livrée"),
    EN_ROUTE("En route"),
    ANNULEE("Annulée");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
