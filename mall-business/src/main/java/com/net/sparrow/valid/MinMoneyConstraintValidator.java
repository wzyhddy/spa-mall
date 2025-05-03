package com.net.sparrow.valid;

import com.net.sparrow.annotation.MinMoney;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class MinMoneyConstraintValidator implements ConstraintValidator<MinMoney, BigDecimal> {

    private MinMoney constraint;

    @Override
    public void initialize(MinMoney constraint) {
        this.constraint = constraint;
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        return value != null && value.doubleValue() >= constraint.value();
    }
}