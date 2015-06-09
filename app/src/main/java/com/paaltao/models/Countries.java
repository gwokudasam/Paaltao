package com.paaltao.models;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arindam.paaltao on 08-Jun-15.
 */
public class Countries extends DataSupport {

    private String value;

    public String getValue() {
        return value;
    }

    public void setvalue(String value) {
        this.value = value;
    }

    public float getLabel() {
        return label;
    }

    public void setLabel(float label) {
        this.label = label;
    }

    public List<States> getStates() {
        return states;
    }

    public void setStates(List<States> states) {
        this.states = states;
    }

    private float label;

    private List<States> states = new ArrayList<States>();

    // generated getters and setters.

}