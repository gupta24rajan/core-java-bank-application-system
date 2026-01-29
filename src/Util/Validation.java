package Util;

import execptions.ValidationException;

@FunctionalInterface
public interface Validation<T> {
    void validation(T value) throws ValidationException;
}
