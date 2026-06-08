package com.suman.accounts.service;

import com.suman.accounts.dto.CustomerDto;

public interface IAccountService {

    /**
     * @param customerDto - Customer DTO Object
     * */
    void createAccount(CustomerDto customerDto);

    CustomerDto fetchAccount(String mobileNumber);

    boolean updateAccount(CustomerDto customerDto);

    boolean deleteAccount(String mobileNumber);

}
