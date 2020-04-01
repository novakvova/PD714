package com.example.begin.productview.netwok;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductCreateDTO {
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("price")
    @Expose
    private String price;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "title='" + title + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
