package com.evalarttest.demo.controller;

import com.evalarttest.demo.dto.BranchRequest;
import com.evalarttest.demo.dto.ProductRequest;
import com.evalarttest.demo.service.FranchiseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/branches")
public class BranchController {
  private final FranchiseService franchiseService;

  public BranchController(FranchiseService franchiseService) {
	this.franchiseService = franchiseService;
  }

  @PostMapping("/add/{franchiseId}")
  public Mono<ResponseEntity<String>> addBranchToFranchise(
		  @PathVariable String franchiseId,
		  @RequestBody BranchRequest branchRequest) {
	return franchiseService.addBranchToFranchise(franchiseId, branchRequest)
			.map(franchise -> ResponseEntity.ok("Branch added successfully to franchise: " + franchise.getName()))
			.onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body("Error: " + e.getMessage())));
  }

  @PostMapping("/add-product/{franchiseId}/{branchName}")
  public Mono<ResponseEntity<String>> addProductToBranch(
		  @PathVariable String franchiseId,
		  @PathVariable String branchName,
		  @RequestBody ProductRequest productRequest) {
	return franchiseService.addProductToBranch(franchiseId, branchName, productRequest)
			.map(franchise -> ResponseEntity.ok("Product added successfully to branch: " + branchName))
			.onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body("Error: " + e.getMessage())));
  }

  @DeleteMapping("/remove-product/{franchiseId}/{branchName}/{productName}")
  public Mono<ResponseEntity<String>> removeProductFromBranch(
		  @PathVariable String franchiseId,
		  @PathVariable String branchName,
		  @PathVariable String productName) {
	return franchiseService.removeProductFromBranch(franchiseId, branchName, productName)
			.map(franchise -> ResponseEntity.ok("Product removed successfully from branch: " + branchName))
			.onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body("Error: " + e.getMessage())));
  }

  @PutMapping("/update-stock/{franchiseId}/{branchName}/{productName}")
  public Mono<ResponseEntity<String>> updateProductStock(
		  @PathVariable String franchiseId,
		  @PathVariable String branchName,
		  @PathVariable String productName,
		  @RequestParam("stock") int newStock) {
	return franchiseService.updateProductStock(franchiseId, branchName, productName, newStock)
			.map(franchise -> ResponseEntity.ok("Stock updated successfully for product: " + productName))
			.onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body("Error: " + e.getMessage())));
  }

  @PutMapping("/update-branch-name/{franchiseId}/{branchName}")
  public Mono<ResponseEntity<String>> updateBranchName(
		  @PathVariable String franchiseId,
		  @PathVariable String branchName,
		  @RequestParam("newName") String newName) {
	return franchiseService.updateBranchName(franchiseId, branchName, newName)
			.map(updatedFranchise -> ResponseEntity.ok("Branch name updated successfully to: " + newName))
			.onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body("Error: " + e.getMessage())));
  }

  @PutMapping("/update-product-name/{franchiseId}/{branchName}/{productName}")
  public Mono<ResponseEntity<String>> updateProductName(
		  @PathVariable String franchiseId,
		  @PathVariable String branchName,
		  @PathVariable String productName,
		  @RequestParam("newName") String newName) {
	return franchiseService.updateProductName(franchiseId, branchName, productName, newName)
			.map(updatedFranchise -> ResponseEntity.ok("Product name updated successfully to: " + newName))
			.onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body("Error: " + e.getMessage())));
  }


}
