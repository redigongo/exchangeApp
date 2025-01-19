package com.exchange.exchange.controllers;

import com.exchange.exchange.DTO.TransactionFilterDTO;
import com.exchange.exchange.exceptions.NotFoundException;
import com.exchange.exchange.models.Transaction;
import com.exchange.exchange.services.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<Transaction>> filterAll(@RequestBody(required = false) TransactionFilterDTO filter,
                                                       @RequestParam(required = false, defaultValue = "20", name = "max") Integer max,
                                                       @RequestParam(required = false, defaultValue = "0", name = "offset") Integer offset) {
        try {
            Page<Transaction> transactions = transactionService.findAll(filter, offset, max);
            return ResponseEntity.ok(transactions);
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            Transaction transactions = transactionService.getTransaction(id);
            return ResponseEntity.ok(transactions);
        }catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}
