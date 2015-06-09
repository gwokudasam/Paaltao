package com.paaltao.models;

import org.litepal.crud.DataSupport;

/**
 * Created by arindam.paaltao on 08-Jun-15.
 */
public class States extends DataSupport{

    private String name;

    private float price;

    private Countries countries;

    // generated getters and setters.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Countries getCountries() {
        return countries;
    }

    public void setCountries(Countries countries) {
        this.countries = countries;
    }
}