package com.epam.jmp.impl.service;

import com.epam.jmp.dto.BankCard;
import com.epam.jmp.dto.Subscription;
import com.epam.jmp.dto.User;
import com.epam.jmp.service.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;


public class ServiceImpl implements Service {

    private final List<User> users = new ArrayList<>();
    private final List<Subscription> cardSubscriptions = new ArrayList<>();

    @Override
    public void addUser(User user) {
        Objects.requireNonNull(user, "provided user object is null");

        this.users.add(user);
    }

    @Override
    public List<User> getAllUsers() {
        return this.users.stream()
                .sorted(Comparator.comparing(User::getSurname)).toList();
    }

    @Override
    public void subscribe(BankCard bankCard) {
        Objects.requireNonNull(bankCard, "provided bank card object is null");

        var subscriptionStartDate = LocalDate.now();
        var newSubscription = new Subscription(bankCard.getNumber(), subscriptionStartDate);

        this.cardSubscriptions.add(newSubscription);
    }

    @Override
    public Optional<Subscription> getSubscriptionByBankCardNumber(String cardNumber) {
        Objects.requireNonNull(cardNumber, "provided card number object is null");

        return Optional.ofNullable(this.cardSubscriptions.stream()
                .filter(subscription -> subscription.getBankcard().equals(cardNumber))
                .findFirst()
                .orElseThrow(
                        () -> new SubscriptionNotFoundException(cardNumber + " subscription not found")
                ));

    }

    @Override
    public List<Subscription> getAllSubscriptionsByCondition(Predicate<Subscription> subscriptionPredicate) {
        Objects.requireNonNull(subscriptionPredicate, "provided subscription predicate is null");

        return this.cardSubscriptions.stream()
                .filter(subscriptionPredicate)
                .toList();
    }
}
