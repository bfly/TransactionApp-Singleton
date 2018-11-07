package edu.dcccd.lesson11.controller;

import edu.dcccd.lesson11.entity.Transaction;
import edu.dcccd.lesson11.repository.SingletonTransaction;
import edu.dcccd.lesson11.services.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class TransactionController {

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    TransactionServiceImpl transactionService;

    @GetMapping("/")
    public String homePage(Model model){
        model.addAttribute("appName",appName);
        return "home";
    }

    @GetMapping("/transaction")
    public String loadTransactionPage(Model model){
        List<Transaction> transactions = transactionService.getAllTransaction();
        model.addAttribute("transactionForm", new Transaction());
        model.addAttribute("days",getDays());
        model.addAttribute("transactions", transactions);
        return "transaction";
    }

    @PostMapping(value="/create")
    public String createTransaction(@ModelAttribute("transactionForm") Transaction transaction){
        transaction.setId(SingletonTransaction.getInstance().autoIncrementID+=1);
        transactionService.createTransaction(transaction);
        return "redirect:transaction";
    }

    @GetMapping(value="/report")
    public String createReport(Model model) {
        List<Transaction> transactions = transactionService.getAllTransaction();
        model.addAttribute("transactions", transactions);
        return "report";
    }

    public List<String> getDays(){
        List<String> days = new ArrayList<>();
        Arrays.asList(DayOfWeek.values()).stream()
                .forEach(day->days.add(day.toString()));
        return days;
    }
}