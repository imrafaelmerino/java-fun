package fun.gen;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

final class GenCache {

    static final Map<String, Gen<?>> cache = new HashMap<>();

    private GenCache() {
    }

    static void put(String name,
                    Gen<?> gen) {
        synchronized (GenCache.class) {
            validateDoesntExist(requireNonNull(name));
            cache.put(name,
                      requireNonNull(gen));
        }
    }


    private static void validateDoesntExist(String name) {
        if (cache.containsKey(requireNonNull(name)))
            throw new IllegalArgumentException(String.format("The gen `%s` has already been created.",
                                                             name));
    }

    @SuppressWarnings({"unchecked","TypeParameterUnusedInFormals"})
    static <O extends Gen<?>> O get(String name) {
        O gen = (O) cache.get(requireNonNull(name));
        if (gen == null)
            throw new RuntimeException(String.format("The gen `%s` doesn't exist.",
                                                     name));
        return gen;
    }



}
