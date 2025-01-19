package com.exchange.exchange.services;

import com.exchange.exchange.DTO.rate.RateCheckRequestDTO;
import com.exchange.exchange.DTO.rate.RateCheckResponseDTO;
import com.exchange.exchange.DTO.external.CurrencyConversionResponseDTO;
import com.exchange.exchange.exceptions.AccessKeyException;
import com.exchange.exchange.exceptions.AccessRestrictedException;
import com.exchange.exchange.exceptions.NotFoundException;
import com.exchange.exchange.models.Transaction;
import com.exchange.exchange.repositories.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class ExchangeService {

    @Value("${api.access.token}")
    private String accessToken;

    ObjectMapper objectMapper = new ObjectMapper();

    private final TransactionService transactionService;

    public ExchangeService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }



    public Transaction exchange(RateCheckRequestDTO exchangeRequest) throws Exception {

        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());

        RateCheckResponseDTO rate = getRate(exchangeRequest);

        Transaction transaction = new Transaction();
        transaction.setDate(now);
        transaction.setAmount(exchangeRequest.getAmount());
        transaction.setConvertedAmount(rate.getAmount());
        transaction.setTargetCurrency(exchangeRequest.getTargetCurrency());
        transaction.setSourceCurrency(exchangeRequest.getSourceCurrency());
        transaction.setRate(rate.getRate());

        return transactionService.saveTransaction(transaction);
    }


    public RateCheckResponseDTO getRate(RateCheckRequestDTO exchangeRequest) throws Exception {


        if (exchangeRequest.getSourceCurrency() == null) {
            throw new BadRequestException("Source currency is required");
        }

        if (exchangeRequest.getTargetCurrency() == null) {
            throw new BadRequestException("Target currency is required");
        }

        if (exchangeRequest.getSourceCurrency().equals(exchangeRequest.getTargetCurrency())) {
            throw new BadRequestException("Source currency and target currency cannot be the same");
        }

        LocalDateTime requestDate = LocalDateTime.now();

        String url = new StringBuilder("http://data.fixer.io/api/latest")
                .append("?access_key=").append(accessToken)
                .append("&base=").append(exchangeRequest.getSourceCurrency())
                .append("&symbols=").append(exchangeRequest.getTargetCurrency())
                .toString();


        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        System.out.println("getRate: Calling -> " + url);

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        CurrencyConversionResponseDTO res = objectMapper.readValue(response.body(), CurrencyConversionResponseDTO.class);

        System.out.println("getRate: Status Code -> " + response.statusCode());

        if (!res.getSuccess()) {
            Integer code = res.getError().getCode();
            String info = res.getError().getInfo();

            if (code == 104 || code == 105 || code == 429) {
                throw new AccessRestrictedException(info);
            }

            if (code == 401 || code == 403) {
                throw new AccessKeyException(info);
            }

            if (code == 400 || code == 601 || code == 602 || code == 603 || code == 604 || code == 605) {
                throw new BadRequestException(info);
            }
            if (code == 404) {
                throw new NotFoundException(info);
            }
        }

        RateCheckResponseDTO rate = new RateCheckResponseDTO();

        rate.setAmount(exchangeRequest.getAmount() * res.getRates().get(exchangeRequest.getTargetCurrency()));
        rate.setRate(res.getRates().get(exchangeRequest.getTargetCurrency()));
        rate.setRateDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(res.getTimestamp()), ZoneId.systemDefault()));
        rate.setRequestDate(requestDate);
        rate.setSourceCurrency(exchangeRequest.getSourceCurrency());
        rate.setTargetCurrency(exchangeRequest.getTargetCurrency());


        return rate;
    }

}
