package com.miniproject.controller;
import com.miniproject.exception.*;
import com.miniproject.model.Account;
import com.miniproject.repository.UserRepository;
import com.miniproject.service.AccountService;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final UserRepository userRepository;

    @Autowired
    public AccountController(AccountService accountService, UserRepository userRepository) {
        this.accountService = accountService;
        this.userRepository = userRepository;
    }

    // Get all accounts of a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAllAccounts(@PathVariable Long userId) {
        // Check if the user exists
        if (!userRepository.existsById(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Fetch accounts for the user
        List<Account> userAccounts = accountService.getAllAccountsByUserId(userId);
        return new ResponseEntity<>(userAccounts, HttpStatus.OK);
    }

    // Create new account for user
    @PostMapping("/user/{userId}")
    public ResponseEntity<Account> createAccount(@PathVariable Long userId, @RequestBody Account newAccount) {
        Account createdAccount = accountService.createAccount(userId, newAccount);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    // Delete account
    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long accountId) {
        boolean deleted = accountService.deleteAccount(accountId);
        if (deleted) {
            return ResponseEntity.ok("Account deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Account does not exist");
        }
    }

    // Transfer balance between accounts
    @PostMapping("/transfer")
    public ResponseEntity<String> transferBalance(@RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam BigDecimal amount) {
        try {
            boolean sameUser = accountService.transferBalance(fromAccountId, toAccountId, amount);
            if (sameUser) {
                return ResponseEntity.ok("Balance transfer successful");
            } else {
                return ResponseEntity.ok("Balance transfer successful between different users");
            }
        } catch (DifferentCurrencyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Currency types of the source and target accounts do not match");
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Insufficient balance in the source account");
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("One or both accounts do not exist");
        } 
    }
}
