package service;

import Util.Validation;
import domain.Account;
import domain.Customer;
import domain.Transaction;
import domain.Type;
import execptions.AccountNotFoundException;
import execptions.InsufficientFundsException;
import execptions.ValidationException;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository=new AccountRepository();
    private final TransactionRepository transactionRepository=new TransactionRepository();
    private final CustomerRepository customerRepository=new CustomerRepository();

    private final Validation<String>validationName=name ->{
        if(name==null || name.isBlank())throw new ValidationException("Name is required");
    };

    private final Validation<String>validationEmail=email ->{
        if(email==null || !email.contains("@"))throw new ValidationException("Email is required");
    };

    private final Validation<String>validationType=type ->{
        if(type==null || !type.equalsIgnoreCase("SAVINGS") || type.equalsIgnoreCase("CURRENT"))
            throw new ValidationException("Type must be SAVINGS or CURRENT");
    };

    private final Validation<Double>validationAmountPositive=amount ->{
        if(amount==null || amount<0 )
            throw new ValidationException("Please enter valid amount");
    };

    @Override
    public String openAccount(String name, String email, String accountType) {
        validationName.validation(name);
        validationEmail.validation(email);
        validationType.validation(accountType);
        String customerId= UUID.randomUUID().toString();

        //CHANGE LATER -->10 + 1=AC11
        //String accountNumber= UUID.randomUUID().toString();
        String accountNumber=getAccountNumber();
        Account account=new Account(accountNumber,accountType,(double)0,customerId);

        accountRepository.save(account);

        // create customer
        Customer c=new Customer(customerId,name,email);
        customerRepository.save(c);
        return accountNumber;
    }

    @Override
    public List<Account> listAccounts() {
        return accountRepository.findAll().stream()
                .sorted(Comparator.comparing(Account::getAccountNumber))
                .collect(Collectors.toList());
    }

    @Override
    public void deposit(String accountNumber, Double amount, String note) {
        validationAmountPositive.validation(amount);
        Account account =accountRepository.findByNumber(accountNumber)
                        .orElseThrow(()->new AccountNotFoundException("Account not found : " + accountNumber));
        account.setBalance(account.getBalance() + amount);
        Transaction transaction=new Transaction( UUID.randomUUID().toString(), // id
                Type.DEPOSITE,                // type
                accountNumber,                // accountNumber
                LocalDateTime.now(),          // timeStamp
                amount,                       // amount
                note                          // note
        );
        transactionRepository.add(transaction);
    }

    @Override
    public void withdraw(String accountNumber, Double amount, String note) {
        validationAmountPositive.validation(amount);
        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " +
                        accountNumber));
        if (account.getBalance().compareTo(amount)<0) {
            throw new InsufficientFundsException("Insufficient Balance");
        }
        account.setBalance(account.getBalance() - amount);
        Transaction transaction=new Transaction( UUID.randomUUID().toString(), // id
                Type.WITHDRAW,                // type
                accountNumber,                // accountNumber
                LocalDateTime.now(),          // timeStamp
                amount,                       // amount
                note                          // note
        );
        transactionRepository.add(transaction);
    }

    @Override
    public void transfer(String fromAccount, String toAccount, Double amount, String note) {
        validationAmountPositive.validation(amount);
        if(fromAccount.equals(toAccount))
            throw new ValidationException("Cannot transfer to your own account");
        Account fromAcc = accountRepository.findByNumber(fromAccount)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " +
                        fromAccount));
        Account toAcc = accountRepository.findByNumber(toAccount)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " +
                        toAccount));
        if (fromAcc.getBalance().compareTo(amount)<0) {
            throw new InsufficientFundsException("Insufficient Balance");
        }
            fromAcc.setBalance(fromAcc.getBalance() - amount);
            toAcc.setBalance(toAcc.getBalance() + amount);

            Transaction fromTransaction=new Transaction( UUID.randomUUID().toString(), // id
                    Type.TRANSFER_OUT,                // type
                   fromAcc.getAccountNumber(),                // accountNumber
                    LocalDateTime.now(),          // timeStamp
                    amount,                       // amount
                    note                          // note
            );
            transactionRepository.add(fromTransaction);

            Transaction toTransaction=new Transaction( UUID.randomUUID().toString(), // id
                    Type.TRANSFER_IN,                // type
                    toAcc.getAccountNumber(),                // accountNumber
                    LocalDateTime.now(),          // timeStamp
                    amount,                       // amount
                    note                          // note
            );
            transactionRepository.add(toTransaction);
    }

    @Override
    public List<Transaction> getStatement(String account) {
        return transactionRepository.findByAccount(account).stream()
                .sorted(Comparator.comparing(Transaction::getTimeStamp))
                .collect(Collectors.toList());
    }

    @Override
    public List<Account> searchAccountsByCustomerName(String q) {
       String query= (q==null)? " ":q.toLowerCase();
//        List<Account>result=new ArrayList<>();
//        for(Customer c:customerRepository.findAll()){
//            if(c.getName().toLowerCase().contains(query))
//                result.addAll(accountRepository.findByCustomerId(c.getId()));
//        }
//        result.sort(Comparator.comparing(Account::getAccountNumber));
//        return result;

        return customerRepository.findAll().stream()
                .filter(c->c.getName().toLowerCase().contains(query))
                .flatMap(c->accountRepository.findByCustomerId(c.getId()).stream())
                .sorted(Comparator.comparing(Account::getAccountNumber))
                        .collect(Collectors.toList());

    }

    private String getAccountNumber() {
        int size=accountRepository.findAll().size() + 1;
        String accountNumber=String.format("AC%06d",size);
        return accountNumber;
    }
}
