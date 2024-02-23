package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Restaurant {

    private String name;
    private String image_url;
    private float rating;
    private String price;

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", image_url='" + image_url + '\'' +
                ", rating=" + rating +
                ", price='" + price + '\'' +
                '}';
    }
}
