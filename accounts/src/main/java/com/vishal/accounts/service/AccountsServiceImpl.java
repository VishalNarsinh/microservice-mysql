package com.vishal.accounts.service;

import com.vishal.accounts.constants.AccountsConstants;
import com.vishal.accounts.dto.AccountsDto;
import com.vishal.accounts.dto.CustomerDto;
import com.vishal.accounts.entity.Accounts;
import com.vishal.accounts.entity.Customer;
import com.vishal.accounts.exception.DuplicateMobileNumberException;
import com.vishal.accounts.exception.ResourceNotFoundException;
import com.vishal.accounts.mapper.AccountsMapper;
import com.vishal.accounts.mapper.CustomerMapper;
import com.vishal.accounts.repository.AccountsRepository;
import com.vishal.accounts.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto,new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customer.getMobileNumber());
        if(optionalCustomer.isPresent()){
            throw new DuplicateMobileNumberException("Customer already registered with given mobileNumber "+customer.getMobileNumber());
        }
        Customer savedCustomer= customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));

    }
    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        return newAccount;
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer,new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts,new AccountsDto()));
        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto !=null ){
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(() -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString()));
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString()));
            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        HashSet<Integer> accounts = new HashSet<Integer>();
        return true;
    }

    @Override
    public List<CustomerDto> getAllAccounts() {
//        List<Customer> customerList = customerRepository.findAll();
//        List<AccountsDto> accountsDtoList = new ArrayList<>();
//        customerList.stream().forEach((customer) -> {
//            accountsRepository.findByCustomerId(customer.getCustomerId()).ifPresent(accounts -> accountsDtoList.add(AccountsMapper.mapToAccountsDto(accounts,new AccountsDto())));
//        });
//        List<CustomerDto> customerDtoList = customerList.stream().map(customer -> {
//            CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
//            customerDto.setAccountsDto(accountsDtoList.get(customerList.indexOf(customer)));
//            return customerDto;
//        }).collect(Collectors.toList());
//        return customerDtoList;
        List<Customer> customerList = customerRepository.findAll();
        List<Long> customerIds = customerList.stream().map(Customer::getCustomerId).toList();
        List<Accounts> accountsList = accountsRepository.findByCustomerIdIn(customerIds);

        Map<Long, AccountsDto> customerAccountsMap = accountsList.stream().collect(
                Collectors.toMap(Accounts::getCustomerId, accounts -> AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()))
        );

        return customerList.stream().map(customer -> {
            CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
            customerDto.setAccountsDto(customerAccountsMap.get(customer.getCustomerId()));
            return customerDto;
        }).collect(Collectors.toList());

    }
}
