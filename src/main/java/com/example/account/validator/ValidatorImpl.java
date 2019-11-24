package com.example.account.validator;

import static com.example.exceptions.ErrorMessage.NO_MANDATORY_FIELD;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.example.exceptions.ErrorCode;
import com.example.exceptions.WalletException;

/**
 * Validator to check that mandatory parameters are present and some condition is TRUE.
 */
@Validated
@Component
public class ValidatorImpl implements Validator<String,String>{

    @Override
    public void validate(@NotNull Map<String, String> input, @NotNull List<String> required) throws WalletException {
        for (String parameter : required) {
            if (!input.containsKey(parameter) || (input.get(parameter) == null) || (input.get(parameter).isEmpty())) {
                String message = String.format(NO_MANDATORY_FIELD, parameter);
                throw new WalletException(message, ErrorCode.BadRequest.getCode());
            }
        }
    }

    @Override
    public void isTrue(@NotNull Boolean condition,@NotNull String errorMessage, int errorCode) throws WalletException{
        if(!condition){
            throw new WalletException(errorMessage, errorCode);
        }
    }
}
