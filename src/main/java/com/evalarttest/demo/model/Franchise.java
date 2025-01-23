package com.evalarttest.demo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "franchises")
public class Franchise {

  @Id
  private String id;
  private String name;
  private List<Branch> branches; // Lista de sucursales

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<Branch> getBranches() {
    return branches;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setBranches(List<Branch> branches) {
    this.branches = branches;
  }
}
