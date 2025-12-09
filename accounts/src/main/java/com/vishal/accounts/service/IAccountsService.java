package com.vishal.accounts.service;

import com.vishal.accounts.dto.AccountsDto;
import com.vishal.accounts.dto.CustomerDto;

import java.util.List;

public interface IAccountsService {
    /**
     *
     * @param customerDto
     */
    void createAccount(CustomerDto customerDto);

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    CustomerDto fetchAccount(String mobileNumber);

    /**
     *
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    boolean updateAccount(CustomerDto customerDto);


    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    boolean deleteAccount(String mobileNumber);

    /*
    *
    * @return List of Accounts
    * */
    List<CustomerDto> getAllAccounts();
}
