package com.epam.jmp.app;

import com.epam.jmp.bank.Bank;
import com.epam.jmp.dto.BankCard;
import com.epam.jmp.dto.BankCardType;
import com.epam.jmp.dto.CreditBankCard;
import com.epam.jmp.dto.DebitBankCard;
import com.epam.jmp.dto.Subscription;
import com.epam.jmp.dto.User;
import com.epam.jmp.impl.bank.BankImpl;
import com.epam.jmp.impl.service.ServiceImpl;
import com.epam.jmp.service.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class App {

    private final Bank bank = new BankImpl();
    private final Service service = new ServiceImpl();

    private void generateUsers() {
        Map<String, LocalDate> inputUsers = Map.of(
                "Gandalf Grey", LocalDate.of(1937, 9, 21),
                "Luke Skywalker", LocalDate.of(1977, 5, 25),
                "Peter Quill", LocalDate.of(2014, 8, 1)
        );

        inputUsers.forEach((key, value) -> this.service.addUser(
                new User(key.split(" ")[0], key.split(" ")[1], value)
        ));
    }

    private List<BankCard> generateCardsForUsers() {
        return this.service.getAllUsers().stream()
                .filter(Service::isPayableUser)
                .map(user -> this.bank.createBankCard(user, user.getBirthday().isAfter(LocalDate.of(1970, 1, 1))? BankCardType.DEBIT : BankCardType.CREDIT))
                .toList();
    }

    private void subscribeBankCards(List<BankCard> bankCards) {
        bankCards.forEach(this.service::subscribe);
    }

    public static void main(String[] args) {
        App bankingApp = new App();

        System.out.println("\n********** users:");
        bankingApp.generateUsers();
        bankingApp.service.getAllUsers().forEach(
                user -> System.out.format("User: %s %s, born %s, is payable: %s\n", user.getName(), user.getSurname(), user.getBirthday(), Service.isPayableUser(user))
        );
        System.out.println("Average user age equals: " + bankingApp.service.getAverageUsersAge());

        System.out.println("********** cards:");
        var bankCards = bankingApp.generateCardsForUsers()
                .stream()
                .peek(card -> {
                    System.out.printf("Bank card:\n User: %s\n Number: %s\n ", card.getUser().getName(), card.getNumber());
                    if (card instanceof DebitBankCard) {
                        System.out.printf("Debit card balance: %1$,.2f\n", ((DebitBankCard) card).getBalance());
                    } else if (card instanceof CreditBankCard) {
                        System.out.printf("Credit card limit: %1$,.2f\n", ((CreditBankCard) card).getCreditLimit());
                    }
                }).collect(Collectors.toList());

        System.out.println("********** subscriptions:");
        bankingApp.subscribeBankCards(bankCards);
        bankCards.stream()
                .map(card -> bankingApp.service.getSubscriptionByBankCardNumber(card.getNumber()))
                .forEach(cardSubscription -> {
                    if (cardSubscription.isPresent()) {
                        var subscription = cardSubscription.get();
                        System.out.printf("Card number: %s\n Subscription date: %s\n", subscription.getBankcard(), subscription.getStartDate());
                    }
                });
        Predicate<Subscription> subscriptionStartsBeforeNewYear =
                subscription -> subscription.getStartDate().isBefore(LocalDate.of(2025, 1, 1));
        bankingApp.service.getAllSubscriptionsByCondition(subscriptionStartsBeforeNewYear)
                .forEach(subscription -> System.out.printf("Subscription was started before New Year for card number: %s\n", subscription.getBankcard()));
        System.out.println("**********");
    }
}