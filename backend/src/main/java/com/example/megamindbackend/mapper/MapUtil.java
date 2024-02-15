package com.example.megamindbackend.mapper;

import java.util.Optional;
import java.util.function.Consumer;

public class MapUtil {

    public static <T> void map(Optional<T> optional, Consumer<T> consumer) {
        if (optional != null) {
            optional.ifPresentOrElse(consumer, () -> consumer.accept(null));
        }
    }
}
