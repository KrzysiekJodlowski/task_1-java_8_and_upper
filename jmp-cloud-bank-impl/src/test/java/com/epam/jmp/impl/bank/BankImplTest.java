package com.epam.jmp.impl.bank;

import com.epam.jmp.bank.Bank;
import com.epam.jmp.dto.BankCardType;
import com.epam.jmp.dto.CreditBankCard;
import com.epam.jmp.dto.DebitBankCard;
import com.epam.jmp.dto.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class BankImplTest {

    Bank subject = new BankImpl();

    @Test
    void createBankCard_ShouldThrowNPE_WhengivenUserIsNull() {
        assertThrows(NullPointerException.class, () -> subject.createBankCard(null, BankCardType.CREDIT));
    }

    @Test
    void createBankCard_ShouldCreateCreditCard_WhenGivenCreditCardType() {
        var user = new User("Jennifer", "Lopez", LocalDate.of(1969, 7, 24));
        var expectedCreditLimit = 651.20d;

        var actual = subject.createBankCard(user, BankCardType.CREDIT);

        assertEquals(16, actual.getNumber().length());
        assertEquals(user, actual.getUser());
        assertEquals(expectedCreditLimit, ((CreditBankCard) actual).getCreditLimit());
    }

    @Test
    void createBankCard_ShouldCreateDebitCard_WhenGivenDebitCardType() {
        var user = new User("Taylor", "Swift", LocalDate.of(1989, 12, 13));
        var expectedCardBalance = 0d;

        var actual = subject.createBankCard(user, BankCardType.DEBIT);

        assertEquals(16, actual.getNumber().length());
        assertEquals(user, actual.getUser());
        assertEquals(expectedCardBalance, ((DebitBankCard) actual).getBalance());
    }
}
