package com.net.sparrow.annotation;

import com.net.sparrow.valid.MaxMoneyConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaxMoneyConstraintValidator.class)
public @interface MaxMoney {
    /**
     * message.
     *
     * @return
     */
    String message() default "{minMoney.message.error}";

    /**
     * max value.
     *
     * @return
     */
    double value() default 0;

    /**
     * group.
     *
     * @return
     */
    Class<?>[] groups() default {};

    /**
     * payload.
     *
     * @return
     */
    Class<? extends Payload>[] payload() default {};
}