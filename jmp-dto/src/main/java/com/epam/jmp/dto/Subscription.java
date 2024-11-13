package com.epam.jmp.dto;

import java.time.LocalDate;
import java.util.Objects;

public class Subscription {
    private String bankCard;
    private LocalDate startDate;

    public Subscription(String bankcard, LocalDate startDate) {
        this.bankCard = bankcard;
        this.startDate = startDate;
    }

    public String getBankcard() {
        return bankCard;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setBankcard(String bankCard) {
        this.bankCard = bankCard;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;

        return Objects.equals(bankCard, that.bankCard) && Objects.equals(startDate, that.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bankCard, startDate);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "bankcard='" + bankCard + '\'' +
                ", startDate=" + startDate +
                '}';
    }
}
