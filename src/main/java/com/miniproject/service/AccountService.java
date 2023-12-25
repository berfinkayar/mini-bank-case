package com.miniproject.service;
import com.miniproject.exception.*;
import com.miniproject.model.*;
import com.miniproject.repository.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public List<Account> getAllAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Account createAccount(Long userId, Account newAccount) {
        // Retrieve the user if exists, otherwise throw error
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

        // Create the account
        Account account = new Account();
        account.setCurrency(newAccount.getCurrency());
        account.setBalance(newAccount.getBalance());
        account.setPassword(newAccount.getPassword());
        account.setUser(user);

        // Save the account
        return accountRepository.save(account);
    }

    public boolean deleteAccount(Long accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isPresent()) {
            accountRepository.deleteById(accountId);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean transferBalance(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        // Check if both accounts exist
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException(fromAccountId));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException(toAccountId));

        // Check if their currencies match
        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) throw new DifferentCurrencyException();
        
        // Check if the accounts belong to the same user
        boolean isSameUser = fromAccount.getUser() == toAccount.getUser();

        // Check if the balance in the source account is sufficient
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException();
        }

        // Transfer is done if all conditions are met
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return isSameUser;
    }
}

