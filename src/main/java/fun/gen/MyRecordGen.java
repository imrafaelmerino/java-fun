package fun.gen;


import fun.tuple.Pair;

import java.util.*;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;


/**
 * Generator for {@link MyRecord} values backed by named field generators.
 * <p>
 * Instances are immutable: configuration methods return a new generator preserving insertion order
 * of keys. A field can be:
 * <p>
 * - required or optional (presence in generated records)
 * <p>
 * - nullable or non-nullable (value may be {@code null} when present)
 * <p>
 * Typical usage is to define a record shape and then map generated records to a domain object.
 * <p>
 * Primary construction APIs are {@link #builder()}, {@link #ofEntries(Pair, Pair[])}, and
 * {@link #of(String, Gen, Object...)}.
 */
public final class MyRecordGen implements Gen<MyRecord> {

    private final static Supplier<Set<String>> EMPTY_SET_GEN = HashSet::new;
    private final SplitGen split;
    private final List<String> optionals;
    private final List<String> nullables;
    private final Map<String, Gen<?>> bindings;

    private MyRecordGen(Map<String, Gen<?>> bindings,
                        List<String> optionals,
                        List<String> nullables) {
        for (String key : optionals) {
            if (!bindings.containsKey(key))
                throw new IllegalArgumentException("optional '" + key + "' not defined in generator");
        }
        for (String key : nullables) {
            if (!bindings.containsKey(key))
                throw new IllegalArgumentException("nullable '" + key + "' not defined in generator");
        }
        this.optionals = optionals;
        this.nullables = nullables;
        this.bindings = bindings;
        this.split = SplitGen.DEFAULT;

    }


    private MyRecordGen(final Map<String, Gen<?>> bindings) {
        this(bindings,
             new ArrayList<>(),
             new ArrayList<>());
    }

    /**
     * Creates a fluent builder for record generator definitions.
     *
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a record generator from field entries preserving insertion order.
     *
     * @param first  first field entry
     * @param others optional additional field entries
     * @return a configured record generator
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static MyRecordGen ofEntries(final Pair<String, Gen<?>> first,
                                        final Pair<String, Gen<?>>... others) {
        requireNonNull(first);
        requireNonNull(others);
        Builder builder = builder().field(first.first(),
                                          first.second());
        for (Pair<String, Gen<?>> pair : others) {
            Pair<String, Gen<?>> nonNullPair = requireNonNull(pair);
            builder.field(nonNullPair.first(),
                          nonNullPair.second());
        }
        return builder.build();
    }

    /**
     * Creates an empty record generator.
     *
     * @return an empty generator equivalent to {@code builder().build()}
     */
    public static MyRecordGen of() {
        return builder().build();
    }

    /**
     * Creates a record generator from alternating key/generator pairs.
     * <p>
     * This compact form replaces the previous large overload family and keeps call sites concise.
     *
     * @param key                   first key
     * @param gen                   first key generator
     * @param additionalKeyGenPairs alternating key ({@link String}) and generator ({@link Gen}) pairs
     * @return configured record generator preserving pair insertion order
     * @throws NullPointerException     if any argument is {@code null}
     * @throws IllegalArgumentException if {@code additionalKeyGenPairs} length is odd or any element has invalid type
     */
    public static MyRecordGen of(final String key,
                                 final Gen<?> gen,
                                 final Object... additionalKeyGenPairs) {
        requireNonNull(key);
        requireNonNull(gen);
        requireNonNull(additionalKeyGenPairs);

        if (additionalKeyGenPairs.length % 2 != 0) {
            throw new IllegalArgumentException("additionalKeyGenPairs must contain an even number of elements: key, gen, ...");
        }

        Builder builder = builder().field(key,
                                          gen);

        for (int i = 0; i < additionalKeyGenPairs.length; i += 2) {
            Object rawKey = requireNonNull(additionalKeyGenPairs[i],
                                           "key at index " + i + " is null");
            Object rawGen = requireNonNull(additionalKeyGenPairs[i + 1],
                                           "generator at index " + (i + 1) + " is null");

            if (!(rawKey instanceof String entryKey)) {
                throw new IllegalArgumentException("Expected String key at index " + i + " but got " + rawKey.getClass().getName());
            }
            if (!(rawGen instanceof Gen<?> entryGen)) {
                throw new IllegalArgumentException("Expected Gen value at index " + (i + 1) + " but got " + rawGen.getClass().getName());
            }

            builder.field(entryKey,
                          entryGen);
        }

        return builder.build();
    }
    /**
     * Returns a brand new record generator with the same key-generators pairs as this instance and
     * the specified nullable keys. The value associated with a nullable key may or may not be null.
     *
     * @param nullables The nullable keys.
     * @return A brand new record generator.
     */
    public MyRecordGen withNullValues(final Collection<String> nullables) {
        return new MyRecordGen(bindings,
                               optionals,
                               new ArrayList<>(requireNonNull(nullables)));
    }

    /**
     * Returns a brand new record generator with the same key-generators pairs as this instance and
     * all keys nullable. The value associated with a nullable key may or may not be null.
     *
     * @return A brand new record generator.
     */
    public MyRecordGen withAllNullValues() {
        return new MyRecordGen(bindings,
                               optionals,
                               new ArrayList<>(bindings.keySet()));
    }

    /**
     * Returns a brand new record generator with the same key-generators pairs as this instance and
     * the specified nullable keys. The value associated with a nullable key may or may not be null.
     *
     * @param nullables The nullable keys.
     * @return A brand new record generator.
     */
    public MyRecordGen withNullValues(final String... nullables) {
        List<String> xs = new ArrayList<>();
        Collections.addAll(xs,
                           requireNonNull(nullables));
        return new MyRecordGen(bindings,
                               optionals,
                               xs);
    }

    /**
     * Returns a brand new record generator with the same key-generators pairs as this instance and
     * the specified optional keys. An optional key may or may not appear in the generated record objects.
     *
     * @param optionals The optional keys.
     * @return A brand new record generator.
     */
    public MyRecordGen withOptKeys(final Collection<String> optionals) {
        return new MyRecordGen(bindings,
                               new ArrayList<>(requireNonNull(optionals)),
                               nullables);
    }

    /**
     * Returns a brand new record generator with the same key-generators pairs as this instance and
     * the specified required keys. A required key must appear in the generated record.
     *
     * @param reqKeys The required keys.
     * @return A brand new record generator.
     */
    public MyRecordGen withReqKeys(final Collection<String> reqKeys) {
        List<String> optionalKeys = new ArrayList<>(bindings.keySet());
        optionalKeys.removeAll(requireNonNull(reqKeys));
        return new MyRecordGen(bindings,
                               optionalKeys,
                               nullables);
    }

    /**
     * Returns a brand new record generator with the same key-generators pairs as this instance and
     * the specified optional keys. An optional key may or may not appear in the generated record.
     *
     * @param optionals The optional keys.
     * @return A brand new record generator.
     */
    public MyRecordGen withOptKeys(final String... optionals) {
        List<String> xs = new ArrayList<>();
        Collections.addAll(xs,
                           requireNonNull(optionals));
        return new MyRecordGen(bindings,
                               xs,
                               nullables);
    }

    /**
     * Returns a brand new record generator with the same key-generators pairs as this instance and
     * the specified required keys. A required key must appear in the generated record.
     *
     * @param reqKeys The required keys.
     * @return A brand new record generator.
     */
    public MyRecordGen withReqKeys(final String... reqKeys) {
        List<String> keys = new ArrayList<>(bindings.keySet());
        for (String reqKey : reqKeys) keys.remove(reqKey);
        return new MyRecordGen(bindings,
                               keys,
                               nullables);
    }

    /**
     * Returns a brand new record generator with the same key-generators pairs as this instance and
     * all keys optional. An optional key may or may not appear in the generated records.
     *
     * @return A brand new record generator.
     */
    public MyRecordGen withAllOptKeys() {
        return new MyRecordGen(bindings,
                               new ArrayList<>(bindings.keySet()),
                               nullables);
    }

    /**
     * Fluent builder for {@link MyRecordGen}.
     * <p>
     * Builder state is mutable while building and converted to immutable {@link MyRecordGen}
     * configuration at {@link #build()}.
     */
    public static final class Builder {
        private final LinkedHashMap<String, Gen<?>> bindings = new LinkedHashMap<>();
        private final LinkedHashSet<String> optionalKeys = new LinkedHashSet<>();
        private final LinkedHashSet<String> nullableKeys = new LinkedHashSet<>();

        private Builder() {
        }

        /**
         * Adds or replaces a field generator.
         *
         * @param key field name
         * @param gen value generator
         * @return this builder
         */
        public Builder field(final String key,
                             final Gen<?> gen) {
            bindings.put(requireNonNull(key),
                         requireNonNull(gen));
            return this;
        }

        /**
         * Marks keys as optional.
         *
         * @param keys keys that may be absent from generated records
         * @return this builder
         */
        public Builder optional(final Collection<String> keys) {
            optionalKeys.addAll(requireNonNull(keys));
            return this;
        }

        /**
         * Marks keys as optional.
         *
         * @param keys keys that may be absent from generated records
         * @return this builder
         */
        public Builder optional(final String... keys) {
            optionalKeys.addAll(List.of(requireNonNull(keys)));
            return this;
        }

        /**
         * Marks keys as nullable.
         *
         * @param keys keys whose generated value may be {@code null}
         * @return this builder
         */
        public Builder nullable(final Collection<String> keys) {
            nullableKeys.addAll(requireNonNull(keys));
            return this;
        }

        /**
         * Marks keys as nullable.
         *
         * @param keys keys whose generated value may be {@code null}
         * @return this builder
         */
        public Builder nullable(final String... keys) {
            nullableKeys.addAll(List.of(requireNonNull(keys)));
            return this;
        }

        /**
         * Builds a {@link MyRecordGen} from this builder configuration.
         *
         * @return configured record generator
         */
        public MyRecordGen build() {
            return new MyRecordGen(new LinkedHashMap<>(bindings),
                                   new ArrayList<>(optionalKeys),
                                   new ArrayList<>(nullableKeys));
        }
    }

    /**
     * Sets a specific field and its corresponding generator for the generated records.
     *
     * @param key The name of the field to set.
     * @param gen The generator for the field's values.
     * @return A new {@code MyRecordGen} instance with the updated field binding.
     */
    public MyRecordGen set(final String key,
                           final Gen<?> gen) {
        LinkedHashMap<String, Gen<?>> b = new LinkedHashMap<>(bindings);
        b.put(key,
              gen);
        return new MyRecordGen(b,
                               optionals,
                               nullables);
    }

    /**
     * Builds a stateful supplier of {@link MyRecord} values from this configuration.
     *
     * @param random source of randomness used to derive all field suppliers
     * @return supplier of records honoring required/optional and nullable settings
     */
    @Override
    public Supplier<MyRecord> apply(final RandomGenerator random) {
        Objects.requireNonNull(random);
        var optionalFields =
                optionals.isEmpty() ?
                EMPTY_SET_GEN :
                new SubsetGen<>(optionals).filter(set -> !set.isEmpty())
                                          .apply(split.apply(random));

        var nullableFields =
                nullables.isEmpty() ?
                EMPTY_SET_GEN :
                new SubsetGen<>(nullables)
                        .filter(set -> !set.isEmpty())
                        .apply(split.apply(random));

        var isRemoveOpts = BoolGen.arbitrary()
                                  .apply(random);
        var isSetNullables = BoolGen.arbitrary()
                                    .apply(random);


        Map<String, Supplier<?>> map = new LinkedHashMap<>();
        for (var pair : bindings.entrySet()) {
            var value = pair.getValue();
            map.put(pair.getKey(),
                    value.apply(split.apply(random))
            );
        }


        return () -> {

            var nullFields = isSetNullables.get() ?
                             nullableFields.get() :
                             null;


            var optFields = isRemoveOpts.get() ?
                            optionalFields.get() :
                            null;


            Map<String, Object> result = new LinkedHashMap<>();

            for (var pair : map.entrySet()) {
                if (optFields == null || !optFields.contains(pair.getKey())) {
                    Object value =
                            nullFields != null && nullFields.contains(pair.getKey()) ?
                            null :
                            pair.getValue().get();
                    result.put(pair.getKey(),
                               value);
                }
            }

            return new MyRecord(result);
        };


    }

}
