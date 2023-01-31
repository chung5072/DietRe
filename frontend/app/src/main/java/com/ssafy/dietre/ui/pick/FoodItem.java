package com.ssafy.dietre.ui.pick;

import java.io.Serializable;

public class FoodItem implements Serializable {

    private Long id;
    private String name;
    private String info;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public FoodItem(Long id, String name, String info) {
        this.id = id;
        this.name = name;
        this.info = info;
    }

}
