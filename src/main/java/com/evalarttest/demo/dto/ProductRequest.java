package com.evalarttest.demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ProductRequest{
  private String name;
  private int stock; //Cantidad en stock
}
