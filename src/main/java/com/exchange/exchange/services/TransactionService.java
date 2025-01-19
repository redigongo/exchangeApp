package com.exchange.exchange.services;

import com.exchange.exchange.DTO.TransactionFilterDTO;
import com.exchange.exchange.exceptions.NotFoundException;
import com.exchange.exchange.models.Transaction;
import com.exchange.exchange.repositories.TransactionRepository;
import com.exchange.exchange.specifications.TransactionSpecification;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    @CachePut(value = "transactions", key = "#transaction.id")
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }


    @Cacheable("transactions")
    public Transaction getTransaction(Long id) {
        System.out.println("(cache test)Fetching transaction from database...");
        return transactionRepository.findById(id).orElseThrow( () ->
                new NotFoundException("Could not find Transaction with id: " + id));
    }


    public Page<Transaction> findAll(TransactionFilterDTO filterDTO, int offset, int max) {

        Pageable pageable = PageRequest.of(offset, max);
        TransactionSpecification specification = new TransactionSpecification(filterDTO);
        return transactionRepository.findAll(specification, pageable);

    }

}
