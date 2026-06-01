package com.teamsits.pbs.utils;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UpdateUtils {

    public static <T> void updateIfDifferent(Supplier<T> oldVal, Supplier<T> newVal, Consumer<T> setter) {
        if (!Objects.equals(newVal.get(), oldVal.get())) {
            setter.accept(newVal.get());
        }
    }
}