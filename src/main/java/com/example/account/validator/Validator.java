package com.example.account.validator;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.example.exceptions.WalletException;

/**
 * Validator to validate input parameters and check that condition is TRUE.
 * @param <K>
 * @param <V>
 */
public interface Validator<K,V> {
    public void validate(@NotNull Map<K, V> input, @NotNull List<K> required) throws WalletException;
    public void isTrue(@NotNull Boolean condition, @NotNull String errorMessage, int errorCode) throws WalletException;


}
