package com.mindhub.homebanking.Controller;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mindhub.homebanking.DTOS.AccountDTO;
import com.mindhub.homebanking.DTOS.ClientDTO;
import com.mindhub.homebanking.DTOS.TransactionDTO;
import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Models.Transaction;
import com.mindhub.homebanking.Models.TransactionType;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.Repositories.TransactionRepository;
import com.mindhub.homebanking.Service.AccountService;
import com.mindhub.homebanking.Service.ClientService;
import com.mindhub.homebanking.Service.TransactionService;
import com.mindhub.homebanking.Utils.UtilsTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@RestController
public class TransactionController {

//    @Autowired
//    private ClientRepository clientRepository;
//    @Autowired
//    private AccountRepository accountRepository;
//    @Autowired
//    private TransactionRepository transactionRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/api/accounts/transactions/date")
    public ResponseEntity<Object> dateBetween (Authentication authentication,@RequestParam Long id, @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        Account account = accountService.findById(id);
        ClientDTO client = clientService.getClientAuthentication(authentication);

        if(account == null){
            return new ResponseEntity<>("The account doesn't exist", HttpStatus.FORBIDDEN);
        }
//        AccountDTO accountRequest = new AccountDTO(account);

        if(client.getAccount().stream().noneMatch(accountDTO -> accountDTO.getNumber().equals(account.getNumber()))){
            return new ResponseEntity<>("This account doesn't belong to you", HttpStatus.FORBIDDEN);
        }

        if (startDate.isBlank()){
            return new ResponseEntity<>("Start date can't on blank", HttpStatus.FORBIDDEN);
        }
        if(endDate.isBlank()){
            return new ResponseEntity<>("End date can't be on blank", HttpStatus.FORBIDDEN);
        }
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

        LocalDateTime startLocalDateTime = UtilsTransaction.dateToLocalDateTime(start);
        LocalDateTime endLocalDateTime = UtilsTransaction.dateToLocalDateTime(end).plusDays(1).minusSeconds(1);

        return new ResponseEntity<>(transactionService.findByIdAndDateBetween(account, startLocalDateTime, endLocalDateTime),HttpStatus.ACCEPTED);
    }

    @PostMapping("/api/accounts/transactions/pdf")
    public ResponseEntity<Object> PDF (Authentication authentication,@RequestParam Long id, @RequestParam String startDate, @RequestParam String endDate) throws ParseException, IOException, DocumentException {
        Account account = accountService.findById(id);
        ClientDTO client = clientService.getClientAuthentication(authentication);

        if(account == null){
            return new ResponseEntity<>("The account doesn't exist", HttpStatus.FORBIDDEN);
        }

        if(client.getAccount().stream().noneMatch(accountDTO -> accountDTO.getNumber().equals(account.getNumber()))){
            return new ResponseEntity<>("This account doesn't belong to you", HttpStatus.FORBIDDEN);
        }

        if (startDate.isBlank()){
            return new ResponseEntity<>("Start date can't on blank", HttpStatus.FORBIDDEN);
        }
        if(endDate.isBlank()){
            return new ResponseEntity<>("End date can't be on blank", HttpStatus.FORBIDDEN);
        }
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);

        LocalDateTime startLocalDateTime = UtilsTransaction.dateToLocalDateTime(start);
        LocalDateTime endLocalDateTime = UtilsTransaction.dateToLocalDateTime(end).plusDays(1).minusSeconds(1);

        List<TransactionDTO> listTransaction = transactionService.findByIdAndDateBetween(account, startLocalDateTime, endLocalDateTime);

        Document pdfTransaction = new Document();

        PdfWriter.getInstance(pdfTransaction, new FileOutputStream("Transaction From " + startDate + " to " + endDate + ".pdf"));
        pdfTransaction.open();

        Image logo = Image.getInstance("C:\\Users\\franc\\OneDrive\\Documentos\\homebanking\\src\\main\\resources\\static\\web\\img\\logo.png");
        logo.scaleToFit(100,100);
        pdfTransaction.add(logo);

        Font title = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
        Paragraph paragraph = new Paragraph("Hi " + client.getFirstName() + " " + client.getLastName(), title);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        paragraph.setSpacingAfter(22);
        paragraph.setSpacingBefore(22);
        pdfTransaction.add(paragraph);

        Font subTitle = new Font(Font.FontFamily.HELVETICA, 20);
        Paragraph subParagraph = new Paragraph("Here I leave the transactions made of the day " + startDate + " to " + endDate + " from your account " + account.getNumber(), subTitle);
        subParagraph.setAlignment(Paragraph.ALIGN_CENTER);
        subParagraph.setSpacingAfter(22);
        subParagraph.setSpacingBefore(22);
        pdfTransaction.add(subParagraph);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);

        PdfPCell amount = new PdfPCell(new Paragraph("Amount"));
        amount.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(amount);

        PdfPCell description = new PdfPCell(new Paragraph("Description"));
        description.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(description);

        PdfPCell date = new PdfPCell(new Paragraph("Date"));
        date.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(date);

        PdfPCell type = new PdfPCell(new Paragraph("Type"));
        type.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(type);

        PdfPCell time = new PdfPCell(new Paragraph("Time"));
        time.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(time);

        PdfPCell balanceCurrent = new PdfPCell(new Paragraph("Current Balance"));
        balanceCurrent.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(balanceCurrent);

        for (TransactionDTO transactionDTO: listTransaction){
            String dateFormat = UtilsTransaction.getStringDateFromLocalDateTime(transactionDTO.getDate());
            String hour = UtilsTransaction.getStringHourFromLocalDateTime(transactionDTO.getDate());

            PdfPCell transactionAmount = new PdfPCell(new Paragraph(transactionDTO.getType().name().equals("DEBIT")? "U$D -" + (NumberFormat.getNumberInstance(Locale.US).format(transactionDTO.getAmount())) : "U$D " + NumberFormat.getNumberInstance(Locale.US).format(transactionDTO.getAmount())));
            transactionAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionAmount.setVerticalAlignment(Element.ALIGN_MIDDLE);
            transactionAmount.setFixedHeight(40);
            table.addCell(transactionAmount);

            PdfPCell transactionDescription = new PdfPCell(new Paragraph(transactionDTO.getDescription()));
            transactionDescription.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionDescription.setVerticalAlignment(Element.ALIGN_MIDDLE);
            transactionDescription.setFixedHeight(40);
            table.addCell(transactionDescription);

            PdfPCell transactionDate = new PdfPCell(new Paragraph(dateFormat));
            transactionDate.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
            transactionDate.setFixedHeight(40);
            table.addCell(transactionDate);

            PdfPCell transactionType = new PdfPCell(new Paragraph(transactionDTO.getType().name()));
            transactionType.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionType.setVerticalAlignment(Element.ALIGN_MIDDLE);
            transactionType.setFixedHeight(40);
            table.addCell(transactionType);

            PdfPCell transactionTime = new PdfPCell(new Paragraph(hour));
            transactionTime.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionTime.setVerticalAlignment(Element.ALIGN_MIDDLE);
            transactionTime.setFixedHeight(40);
            table.addCell(transactionTime);

            PdfPCell currentBalance = new PdfPCell(new Paragraph("U$D " + NumberFormat.getNumberInstance(Locale.US).format(transactionDTO.getBalanceTotal())));
            currentBalance.setHorizontalAlignment(Element.ALIGN_CENTER);
            currentBalance.setVerticalAlignment(Element.ALIGN_MIDDLE);
            currentBalance.setFixedHeight(40);
            table.addCell(currentBalance);
        }
        pdfTransaction.add(table);
        pdfTransaction.close();

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
@Transactional
    @PostMapping("/api/clients/current/transaction")

    public ResponseEntity<Object> newTransaction(Authentication authentication
            , @RequestParam double amount, @RequestParam String description
            , @RequestParam String account1, @RequestParam String account2){

        Client selectClient = clientService.findByEmail(authentication.getName());
        Account selectAccount1 = accountService.findByNumber(account1.toUpperCase());
        Account selectAccount2 = accountService.findByNumber(account2.toUpperCase());

        if (amount < 1){
            return new ResponseEntity<>("You cannot send negative amounts", HttpStatus.FORBIDDEN);
        }
        if (description.isBlank()){
            return new ResponseEntity<>("Please add a description", HttpStatus.FORBIDDEN);
        }
        if (selectAccount1 == null){
            return new ResponseEntity<>("This account does not exist", HttpStatus.FORBIDDEN);
        }
        if (selectAccount1.equals(selectAccount2)){
            return new ResponseEntity<>("You cannot send money to the same account", HttpStatus.FORBIDDEN);
        }
        if (selectAccount2 == null){
            return new ResponseEntity<>("This account does not exist", HttpStatus.FORBIDDEN);
        }
        if (selectAccount1.getBalance() < amount){
            return new ResponseEntity<>("I do not have enough money", HttpStatus.FORBIDDEN);
        }
        if (selectClient.getAccounts().stream().filter(account -> account.getNumber().equalsIgnoreCase(account1)).collect(Collectors.toList()).size() == 0){
            return new ResponseEntity<>("This account is not yours", HttpStatus.FORBIDDEN);
        }

        selectAccount1.setBalance(selectAccount1.getBalance() - amount);
        selectAccount2.setBalance(selectAccount2.getBalance() + amount);


        Transaction newTransaction = new Transaction(TransactionType.DEBIT, amount, description, LocalDateTime.now(), true, selectAccount1.getBalance());
        selectAccount1.addTransaction(newTransaction);
        transactionService.saveTransaction(newTransaction);

        Transaction newTransactionCredit = new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now(), true, selectAccount2.getBalance());
        selectAccount2.addTransaction(newTransactionCredit);
        transactionService.saveTransaction(newTransactionCredit);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
