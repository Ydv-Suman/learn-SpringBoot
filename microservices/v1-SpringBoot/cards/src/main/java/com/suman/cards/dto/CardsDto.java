package com.suman.cards.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CardsDto {

    @NotEmpty(message = "mobile number can't be null or empty")
    @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile Number must be 10 digits")
    private String mobileNumber;

    @NotEmpty(message = "card number can't be null or empty")
    @Pattern(regexp="(^$|[0-9]{12})",message = "Mobile Number must be 10 digits")
    private String cardNumber;

    @NotEmpty(message = "card type can't be null or empty")
    private String cardType;

    @Positive(message = "total limit must be positive")
    private int totalLimit;

    @PositiveOrZero(message = "amount used must be positive or zero")
    private int amountUsed;

    @PositiveOrZero(message = "available amount must be positive or zero")
    private int availableAmount;
}
