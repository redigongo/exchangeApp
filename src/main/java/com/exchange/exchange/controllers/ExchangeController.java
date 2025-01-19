package com.exchange.exchange.controllers;


import com.exchange.exchange.DTO.rate.RateCheckRequestDTO;
import com.exchange.exchange.DTO.ErrorDTO;
import com.exchange.exchange.exceptions.AccessKeyException;
import com.exchange.exchange.exceptions.AccessRestrictedException;
import com.exchange.exchange.exceptions.NotFoundException;
import com.exchange.exchange.services.ExchangeService;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @PostMapping("/rate")
    public ResponseEntity<?> getRate(@RequestBody RateCheckRequestDTO exchangeRequest) {


        try {
            return new ResponseEntity<>(exchangeService.getRate(exchangeRequest), HttpStatus.OK);
        }catch (BadRequestException e) {
            System.out.println("getRate: " + e.getMessage());

            return handleError(HttpStatus.BAD_REQUEST, e.getMessage());
        }catch (AccessRestrictedException e) {
            System.out.println("getRate: " + e.getMessage());

            return handleError(HttpStatus.FORBIDDEN, e.getMessage());
            }catch (AccessKeyException e) {

            System.out.println("getRate: " + e.getMessage());
            return handleError(HttpStatus.UNAUTHORIZED, e.getMessage());
        }catch (NotFoundException e) {

            System.out.println("getRate: " + e.getMessage());
            return handleError(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e) {

            System.out.println("getRate: " + e.getMessage());
            return handleError(HttpStatus.INTERNAL_SERVER_ERROR, "An error has occurred while processing your request");
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> exchangeTransaction(@RequestBody RateCheckRequestDTO exchangeRequest) {

        try {
            return new ResponseEntity<>(exchangeService.exchange(exchangeRequest), HttpStatus.OK);
        }catch (BadRequestException e) {
            System.out.println("getRate: " + e.getMessage());

            return handleError(HttpStatus.BAD_REQUEST, e.getMessage());
        }catch (AccessRestrictedException e) {
            System.out.println("getRate: " + e.getMessage());

            return handleError(HttpStatus.FORBIDDEN, e.getMessage());
        }catch (AccessKeyException e) {

            System.out.println("getRate: " + e.getMessage());
            return handleError(HttpStatus.UNAUTHORIZED, e.getMessage());
        }catch (NotFoundException e) {

            System.out.println("getRate: " + e.getMessage());
            return handleError(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e) {

            System.out.println("getRate: " + e.getMessage());
            return handleError(HttpStatus.INTERNAL_SERVER_ERROR, "An error has occurred while processing your request");
        }
    }

    private ResponseEntity<?> handleError(HttpStatus status, String message) {
        ErrorDTO errorResponse = new ErrorDTO();
        errorResponse.setCode(status.value());
        errorResponse.setInfo(message);

        return new ResponseEntity<>(errorResponse, status);
    }

}
