package com.evalarttest.demo.dto;
import lombok.Data;
import java.util.List;

@Data
public class FranchiseRequest {
  	private String Id;
	private String Name; // Nombre de la franquicia
  	private List<BranchRequest> Branches;
}



