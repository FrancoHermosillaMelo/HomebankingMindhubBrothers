package com.mindhub.homebanking.DTOS;

public class LoanApllicationDTO {
    private long loanID;
    private Double amount;
    private Integer payments;
    private String accountDestiny;

    public LoanApllicationDTO() {
    }

    public LoanApllicationDTO(long loanID, Double amount, Integer payments, String accountDestiny) {
        this.loanID = loanID;
        this.amount = amount;
        this.payments = payments;
        this.accountDestiny = accountDestiny;
    }

    public long getLoanID() {
        return loanID;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getAccountDestiny() {
        return accountDestiny;
    }

}
