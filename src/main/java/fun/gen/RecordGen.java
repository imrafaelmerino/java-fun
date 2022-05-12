package fun.gen;

import java.util.*;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;

import static java.util.Objects.requireNonNull;


/**
 * represent a generator of records.
 */
public final class RecordGen implements Gen<Map<String, ?>> {

    private final SplitGen split = SplitGen.DEFAULT;
    private List<String> optionals = new ArrayList<>();
    private List<String> nullables = new ArrayList<>();
    private Map<String, Gen<?>> bindings = new LinkedHashMap<>();

    private RecordGen(final Map<String, Gen<?>> bindings) {
        this.bindings = bindings;
    }

    @SuppressWarnings("squid:S00107")
    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1,
                      final String key2,
                      final Gen<?> gen2,
                      final String key3,
                      final Gen<?> gen3,
                      final String key4,
                      final Gen<?> gen4,
                      final String key5,
                      final Gen<?> gen5,
                      final String key6,
                      final Gen<?> gen6,
                      final String key7,
                      final Gen<?> gen7,
                      final String key8,
                      final Gen<?> gen8,
                      final String key9,
                      final Gen<?> gen9,
                      final String key10,
                      final Gen<?> gen10,
                      final String key11,
                      final Gen<?> gen11,
                      final String key12,
                      final Gen<?> gen12,
                      final String key13,
                      final Gen<?> gen13,
                      final String key14,
                      final Gen<?> gen14,
                      final String key15,
                      final Gen<?> gen15
    ) {
        this(key,
             gen,
             key1,
             gen1,
             key2,
             gen2,
             key3,
             gen3,
             key4,
             gen4,
             key5,
             gen5,
             key6,
             gen6,
             key7,
             gen7,
             key8,
             gen8,
             key9,
             gen9,
             key10,
             gen10,
             key11,
             gen11,
             key12,
             gen12,
             key13,
             gen13,
             key14,
             gen14
        );
        bindings.put(key15,
                     gen15
        );
    }

    @SuppressWarnings("squid:S00107")
    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1,
                      final String key2,
                      final Gen<?> gen2,
                      final String key3,
                      final Gen<?> gen3,
                      final String key4,
                      final Gen<?> gen4,
                      final String key5,
                      final Gen<?> gen5,
                      final String key6,
                      final Gen<?> gen6,
                      final String key7,
                      final Gen<?> gen7,
                      final String key8,
                      final Gen<?> gen8,
                      final String key9,
                      final Gen<?> gen9,
                      final String key10,
                      final Gen<?> gen10,
                      final String key11,
                      final Gen<?> gen11,
                      final String key12,
                      final Gen<?> gen12,
                      final String key13,
                      final Gen<?> gen13,
                      final String key14,
                      final Gen<?> gen14,
                      final String key15,
                      final Gen<?> gen15,
                      final String key16,
                      final Gen<?> gen16
    ) {
        this(key,
             gen,
             key1,
             gen1,
             key2,
             gen2,
             key3,
             gen3,
             key4,
             gen4,
             key5,
             gen5,
             key6,
             gen6,
             key7,
             gen7,
             key8,
             gen8,
             key9,
             gen9,
             key10,
             gen10,
             key11,
             gen11,
             key12,
             gen12,
             key13,
             gen13,
             key14,
             gen14,
             key15,
             gen15
        );
        bindings.put(key16,
                     gen16
        );
    }

    @SuppressWarnings("squid:S00107")
    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1,
                      final String key2,
                      final Gen<?> gen2,
                      final String key3,
                      final Gen<?> gen3,
                      final String key4,
                      final Gen<?> gen4,
                      final String key5,
                      final Gen<?> gen5,
                      final String key6,
                      final Gen<?> gen6,
                      final String key7,
                      final Gen<?> gen7,
                      final String key8,
                      final Gen<?> gen8,
                      final String key9,
                      final Gen<?> gen9,
                      final String key10,
                      final Gen<?> gen10,
                      final String key11,
                      final Gen<?> gen11,
                      final String key12,
                      final Gen<?> gen12,
                      final String key13,
                      final Gen<?> gen13,
                      final String key14,
                      final Gen<?> gen14,
                      final String key15,
                      final Gen<?> gen15,
                      final String key16,
                      final Gen<?> gen16,
                      final String key17,
                      final Gen<?> gen17
    ) {
        this(key,
             gen,
             key1,
             gen1,
             key2,
             gen2,
             key3,
             gen3,
             key4,
             gen4,
             key5,
             gen5,
             key6,
             gen6,
             key7,
             gen7,
             key8,
             gen8,
             key9,
             gen9,
             key10,
             gen10,
             key11,
             gen11,
             key12,
             gen12,
             key13,
             gen13,
             key14,
             gen14,
             key15,
             gen15,
             key16,
             gen16
        );
        bindings.put(key17,
                     gen17
        );
    }


    @SuppressWarnings("squid:S00107")
    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1,
                      final String key2,
                      final Gen<?> gen2,
                      final String key3,
                      final Gen<?> gen3,
                      final String key4,
                      final Gen<?> gen4,
                      final String key5,
                      final Gen<?> gen5,
                      final String key6,
                      final Gen<?> gen6,
                      final String key7,
                      final Gen<?> gen7,
                      final String key8,
                      final Gen<?> gen8,
                      final String key9,
                      final Gen<?> gen9,
                      final String key10,
                      final Gen<?> gen10,
                      final String key11,
                      final Gen<?> gen11,
                      final String key12,
                      final Gen<?> gen12,
                      final String key13,
                      final Gen<?> gen13,
                      final String key14,
                      final Gen<?> gen14,
                      final String key15,
                      final Gen<?> gen15,
                      final String key16,
                      final Gen<?> gen16,
                      final String key17,
                      final Gen<?> gen17,
                      final String key18,
                      final Gen<?> gen18
    ) {
        this(key,
             gen,
             key1,
             gen1,
             key2,
             gen2,
             key3,
             gen3,
             key4,
             gen4,
             key5,
             gen5,
             key6,
             gen6,
             key7,
             gen7,
             key8,
             gen8,
             key9,
             gen9,
             key10,
             gen10,
             key11,
             gen11,
             key12,
             gen12,
             key13,
             gen13,
             key14,
             gen14,
             key15,
             gen15,
             key16,
             gen16,
             key17,
             gen17
        );
        bindings.put(key18,
                     gen18
        );
    }


    @SuppressWarnings("squid:S00107")
    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1,
                      final String key2,
                      final Gen<?> gen2,
                      final String key3,
                      final Gen<?> gen3,
                      final String key4,
                      final Gen<?> gen4,
                      final String key5,
                      final Gen<?> gen5,
                      final String key6,
                      final Gen<?> gen6,
                      final String key7,
                      final Gen<?> gen7,
                      final String key8,
                      final Gen<?> gen8,
                      final String key9,
                      final Gen<?> gen9,
                      final String key10,
                      final Gen<?> gen10,
                      final String key11,
                      final Gen<?> gen11,
                      final String key12,
                      final Gen<?> gen12,
                      final String key13,
                      final Gen<?> gen13,
                      final String key14,
                      final Gen<?> gen14,
                      final String key15,
                      final Gen<?> gen15,
                      final String key16,
                      final Gen<?> gen16,
                      final String key17,
                      final Gen<?> gen17,
                      final String key18,
                      final Gen<?> gen18,
                      final String key19,
                      final Gen<?> gen19
    ) {
        this(key,
             gen,
             key1,
             gen1,
             key2,
             gen2,
             key3,
             gen3,
             key4,
             gen4,
             key5,
             gen5,
             key6,
             gen6,
             key7,
             gen7,
             key8,
             gen8,
             key9,
             gen9,
             key10,
             gen10,
             key11,
             gen11,
             key12,
             gen12,
             key13,
             gen13,
             key14,
             gen14,
             key15,
             gen15,
             key16,
             gen16,
             key17,
             gen17,
             key18,
             gen18
        );
        bindings.put(key19,
                     gen19
        );
    }

    @SuppressWarnings("squid:S00107")
    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1,
                      final String key2,
                      final Gen<?> gen2,
                      final String key3,
                      final Gen<?> gen3,
                      final String key4,
                      final Gen<?> gen4,
                      final String key5,
                      final Gen<?> gen5,
                      final String key6,
                      final Gen<?> gen6,
                      final String key7,
                      final Gen<?> gen7,
                      final String key8,
                      final Gen<?> gen8,
                      final String key9,
                      final Gen<?> gen9,
                      final String key10,
                      final Gen<?> gen10,
                      final String key11,
                      final Gen<?> gen11,
                      final String key12,
                      final Gen<?> gen12,
                      final String key13,
                      final Gen<?> gen13,
                      final String key14,
                      final Gen<?> gen14
    ) {
        this(key,
             gen,
             key1,
             gen1,
             key2,
             gen2,
             key3,
             gen3,
             key4,
             gen4,
             key5,
             gen5,
             key6,
             gen6,
             key7,
             gen7,
             key8,
             gen8,
             key9,
             gen9,
             key10,
             gen10,
             key11,
             gen11,
             key12,
             gen12,
             key13,
             gen13
        );
        bindings.put(key14,
                     gen14
        );
    }

    @SuppressWarnings("squid:S00107")
    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1,
                      final String key2,
                      final Gen<?> gen2,
                      final String key3,
                      final Gen<?> gen3,
                      final String key4,
                      final Gen<?> gen4,
                      final String key5,
                      final Gen<?> gen5,
                      final String key6,
                      final Gen<?> gen6,
                      final String key7,
                      final Gen<?> gen7,
                      final String key8,
                      final Gen<?> gen8,
                      final String key9,
                      final Gen<?> gen9,
                      final String key10,
                      final Gen<?> gen10,
                      final String key11,
                      final Gen<?> gen11,
                      final String key12,
                      final Gen<?> gen12,
                      final String key13,
                      final Gen<?> gen13
    ) {
        this(key,
             gen,
             key1,
             gen1,
             key2,
             gen2,
             key3,
             gen3,
             key4,
             gen4,
             key5,
             gen5,
             key6,
             gen6,
             key7,
             gen7,
             key8,
             gen8,
             key9,
             gen9,
             key10,
             gen10,
             key11,
             gen11,
             key12,
             gen12
        );
        bindings.put(key13,
                     gen13
        );
    }

    @SuppressWarnings("squid:S00107")
    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1,
                      final String key2,
                      final Gen<?> gen2,
                      final String key3,
                      final Gen<?> gen3,
                      final String key4,
                      final Gen<?> gen4,
                      final String key5,
                      final Gen<?> gen5,
                      final String key6,
                      final Gen<?> gen6,
                      final String key7,
                      final Gen<?> gen7,
                      final String key8,
                      final Gen<?> gen8,
                      final String key9,
                      final Gen<?> gen9,
                      final String key10,
                      final Gen<?> gen10,
                      final String key11,
                      final Gen<?> gen11,
                      final String key12,
                      final Gen<?> gen12
    ) {
        this(key,
             gen,
             key1,
             gen1,
             key2,
             gen2,
             key3,
             gen3,
             key4,
             gen4,
             key5,
             gen5,
             key6,
             gen6,
             key7,
             gen7,
             key8,
             gen8,
             key9,
             gen9,
             key10,
             gen10,
             key11,
             gen11
        );
        bindings.put(key12,
                     gen12
        );
    }

    @SuppressWarnings("squid:S00107")
    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1,
                      final String key2,
                      final Gen<?> gen2,
                      final String key3,
                      final Gen<?> gen3,
                      final String key4,
                      final Gen<?> gen4,
                      final String key5,
                      final Gen<?> gen5,
                      final String key6,
                      final Gen<?> gen6,
                      final String key7,
                      final Gen<?> gen7,
                      final String key8,
                      final Gen<?> gen8,
                      final String key9,
                      final Gen<?> gen9,
                      final String key10,
                      final Gen<?> gen10,
                      final String key11,
                      final Gen<?> gen11
    ) {
        this(key,
             gen,
             key1,
             gen1,
             key2,
             gen2,
             key3,
             gen3,
             key4,
             gen4,
             key5,
             gen5,
             key6,
             gen6,
             key7,
             gen7,
             key8,
             gen8,
             key9,
             gen9,
             key10,
             gen10
        );
        bindings.put(key11,
                     gen11
        );
    }

    @SuppressWarnings("squid:S00107")
    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1,
                      final String key2,
                      final Gen<?> gen2,
                      final String key3,
                      final Gen<?> gen3,
                      final String key4,
                      final Gen<?> gen4,
                      final String key5,
                      final Gen<?> gen5,
                      final String key6,
                      final Gen<?> gen6,
                      final String key7,
                      final Gen<?> gen7,
                      final String key8,
                      final Gen<?> gen8,
                      final String key9,
                      final Gen<?> gen9,
                      final String key10,
                      final Gen<?> gen10
    ) {
        this(key,
             gen,
             key1,
             gen1,
             key2,
             gen2,
             key3,
             gen3,
             key4,
             gen4,
             key5,
             gen5,
             key6,
             gen6,
             key7,
             gen7,
             key8,
             gen8,
             key9,
             gen9
        );
        bindings.put(key10,
                     gen10
        );
    }

    @SuppressWarnings("squid:S00107")
    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1,
                      final String key2,
                      final Gen<?> gen2,
                      final String key3,
                      final Gen<?> gen3,
                      final String key4,
                      final Gen<?> gen4,
                      final String key5,
                      final Gen<?> gen5,
                      final String key6,
                      final Gen<?> gen6,
                      final String key7,
                      final Gen<?> gen7,
                      final String key8,
                      final Gen<?> gen8,
                      final String key9,
                      final Gen<?> gen9
    ) {
        this(key,
             gen,
             key1,
             gen1,
             key2,
             gen2,
             key3,
             gen3,
             key4,
             gen4,
             key5,
             gen5,
             key6,
             gen6,
             key7,
             gen7,
             key8,
             gen8
        );
        bindings.put(key9,
                     gen9
        );
    }

    @SuppressWarnings("squid:S00107")
    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1,
                      final String key2,
                      final Gen<?> gen2,
                      final String key3,
                      final Gen<?> gen3,
                      final String key4,
                      final Gen<?> gen4,
                      final String key5,
                      final Gen<?> gen5,
                      final String key6,
                      final Gen<?> gen6,
                      final String key7,
                      final Gen<?> gen7,
                      final String key8,
                      final Gen<?> gen8
    ) {
        this(key,
             gen,
             key1,
             gen1,
             key2,
             gen2,
             key3,
             gen3,
             key4,
             gen4,
             key5,
             gen5,
             key6,
             gen6,
             key7,
             gen7
        );
        bindings.put(key8,
                     gen8
        );
    }

    @SuppressWarnings("squid:S00107")
    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1,
                      final String key2,
                      final Gen<?> gen2,
                      final String key3,
                      final Gen<?> gen3,
                      final String key4,
                      final Gen<?> gen4,
                      final String key5,
                      final Gen<?> gen5,
                      final String key6,
                      final Gen<?> gen6,
                      final String key7,
                      final Gen<?> gen7
    ) {
        this(key,
             gen,
             key1,
             gen1,
             key2,
             gen2,
             key3,
             gen3,
             key4,
             gen4,
             key5,
             gen5,
             key6,
             gen6
        );
        bindings.put(key7,
                     gen7
        );
    }

    @SuppressWarnings("squid:S00107")
    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1,
                      final String key2,
                      final Gen<?> gen2,
                      final String key3,
                      final Gen<?> gen3,
                      final String key4,
                      final Gen<?> gen4,
                      final String key5,
                      final Gen<?> gen5,
                      final String key6,
                      final Gen<?> gen6
    ) {
        this(key,
             gen,
             key1,
             gen1,
             key2,
             gen2,
             key3,
             gen3,
             key4,
             gen4,
             key5,
             gen5
        );
        bindings.put(key6,
                     gen6
        );
    }

    @SuppressWarnings("squid:S00107")
    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1,
                      final String key2,
                      final Gen<?> gen2,
                      final String key3,
                      final Gen<?> gen3,
                      final String key4,
                      final Gen<?> gen4,
                      final String key5,
                      final Gen<?> gen5
    ) {
        this(key,
             gen,
             key1,
             gen1,
             key2,
             gen2,
             key3,
             gen3,
             key4,
             gen4
        );
        bindings.put(key5,
                     gen5
        );
    }

    @SuppressWarnings("squid:S00107")
    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1,
                      final String key2,
                      final Gen<?> gen2,
                      final String key3,
                      final Gen<?> gen3,
                      final String key4,
                      final Gen<?> gen4
    ) {
        this(key,
             gen,
             key1,
             gen1,
             key2,
             gen2,
             key3,
             gen3
        );
        bindings.put(key4,
                     gen4
        );
    }

    @SuppressWarnings("squid:S00107")
    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1,
                      final String key2,
                      final Gen<?> gen2,
                      final String key3,
                      final Gen<?> gen3
    ) {
        this(key,
             gen,
             key1,
             gen1,
             key2,
             gen2
        );
        bindings.put(key3,
                     gen3
        );
    }

    @SuppressWarnings("squid:S00107")
    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1,
                      final String key2,
                      final Gen<?> gen2
    ) {
        this(key,
             gen,
             key1,
             gen1
        );
        bindings.put(key2,
                     gen2
        );
    }

    private RecordGen(final String key,
                      final Gen<?> gen,
                      final String key1,
                      final Gen<?> gen1
    ) {
        this(key,
             gen
        );
        bindings.put(key1,
                     gen1
        );
    }

    private RecordGen(final String key,
                      final Gen<?> gen
    ) {
        bindings.put(key,
                     gen
        );
    }

    public static RecordGen of(final String key,
                               final Gen<?> gen
    ) {
        return new RecordGen(key,
                             gen
        );
    }

    public static RecordGen of(final String key,
                               final Gen<?> gen,
                               final String key1,
                               final Gen<?> gen1
    ) {
        return new RecordGen(key,
                             gen,
                             key1,
                             gen1
        );
    }

    public static RecordGen of(final String key,
                               final Gen<?> gen,
                               final String key1,
                               final Gen<?> gen1,
                               final String key2,
                               final Gen<?> gen2
    ) {
        return new RecordGen(key,
                             gen,
                             key1,
                             gen1,
                             key2,
                             gen2
        );
    }

    @SuppressWarnings("squid:S00107")
    public static RecordGen of(final String key,
                               final Gen<?> gen,
                               final String key1,
                               final Gen<?> gen1,
                               final String key2,
                               final Gen<?> gen2,
                               final String key3,
                               final Gen<?> gen3
    ) {
        return new RecordGen(key,
                             gen,
                             key1,
                             gen1,
                             key2,
                             gen2,
                             key3,
                             gen3
        );
    }

    @SuppressWarnings("squid:S00107")
    public static RecordGen of(final String key,
                               final Gen<?> gen,
                               final String key1,
                               final Gen<?> gen1,
                               final String key2,
                               final Gen<?> gen2,
                               final String key3,
                               final Gen<?> gen3,
                               final String key4,
                               final Gen<?> gen4
    ) {
        return new RecordGen(key,
                             gen,
                             key1,
                             gen1,
                             key2,
                             gen2,
                             key3,
                             gen3,
                             key4,
                             gen4
        );
    }

    @SuppressWarnings("squid:S00107")
    public static RecordGen of(final String key,
                               final Gen<?> gen,
                               final String key1,
                               final Gen<?> gen1,
                               final String key2,
                               final Gen<?> gen2,
                               final String key3,
                               final Gen<?> gen3,
                               final String key4,
                               final Gen<?> gen4,
                               final String key5,
                               final Gen<?> gen5
    ) {
        return new RecordGen(key,
                             gen,
                             key1,
                             gen1,
                             key2,
                             gen2,
                             key3,
                             gen3,
                             key4,
                             gen4,
                             key5,
                             gen5
        );
    }

    @SuppressWarnings("squid:S00107")
    public static RecordGen of(final String key,
                               final Gen<?> gen,
                               final String key1,
                               final Gen<?> gen1,
                               final String key2,
                               final Gen<?> gen2,
                               final String key3,
                               final Gen<?> gen3,
                               final String key4,
                               final Gen<?> gen4,
                               final String key5,
                               final Gen<?> gen5,
                               final String key6,
                               final Gen<?> gen6
    ) {
        return new RecordGen(key,
                             gen,
                             key1,
                             gen1,
                             key2,
                             gen2,
                             key3,
                             gen3,
                             key4,
                             gen4,
                             key5,
                             gen5,
                             key6,
                             gen6
        );
    }

    @SuppressWarnings("squid:S00107")
    public static RecordGen of(final String key,
                               final Gen<?> gen,
                               final String key1,
                               final Gen<?> gen1,
                               final String key2,
                               final Gen<?> gen2,
                               final String key3,
                               final Gen<?> gen3,
                               final String key4,
                               final Gen<?> gen4,
                               final String key5,
                               final Gen<?> gen5,
                               final String key6,
                               final Gen<?> gen6,
                               final String key7,
                               final Gen<?> gen7
    ) {
        return new RecordGen(key,
                             gen,
                             key1,
                             gen1,
                             key2,
                             gen2,
                             key3,
                             gen3,
                             key4,
                             gen4,
                             key5,
                             gen5,
                             key6,
                             gen6,
                             key7,
                             gen7
        );

    }

    @SuppressWarnings("squid:S00107")
    public static RecordGen of(final String key,
                               final Gen<?> gen,
                               final String key1,
                               final Gen<?> gen1,
                               final String key2,
                               final Gen<?> gen2,
                               final String key3,
                               final Gen<?> gen3,
                               final String key4,
                               final Gen<?> gen4,
                               final String key5,
                               final Gen<?> gen5,
                               final String key6,
                               final Gen<?> gen6,
                               final String key7,
                               final Gen<?> gen7,
                               final String key8,
                               final Gen<?> gen8
    ) {
        return new RecordGen(key,
                             gen,
                             key1,
                             gen1,
                             key2,
                             gen2,
                             key3,
                             gen3,
                             key4,
                             gen4,
                             key5,
                             gen5,
                             key6,
                             gen6,
                             key7,
                             gen7,
                             key8,
                             gen8
        );
    }

    /**
     * static factory method to create a JsObGen of ten mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @return a MapGen
     */
    @SuppressWarnings("squid:S00107")
    public static RecordGen of(final String key1,
                               final Gen<?> gen1,
                               final String key2,
                               final Gen<?> gen2,
                               final String key3,
                               final Gen<?> gen3,
                               final String key4,
                               final Gen<?> gen4,
                               final String key5,
                               final Gen<?> gen5,
                               final String key6,
                               final Gen<?> gen6,
                               final String key7,
                               final Gen<?> gen7,
                               final String key8,
                               final Gen<?> gen8,
                               final String key9,
                               final Gen<?> gen9,
                               final String key10,
                               final Gen<?> gen10
    ) {
        return new RecordGen(key1,
                             gen1,
                             key2,
                             gen2,
                             key3,
                             gen3,
                             key4,
                             gen4,
                             key5,
                             gen5,
                             key6,
                             gen6,
                             key7,
                             gen7,
                             key8,
                             gen8,
                             key9,
                             gen9,
                             key10,
                             gen10
        );
    }

    /**
     * static factory method to create a JsObGen of eleven mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @return a MapGen
     */
    @SuppressWarnings("squid:S00107")
    public static RecordGen of(final String key1,
                               final Gen<?> gen1,
                               final String key2,
                               final Gen<?> gen2,
                               final String key3,
                               final Gen<?> gen3,
                               final String key4,
                               final Gen<?> gen4,
                               final String key5,
                               final Gen<?> gen5,
                               final String key6,
                               final Gen<?> gen6,
                               final String key7,
                               final Gen<?> gen7,
                               final String key8,
                               final Gen<?> gen8,
                               final String key9,
                               final Gen<?> gen9,
                               final String key10,
                               final Gen<?> gen10,
                               final String key11,
                               final Gen<?> gen11
    ) {
        return new RecordGen(key1,
                             gen1,
                             key2,
                             gen2,
                             key3,
                             gen3,
                             key4,
                             gen4,
                             key5,
                             gen5,
                             key6,
                             gen6,
                             key7,
                             gen7,
                             key8,
                             gen8,
                             key9,
                             gen9,
                             key10,
                             gen10,
                             key11,
                             gen11
        );
    }

    /**
     * static factory method to create a JsObGen of twelve mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param gen12 the mapping associated to the twelfth key,
     * @return a MapGen
     */
    @SuppressWarnings("squid:S00107")
    public static RecordGen of(final String key1,
                               final Gen<?> gen1,
                               final String key2,
                               final Gen<?> gen2,
                               final String key3,
                               final Gen<?> gen3,
                               final String key4,
                               final Gen<?> gen4,
                               final String key5,
                               final Gen<?> gen5,
                               final String key6,
                               final Gen<?> gen6,
                               final String key7,
                               final Gen<?> gen7,
                               final String key8,
                               final Gen<?> gen8,
                               final String key9,
                               final Gen<?> gen9,
                               final String key10,
                               final Gen<?> gen10,
                               final String key11,
                               final Gen<?> gen11,
                               final String key12,
                               final Gen<?> gen12
    ) {
        return new RecordGen(key1,
                             gen1,
                             key2,
                             gen2,
                             key3,
                             gen3,
                             key4,
                             gen4,
                             key5,
                             gen5,
                             key6,
                             gen6,
                             key7,
                             gen7,
                             key8,
                             gen8,
                             key9,
                             gen9,
                             key10,
                             gen10,
                             key11,
                             gen11,
                             key12,
                             gen12
        );
    }

    /**
     * static factory method to create a JsObGen of thirteen mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param gen12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param gen13 the mapping associated to the thirteenth key
     * @return a MapGen
     */
    @SuppressWarnings("squid:S00107")
    public static RecordGen of(final String key1,
                               final Gen<?> gen1,
                               final String key2,
                               final Gen<?> gen2,
                               final String key3,
                               final Gen<?> gen3,
                               final String key4,
                               final Gen<?> gen4,
                               final String key5,
                               final Gen<?> gen5,
                               final String key6,
                               final Gen<?> gen6,
                               final String key7,
                               final Gen<?> gen7,
                               final String key8,
                               final Gen<?> gen8,
                               final String key9,
                               final Gen<?> gen9,
                               final String key10,
                               final Gen<?> gen10,
                               final String key11,
                               final Gen<?> gen11,
                               final String key12,
                               final Gen<?> gen12,
                               final String key13,
                               final Gen<?> gen13
    ) {
        return new RecordGen(key1,
                             gen1,
                             key2,
                             gen2,
                             key3,
                             gen3,
                             key4,
                             gen4,
                             key5,
                             gen5,
                             key6,
                             gen6,
                             key7,
                             gen7,
                             key8,
                             gen8,
                             key9,
                             gen9,
                             key10,
                             gen10,
                             key11,
                             gen11,
                             key12,
                             gen12,
                             key13,
                             gen13
        );
    }

    /**
     * static factory method to create a JsObGen of fourteen mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param gen12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param gen13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param gen14 the mapping associated to the fourteenth key
     * @return a MapGen
     */
    @SuppressWarnings("squid:S00107")
    public static RecordGen of(final String key1,
                               final Gen<?> gen1,
                               final String key2,
                               final Gen<?> gen2,
                               final String key3,
                               final Gen<?> gen3,
                               final String key4,
                               final Gen<?> gen4,
                               final String key5,
                               final Gen<?> gen5,
                               final String key6,
                               final Gen<?> gen6,
                               final String key7,
                               final Gen<?> gen7,
                               final String key8,
                               final Gen<?> gen8,
                               final String key9,
                               final Gen<?> gen9,
                               final String key10,
                               final Gen<?> gen10,
                               final String key11,
                               final Gen<?> gen11,
                               final String key12,
                               final Gen<?> gen12,
                               final String key13,
                               final Gen<?> gen13,
                               final String key14,
                               final Gen<?> gen14
    ) {
        return new RecordGen(key1,
                             gen1,
                             key2,
                             gen2,
                             key3,
                             gen3,
                             key4,
                             gen4,
                             key5,
                             gen5,
                             key6,
                             gen6,
                             key7,
                             gen7,
                             key8,
                             gen8,
                             key9,
                             gen9,
                             key10,
                             gen10,
                             key11,
                             gen11,
                             key12,
                             gen12,
                             key13,
                             gen13,
                             key14,
                             gen14
        );
    }

    /**
     * static factory method to create a JsObGen of fifteen mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param gen12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param gen13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param gen14 the mapping associated to the fourteenth key
     * @param key15 the fifteenth key
     * @param gen15 the mapping associated to the fifteenth key
     * @return a MapGen
     */
    @SuppressWarnings("squid:S00107")
    public static RecordGen of(final String key1,
                               final Gen<?> gen1,
                               final String key2,
                               final Gen<?> gen2,
                               final String key3,
                               final Gen<?> gen3,
                               final String key4,
                               final Gen<?> gen4,
                               final String key5,
                               final Gen<?> gen5,
                               final String key6,
                               final Gen<?> gen6,
                               final String key7,
                               final Gen<?> gen7,
                               final String key8,
                               final Gen<?> gen8,
                               final String key9,
                               final Gen<?> gen9,
                               final String key10,
                               final Gen<?> gen10,
                               final String key11,
                               final Gen<?> gen11,
                               final String key12,
                               final Gen<?> gen12,
                               final String key13,
                               final Gen<?> gen13,
                               final String key14,
                               final Gen<?> gen14,
                               final String key15,
                               final Gen<?> gen15
    ) {
        return new RecordGen(key1,
                             gen1,
                             key2,
                             gen2,
                             key3,
                             gen3,
                             key4,
                             gen4,
                             key5,
                             gen5,
                             key6,
                             gen6,
                             key7,
                             gen7,
                             key8,
                             gen8,
                             key9,
                             gen9,
                             key10,
                             gen10,
                             key11,
                             gen11,
                             key12,
                             gen12,
                             key13,
                             gen13,
                             key14,
                             gen14,
                             key15,
                             gen15
        );
    }

    /**
     * static factory method to create a JsObGen of sixteen mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param gen12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param gen13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param gen14 the mapping associated to the fourteenth key
     * @param key15 the fifteenth key
     * @param gen15 the mapping associated to the fifteenth key
     * @param key16 the sixteenth key
     * @param gen16 the mapping associated to the sixteenth key
     * @return a MapGen
     */
    @SuppressWarnings("squid:S00107")
    public static RecordGen of(final String key1,
                               final Gen<?> gen1,
                               final String key2,
                               final Gen<?> gen2,
                               final String key3,
                               final Gen<?> gen3,
                               final String key4,
                               final Gen<?> gen4,
                               final String key5,
                               final Gen<?> gen5,
                               final String key6,
                               final Gen<?> gen6,
                               final String key7,
                               final Gen<?> gen7,
                               final String key8,
                               final Gen<?> gen8,
                               final String key9,
                               final Gen<?> gen9,
                               final String key10,
                               final Gen<?> gen10,
                               final String key11,
                               final Gen<?> gen11,
                               final String key12,
                               final Gen<?> gen12,
                               final String key13,
                               final Gen<?> gen13,
                               final String key14,
                               final Gen<?> gen14,
                               final String key15,
                               final Gen<?> gen15,
                               final String key16,
                               final Gen<?> gen16
    ) {
        return new RecordGen(key1,
                             gen1,
                             key2,
                             gen2,
                             key3,
                             gen3,
                             key4,
                             gen4,
                             key5,
                             gen5,
                             key6,
                             gen6,
                             key7,
                             gen7,
                             key8,
                             gen8,
                             key9,
                             gen9,
                             key10,
                             gen10,
                             key11,
                             gen11,
                             key12,
                             gen12,
                             key13,
                             gen13,
                             key14,
                             gen14,
                             key15,
                             gen15,
                             key16,
                             gen16
        );
    }

    /**
     * static factory method to create a JsObGen of seventeen mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param gen12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param gen13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param gen14 the mapping associated to the fourteenth key
     * @param key15 the fifteenth key
     * @param gen15 the mapping associated to the fifteenth key
     * @param key16 the sixteenth key
     * @param gen16 the mapping associated to the sixteenth key
     * @param key17 the seventeenth key
     * @param gen17 the mapping associated to the seventeenth key
     * @return a MapGen
     */
    @SuppressWarnings("squid:S00107")
    public static RecordGen of(final String key1,
                               final Gen<?> gen1,
                               final String key2,
                               final Gen<?> gen2,
                               final String key3,
                               final Gen<?> gen3,
                               final String key4,
                               final Gen<?> gen4,
                               final String key5,
                               final Gen<?> gen5,
                               final String key6,
                               final Gen<?> gen6,
                               final String key7,
                               final Gen<?> gen7,
                               final String key8,
                               final Gen<?> gen8,
                               final String key9,
                               final Gen<?> gen9,
                               final String key10,
                               final Gen<?> gen10,
                               final String key11,
                               final Gen<?> gen11,
                               final String key12,
                               final Gen<?> gen12,
                               final String key13,
                               final Gen<?> gen13,
                               final String key14,
                               final Gen<?> gen14,
                               final String key15,
                               final Gen<?> gen15,
                               final String key16,
                               final Gen<?> gen16,
                               final String key17,
                               final Gen<?> gen17
    ) {
        return new RecordGen(key1,
                             gen1,
                             key2,
                             gen2,
                             key3,
                             gen3,
                             key4,
                             gen4,
                             key5,
                             gen5,
                             key6,
                             gen6,
                             key7,
                             gen7,
                             key8,
                             gen8,
                             key9,
                             gen9,
                             key10,
                             gen10,
                             key11,
                             gen11,
                             key12,
                             gen12,
                             key13,
                             gen13,
                             key14,
                             gen14,
                             key15,
                             gen15,
                             key16,
                             gen16,
                             key17,
                             gen17
        );
    }

    /**
     * static factory method to create a JsObGen of eighteen mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param gen12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param gen13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param gen14 the mapping associated to the fourteenth key
     * @param key15 the fifteenth key
     * @param gen15 the mapping associated to the fifteenth key
     * @param key16 the sixteenth key
     * @param gen16 the mapping associated to the sixteenth key
     * @param key17 the seventeenth key
     * @param gen17 the mapping associated to the seventeenth key
     * @param key18 the eighteenth key
     * @param gen18 the mapping associated to the eighteenth key
     * @return a MapGen
     */
    @SuppressWarnings("squid:S00107")
    public static RecordGen of(final String key1,
                               final Gen<?> gen1,
                               final String key2,
                               final Gen<?> gen2,
                               final String key3,
                               final Gen<?> gen3,
                               final String key4,
                               final Gen<?> gen4,
                               final String key5,
                               final Gen<?> gen5,
                               final String key6,
                               final Gen<?> gen6,
                               final String key7,
                               final Gen<?> gen7,
                               final String key8,
                               final Gen<?> gen8,
                               final String key9,
                               final Gen<?> gen9,
                               final String key10,
                               final Gen<?> gen10,
                               final String key11,
                               final Gen<?> gen11,
                               final String key12,
                               final Gen<?> gen12,
                               final String key13,
                               final Gen<?> gen13,
                               final String key14,
                               final Gen<?> gen14,
                               final String key15,
                               final Gen<?> gen15,
                               final String key16,
                               final Gen<?> gen16,
                               final String key17,
                               final Gen<?> gen17,
                               final String key18,
                               final Gen<?> gen18
    ) {
        return new RecordGen(key1,
                             gen1,
                             key2,
                             gen2,
                             key3,
                             gen3,
                             key4,
                             gen4,
                             key5,
                             gen5,
                             key6,
                             gen6,
                             key7,
                             gen7,
                             key8,
                             gen8,
                             key9,
                             gen9,
                             key10,
                             gen10,
                             key11,
                             gen11,
                             key12,
                             gen12,
                             key13,
                             gen13,
                             key14,
                             gen14,
                             key15,
                             gen15,
                             key16,
                             gen16,
                             key17,
                             gen17,
                             key18,
                             gen18
        );
    }

    /**
     * static factory method to create a JsObGen of nineteen mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param gen12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param gen13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param gen14 the mapping associated to the fourteenth key
     * @param key15 the fifteenth key
     * @param gen15 the mapping associated to the fifteenth key
     * @param key16 the sixteenth key
     * @param gen16 the mapping associated to the sixteenth key
     * @param key17 the seventeenth key
     * @param gen17 the mapping associated to the seventeenth key
     * @param key18 the eighteenth key
     * @param gen18 the mapping associated to the eighteenth key
     * @param key19 the nineteenth key
     * @param gen19 the mapping associated to the nineteenth key
     * @return a MapGen
     */
    @SuppressWarnings("squid:S00107")
    public static RecordGen of(final String key1,
                               final Gen<?> gen1,
                               final String key2,
                               final Gen<?> gen2,
                               final String key3,
                               final Gen<?> gen3,
                               final String key4,
                               final Gen<?> gen4,
                               final String key5,
                               final Gen<?> gen5,
                               final String key6,
                               final Gen<?> gen6,
                               final String key7,
                               final Gen<?> gen7,
                               final String key8,
                               final Gen<?> gen8,
                               final String key9,
                               final Gen<?> gen9,
                               final String key10,
                               final Gen<?> gen10,
                               final String key11,
                               final Gen<?> gen11,
                               final String key12,
                               final Gen<?> gen12,
                               final String key13,
                               final Gen<?> gen13,
                               final String key14,
                               final Gen<?> gen14,
                               final String key15,
                               final Gen<?> gen15,
                               final String key16,
                               final Gen<?> gen16,
                               final String key17,
                               final Gen<?> gen17,
                               final String key18,
                               final Gen<?> gen18,
                               final String key19,
                               final Gen<?> gen19
    ) {
        return new RecordGen(key1,
                             gen1,
                             key2,
                             gen2,
                             key3,
                             gen3,
                             key4,
                             gen4,
                             key5,
                             gen5,
                             key6,
                             gen6,
                             key7,
                             gen7,
                             key8,
                             gen8,
                             key9,
                             gen9,
                             key10,
                             gen10,
                             key11,
                             gen11,
                             key12,
                             gen12,
                             key13,
                             gen13,
                             key14,
                             gen14,
                             key15,
                             gen15,
                             key16,
                             gen16,
                             key17,
                             gen17,
                             key18,
                             gen18,
                             key19,
                             gen19
        );
    }

    /**
     * static factory method to create a JsObGen of twenty mappings
     *
     * @param key1  the first key
     * @param gen1  the mapping associated to the first key
     * @param key2  the second key
     * @param gen2  the mapping associated to the second key
     * @param key3  the third key
     * @param gen3  the mapping associated to the third key
     * @param key4  the fourth key
     * @param gen4  the mapping associated to the fourth key
     * @param key5  the fifth key
     * @param gen5  the mapping associated to the fifth key
     * @param key6  the sixth key
     * @param gen6  the mapping associated to the sixth key
     * @param key7  the seventh key
     * @param gen7  the mapping associated to the seventh key
     * @param key8  the eighth key
     * @param gen8  the mapping associated to the eighth key
     * @param key9  the ninth key
     * @param gen9  the mapping associated to the ninth key
     * @param key10 the tenth key
     * @param gen10 the mapping associated to the eleventh key
     * @param key11 the eleventh key
     * @param gen11 the mapping associated to the eleventh key
     * @param key12 the twelfth key
     * @param gen12 the mapping associated to the twelfth key,
     * @param key13 the thirteenth key
     * @param gen13 the mapping associated to the thirteenth key
     * @param key14 the fourteenth key
     * @param gen14 the mapping associated to the fourteenth key
     * @param key15 the fifteenth key
     * @param gen15 the mapping associated to the fifteenth key
     * @param key16 the sixteenth key
     * @param gen16 the mapping associated to the sixteenth key
     * @param key17 the seventeenth key
     * @param gen17 the mapping associated to the seventeenth key
     * @param key18 the eighteenth key
     * @param gen18 the mapping associated to the eighteenth key
     * @param key19 the nineteenth key
     * @param gen19 the mapping associated to the nineteenth key
     * @param key20 the twentieth key
     * @param gen20 the mapping associated to the twentieth key
     * @return a MapGen
     */
    @SuppressWarnings("squid:S00107")
    public static RecordGen of(final String key1,
                               final Gen<?> gen1,
                               final String key2,
                               final Gen<?> gen2,
                               final String key3,
                               final Gen<?> gen3,
                               final String key4,
                               final Gen<?> gen4,
                               final String key5,
                               final Gen<?> gen5,
                               final String key6,
                               final Gen<?> gen6,
                               final String key7,
                               final Gen<?> gen7,
                               final String key8,
                               final Gen<?> gen8,
                               final String key9,
                               final Gen<?> gen9,
                               final String key10,
                               final Gen<?> gen10,
                               final String key11,
                               final Gen<?> gen11,
                               final String key12,
                               final Gen<?> gen12,
                               final String key13,
                               final Gen<?> gen13,
                               final String key14,
                               final Gen<?> gen14,
                               final String key15,
                               final Gen<?> gen15,
                               final String key16,
                               final Gen<?> gen16,
                               final String key17,
                               final Gen<?> gen17,
                               final String key18,
                               final Gen<?> gen18,
                               final String key19,
                               final Gen<?> gen19,
                               final String key20,
                               final Gen<?> gen20
    ) {
        return new RecordGen(key1,
                             gen1,
                             key2,
                             gen2,
                             key3,
                             gen3,
                             key4,
                             gen4,
                             key5,
                             gen5,
                             key6,
                             gen6,
                             key7,
                             gen7,
                             key8,
                             gen8,
                             key9,
                             gen9,
                             key10,
                             gen10,
                             key11,
                             gen11,
                             key12,
                             gen12,
                             key13,
                             gen13,
                             key14,
                             gen14,
                             key15,
                             gen15,
                             key16,
                             gen16,
                             key17,
                             gen17,
                             key18,
                             gen18,
                             key19,
                             gen19,
                             key20,
                             gen20
        );
    }

    public RecordGen setNullables(final List<String> nullables) {
        this.nullables = requireNonNull(optionals);
        return this;
    }

    public RecordGen setNullables(final String... nullables) {
        this.nullables = Arrays.stream(requireNonNull(nullables)).toList();
        return this;
    }

    public RecordGen setOptionals(final List<String> optionals) {
        this.optionals = requireNonNull(optionals);
        return this;
    }

    public RecordGen setOptionals(final String... optional) {
        this.optionals = Arrays.stream(requireNonNull(optional)).toList();
        return this;
    }

    public RecordGen set(final String key,
                         final Gen<?> gen
    ) {
        var b = new LinkedHashMap<String, Gen<?>>();
        b.put(key,
              gen
        );
        return new RecordGen(b);
    }


    @Override
    public Supplier<Map<String, ?>> apply(final RandomGenerator gen) {
        Objects.requireNonNull(gen);

        Map<String, Supplier<?>> map = new LinkedHashMap<>();
        for (var pair : bindings.entrySet()) {
            map.put(pair.getKey(),
                    pair.getValue().apply(split.apply(gen))
            );
        }

        Supplier<List<String>> optionalCombinations =
                Combinators.permutations(optionals)
                           .apply(split.apply(gen));

        Supplier<Boolean> isRemoveOptionals =
                optionals.isEmpty() ?
                () -> false :
                BoolGen.arbitrary.apply(split.apply(gen));

        Supplier<List<String>> nullableCombinations =
                Combinators.permutations(nullables)
                           .apply(split.apply(gen));
        Supplier<Boolean> isRemoveNullables =
                nullables.isEmpty() ?
                () -> false :
                BoolGen.arbitrary.apply(split.apply(gen));
        return () ->
        {
            Map<String, Object> result = new LinkedHashMap<>();
            for (var pair : map.entrySet()) {
                final Object value = pair.getValue().get();
                result.put(pair.getKey(),
                           value
                );
            }
            if (isRemoveOptionals.get()) {
                final List<String> r = optionalCombinations.get();
                for (var s : r) result.remove(s);
            }
            if (isRemoveNullables.get()) {
                final List<String> r = nullableCombinations.get();
                for (var s : r)
                    result.put(s,
                               null);
            }
            return result;
        };
    }


}
