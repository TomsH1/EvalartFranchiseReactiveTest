package com.evalarttest.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.evalarttest.demo.dto.FranchiseRequest;
import com.evalarttest.demo.service.FranchiseService;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@RequestMapping("/api/franchises")
public class FranchiseController
{
  private final FranchiseService franchiseService;

  public FranchiseController(FranchiseService franchiseService) {
	this.franchiseService = franchiseService;
  }

  @PostMapping(name = "addFranchise", value = "/create")
  public Mono<ResponseEntity<String>> addFranchise(@RequestBody FranchiseRequest franchiseRequest) {
	return franchiseService.addFranchise(franchiseRequest)
			.map(savedFranchise -> ResponseEntity.ok("Franchise created successfully with ID: " + savedFranchise.getId()))
			.onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body("Error: " + e.getMessage())));
  }

  @GetMapping("/max-stock/{franchiseId}")
  public Mono<ResponseEntity<List<String>>> getMaxStockProductByBranch(
		  @PathVariable String franchiseId) {
	return franchiseService.getMaxStockProductByBranch(franchiseId)
			.map(ResponseEntity::ok)
			.onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(null)));
  }

  @PutMapping("/update-franchise-name/{franchiseId}")
  public Mono<ResponseEntity<String>> updateFranchiseName(
		  @PathVariable String franchiseId,
		  @RequestParam("newName") String newName) {
	return franchiseService.updateFranchiseName(franchiseId, newName)
			.map(updatedFranchise -> ResponseEntity.ok("Franchise name updated successfully to: " + newName))
			.onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body("Error: " + e.getMessage())));
  }


}
