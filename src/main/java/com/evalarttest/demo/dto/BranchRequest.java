package com.evalarttest.demo.dto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class BranchRequest{

  private String name;
  private List<ProductRequest> products;
}