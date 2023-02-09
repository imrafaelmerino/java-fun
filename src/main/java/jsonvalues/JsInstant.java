package jsonvalues;

import fun.optic.Prism;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * Represents an immutable instant. An instant is not part of the Json specification. It is serialized into
 * it's a string representation using ISO-8601 representation. A JsInstant and a JsStr are equals
 * if both represent the same date.
 * {@code
 * Instant a = Instant.now();
 * JsStr.of(a.toString()).equals(JsInstant.of(a)) // true
 * }
 */
public final class JsInstant extends JsPrimitive implements Comparable<JsInstant> {

    /**
     * prism between the sum type JsValue and JsInstant
     */
    public static final Prism<JsValue, Instant> prism =
            new Prism<>(s -> {
                if (s.isInstant()) return Optional.of(s.toJsInstant().value);
                if (s.isStr()) return JsStr.instantPrism.getOptional.apply(s.toJsStr().value);
                return Optional.empty();
            },
                        JsInstant::of
            );
    public final Instant value;

    private JsInstant(final Instant value) {
        this.value = value;
    }

    public static JsInstant of(final Instant instant) {
        return new JsInstant(requireNonNull(instant));
    }

    public static JsInstant of(final String instant) {
        return JsInstant.of(Instant.parse(requireNonNull(instant)));
    }

    public JsInstant map(Function<Instant, Instant> fn) {
        return JsInstant.of(requireNonNull(fn).apply(value));
    }

    @Override
    public JsPrimitive toJsPrimitive() {
        return this;
    }

    @Override
    public boolean isInstant() {
        return true;
    }

    @Override
    public String toString() {
        return DateTimeFormatter.ISO_INSTANT.format(value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null) return false;
        return o instanceof JsValue ?
               JsInstant.prism.getOptional.apply(((JsValue) o))
                                          .map(instant -> instant.equals(value))
                                          .orElse(false) :
              false;

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value.toString());
    }

    @Override
    public int compareTo(final JsInstant o) {
        return value.compareTo(requireNonNull(o).value);

    }
}
