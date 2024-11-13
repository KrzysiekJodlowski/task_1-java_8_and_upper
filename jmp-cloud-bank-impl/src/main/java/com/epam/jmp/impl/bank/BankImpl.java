package com.epam.jmp.impl.bank;

import com.epam.jmp.dto.BankCard;
import com.epam.jmp.dto.BankCardType;
import com.epam.jmp.dto.CreditBankCard;
import com.epam.jmp.dto.DebitBankCard;
import com.epam.jmp.dto.User;
import com.epam.jmp.bank.Bank;

import java.time.LocalDate;
import java.time.Period;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BankImpl implements Bank {

    public static final int MATURITY_AGE = 18;
    public static final double CREDIT_LIMIT_PER_AGE = 17.6d;
    public static final double STARTING_DEBIT_CARD_BALANCE = 0d;
    private final Supplier<String> cardNumberSupplier = () -> new Random()
        .ints(16, 0, 9)
        .boxed()
        .map(String::valueOf)
        .collect(Collectors.joining(""));

    private final Map<BankCardType, CardTypeProperties> cardFactory = Map.of(
            BankCardType.CREDIT, new CardTypeProperties(CreditBankCard::new, this::calculateCreditCardLimit),
            BankCardType.DEBIT, new CardTypeProperties(DebitBankCard::new, ignoreUserBirthDate -> STARTING_DEBIT_CARD_BALANCE)
    );

    @Override
    public BankCard createBankCard(User user, BankCardType cardType) {
        Objects.requireNonNull(user, "provided user object is null");

        var cardTypeProperties = cardFactory.get(cardType);
        var cardMetric = cardTypeProperties.cardMetric().apply(user);

        return cardTypeProperties.cardCreator().createCard(
                cardNumberSupplier.get(),
                user,
                cardMetric
        );
    }

    private double calculateCreditCardLimit(User user) {
        var userBirthDate = user.getBirthday();
        var period = Period.between(LocalDate.now(), userBirthDate.plusYears(MATURITY_AGE));

        return Math.abs(period.getYears() * CREDIT_LIMIT_PER_AGE);
    }

    private record CardTypeProperties(BankCardCreator<String, User, Double, BankCard> cardCreator,
                                      Function<User, Double> cardMetric) { }

    @FunctionalInterface
    interface BankCardCreator<String, User, Double, BankCard> {
        BankCard createCard(String cardNumber, User user, Double cardMetric);
    }
}
