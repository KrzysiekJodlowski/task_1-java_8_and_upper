package com.epam.jmp.impl.service;

import com.epam.jmp.dto.CreditBankCard;
import com.epam.jmp.dto.Subscription;
import com.epam.jmp.dto.User;
import com.epam.jmp.service.Service;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.function.Predicate;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ServiceImplTest {

    Service subject = new ServiceImpl();

    @Test
    void createUser_ShouldThrowNPE_WhenUserIsNull() {
        assertThrows(NullPointerException.class, () -> subject.addUser(null));
    }

    @Test
    void getAllUsers_ShouldReturnTwoUsers_WhenTwoUsersAreAdded() {
        var userLeia = new User("Leia", "Organa", LocalDate.of(1956, 10, 21));
        var userHan = new User("Han", "Solo", LocalDate.of(1942, 7, 13));

        subject.addUser(userLeia);
        subject.addUser(userHan);

        assertEquals(2, subject.getAllUsers().size());
    }

    @Test
    void subscribe_ShouldThrowNPE_WhenBankCardIsNull() {
        assertThrows(NullPointerException.class, () -> subject.subscribe(null));
    }

    @Test
    void getSubscriptionByBankCardNumber_ShouldThrowNPE_WhenBankCardIsNull() {
        assertThrows(NullPointerException.class, () -> subject.getSubscriptionByBankCardNumber(null));
    }

    @Test
    void getSubscriptionByBankCardNumber_ShouldThrowSubscriptionNotFoundException_WhenBankCardIsAbsent() {
        assertThrows(SubscriptionNotFoundException.class, () -> subject.getSubscriptionByBankCardNumber("111122223333444455556666"));
    }

    @Test
    void getSubscriptionByBankCardNumber_ShouldReturnSubscription_WhenBankCardIsPresent() {
        var userLando = new User("Lando", "Calrissian", LocalDate.of(1937, 4, 6));
        var landosCard = new CreditBankCard("1111222233334444", userLando, 1_000_000d);

        subject.subscribe(landosCard);

        assertNotNull(subject.getSubscriptionByBankCardNumber("1111222233334444"));
    }

    @Test
    void getAllSubscriptionsByCondition_ShouldThrowNPE_WhenConditionIsNull() {
        assertThrows(NullPointerException.class, () -> subject.getAllSubscriptionsByCondition(null));
    }

    @Test
    void getAllSubscriptionsByCondition_ShouldReturnSubscription_WhenConditionIsMet() {
        var userObi = new User("Obi Wan", "Kenobi", LocalDate.of(1971, 3, 31));
        var obisCard = new CreditBankCard("1111111111111111", userObi, 1_000d);
        subject.subscribe(obisCard);

        Predicate<Subscription> subscriptionCardNumberStartsWithOne =
                subscription -> subscription.getBankcard().startsWith("1");

        assertNotNull(subject.getAllSubscriptionsByCondition(subscriptionCardNumberStartsWithOne));
    }

    @Test
    void getAverageUsersAge_ShouldReturnZero_WhenNoUsersAreAdded() {
        var actual = subject.getAverageUsersAge();

        assertEquals(0d, actual);
    }

    @Test
    void getAverageUsersAge_ShouldReturnUsersAge_WhenOneUserIsAdded() {
        var userChewie = new User("Chew", "Bacca", LocalDate.of(1944, 5, 19));
        subject.addUser(userChewie);

        var actual = subject.getAverageUsersAge();

        assertEquals((double) ChronoUnit.YEARS.between(userChewie.getBirthday(), LocalDate.now()), actual);
    }

    @Test
    void isPayableUser_ShouldThrowNPE_WhenUserIsNull() {
        assertThrows(NullPointerException.class, () -> Service.isPayableUser(null));
    }

    @Test
    void isPayableUser_ShouldReturnFalse_WhenUserIsImmature() {
        var userKai = new User("Kai", "Brightstar", LocalDate.of(2023, 5, 4));

        assertFalse(Service.isPayableUser(userKai));
    }

    @Test
    void isPayableUser_ShouldReturnTrue_WhenUserIsAdult() {
        var userKai = new User("Sheev", "Palpatine", LocalDate.of(1944, 8, 11));

        assertTrue(Service.isPayableUser(userKai));
    }
}
