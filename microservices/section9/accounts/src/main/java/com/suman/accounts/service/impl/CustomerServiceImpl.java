package com.suman.accounts.service.impl;

import com.suman.accounts.Mapper.AccountsMapper;
import com.suman.accounts.Mapper.CustomerMapper;
import com.suman.accounts.dto.AccountsDto;
import com.suman.accounts.dto.CardsDto;
import com.suman.accounts.dto.CustomerDetailsDto;
import com.suman.accounts.dto.LoansDto;
import com.suman.accounts.entity.Accounts;
import com.suman.accounts.entity.Customer;
import com.suman.accounts.exception.ResourceNotFoundException;
import com.suman.accounts.repository.AccountRepository;
import com.suman.accounts.repository.CustomerRepository;
import com.suman.accounts.service.ICustomerService;
import com.suman.accounts.service.client.CardsFeignClient;
import com.suman.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber + "")
        );
        Accounts account = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString() + "")
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(account, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId, mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;
    }
}
