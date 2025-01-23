package com.evalarttest.demo.model;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
  private String name;
  private int stock; // Cantidad en Stock

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getStock() {
    return stock;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }
}
