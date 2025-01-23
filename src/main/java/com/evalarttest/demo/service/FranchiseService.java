package com.evalarttest.demo.service;

import com.evalarttest.demo.dto.BranchRequest;
import com.evalarttest.demo.dto.FranchiseRequest;
import com.evalarttest.demo.dto.ProductRequest;
import com.evalarttest.demo.model.Branch;
import com.evalarttest.demo.model.Franchise;
import com.evalarttest.demo.model.Product;
import com.evalarttest.demo.repository.FranchiseRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FranchiseService {

  private final FranchiseRepository franchiseRepository;

  public FranchiseService(FranchiseRepository franchiseRepository) {
	this.franchiseRepository = franchiseRepository;
  }

  public Mono<Franchise> addFranchise(FranchiseRequest franchiseRequest) {
	// Convert FranchiseRequest to Franchise model
	Franchise franchise = new Franchise();
	franchise.setName(franchiseRequest.getName());

	//We only map branches if the list has more than 1 element
	if(franchiseRequest.getBranches() != null && !franchiseRequest.getBranches().isEmpty()){
	  franchise.setBranches(mapBranches(franchiseRequest.getBranches()));
	} else {
	  franchise.setBranches(new ArrayList<>());
	}

	/* Save to database using reactive repository */
	System.out.println("franchise = " + franchise);
	return franchiseRepository.save(franchise);
  }

  public Mono<Franchise> addBranchToFranchise(String franchiseId, BranchRequest branchRequest) {
	return franchiseRepository.findById(franchiseId)
	  .flatMap(franchise -> {
		Branch newBranch = new Branch();
		newBranch.setName(branchRequest.getName());
		newBranch.setProducts(mapProducts(branchRequest.getProducts()));
		franchise.getBranches().add(newBranch);
		return franchiseRepository.save(franchise);
	  });
  }

  public Mono<Franchise> addProductToBranch(String franchiseId, String branchName, ProductRequest productRequest) {
	return franchiseRepository.findById(franchiseId)
	  .flatMap(franchise -> {
		Branch branch = franchise.getBranches().stream()
		  .filter(b -> b.getName().equalsIgnoreCase(branchName))
		  .findFirst()
		  .orElseThrow(() -> new RuntimeException("Branch not found"));

		Product newProduct = new Product();
		newProduct.setName(productRequest.getName());
		newProduct.setStock(productRequest.getStock());
		branch.getProducts().add(newProduct);

		return franchiseRepository.save(franchise);
	  });
  }

  public Mono<Franchise> removeProductFromBranch(String franchiseId, String branchName, String productName) {
	return franchiseRepository.findById(franchiseId)
			.flatMap(franchise -> {
			  Branch branch = franchise.getBranches().stream()
					  .filter(b -> b.getName().equalsIgnoreCase(branchName))
					  .findFirst()
					  .orElseThrow(() -> new RuntimeException("Branch not found"));

			  branch.getProducts().removeIf(product -> product.getName().equalsIgnoreCase(productName));
			  return franchiseRepository.save(franchise);
			});
  }

  public Mono<Franchise> updateProductStock(String franchiseId, String branchName, String productName, int newStock) {
	return franchiseRepository.findById(franchiseId)
			.flatMap(franchise -> {
			  Branch branch = franchise.getBranches().stream()
					  .filter(b -> b.getName().equalsIgnoreCase(branchName))
					  .findFirst()
					  .orElseThrow(() -> new RuntimeException("Branch not found"));

			  branch.getProducts().stream()
					  .filter(product -> product.getName().equalsIgnoreCase(productName))
					  .findFirst()
					  .orElseThrow(() -> new RuntimeException("Product not found"))
					  .setStock(newStock);

			  return franchiseRepository.save(franchise);
			});
  }

  public Mono<List<String>> getMaxStockProductByBranch(String franchiseId) {
	return franchiseRepository.findById(franchiseId)
	  .map(franchise -> franchise.getBranches().stream()
			  .map(branch -> {
				Product maxStockProduct = branch.getProducts().stream()
						.max(Comparator.comparingInt(Product::getStock))
						.orElseThrow(() -> new RuntimeException("No products found in branch: " + branch.getName()));
				return "Branch: " + branch.getName() + ", Product: " + maxStockProduct.getName() + ", Stock: " + maxStockProduct.getStock();
			  })
			  .collect(Collectors.toList()));
  }

  public Mono<Franchise> updateFranchiseName(String franchiseId, String newName) {
	return franchiseRepository.findById(franchiseId)
			.switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))
			.flatMap(franchise -> {
			  franchise.setName(newName);
			  return franchiseRepository.save(franchise);
			});
  }
  public Mono<Franchise> updateBranchName(String franchiseId, String branchName, String newName) {
	return franchiseRepository.findById(franchiseId)
			.switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))
			.flatMap(franchise -> {
			  Branch branch = franchise.getBranches().stream()
					  .filter(b -> b.getName().equalsIgnoreCase(branchName))
					  .findFirst()
					  .orElseThrow(() -> new RuntimeException("Branch not found"));

			  branch.setName(newName);
			  return franchiseRepository.save(franchise);
			});
  }

  public Mono<Franchise> updateProductName(String franchiseId, String branchName, String productName, String newName) {
	return franchiseRepository.findById(franchiseId)
			.switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))
			.flatMap(franchise -> {
			  Branch branch = franchise.getBranches().stream()
					  .filter(b -> b.getName().equalsIgnoreCase(branchName))
					  .findFirst()
					  .orElseThrow(() -> new RuntimeException("Branch not found"));

			  Product product = branch.getProducts().stream()
					  .filter(p -> p.getName().equalsIgnoreCase(productName))
					  .findFirst()
					  .orElseThrow(() -> new RuntimeException("Product not found"));

			  product.setName(newName);
			  return franchiseRepository.save(franchise);
			});
  }

  private List<Branch> mapBranches(List<BranchRequest> branchRequests) {
	if(!branchRequests.isEmpty()){
	  return branchRequests.stream()
			  .map(branchRequest -> {
				Branch branch = new Branch();
				branch.setName(branchRequest.getName());
				branch.setProducts(mapProducts(branchRequest.getProducts()));
				return branch;
			  })
			  .collect(Collectors.toList());
	}else {
		return new ArrayList<>();
	}
  }

  private List<Product> mapProducts(List<ProductRequest> productRequests) {
	if(!productRequests.isEmpty()){
	  return productRequests.stream()
			  .map(productRequest -> {
				Product product = new Product();
				product.setName(productRequest.getName());
				product.setStock(productRequest.getStock());
				return product;
			  })
			  .collect(Collectors.toList());
	} else {
	  return new ArrayList<>();
	}
  }
}
