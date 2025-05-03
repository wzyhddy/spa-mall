package com.net.sparrow.valid;

import com.net.sparrow.annotation.MaxMoney;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class MaxMoneyConstraintValidator implements ConstraintValidator<MaxMoney, BigDecimal> {

    private MaxMoney constraint;

    @Override
    public void initialize(MaxMoney constraint) {
        this.constraint = constraint;
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        return value != null && value.doubleValue() < constraint.value();
    }

}