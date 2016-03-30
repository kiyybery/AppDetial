package com.xyn.appdetial.model;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class Category {

    private static final long serialVersionUID = -1263606641582007003L;
    public String id;
    public String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
