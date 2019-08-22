package jsonvalues;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

final class MyImmutableJsArray extends MyAbstractJsArray<MyScalaVector, JsObj>
{
    public static final long serialVersionUID = 1L;

    @SuppressWarnings("squid:S3008")//EMPTY should be a valid name
    static MyImmutableJsArray EMPTY = new MyImmutableJsArray(MyScalaVector.EMPTY);
    private transient volatile int hascode;
    //squid:S3077: doesn't make any sese, volatile is perfectly valid here an as a matter of fact
    //is a recomendation from Efective Java to apply the idiom single check for lazy initialization
    @SuppressWarnings("squid:S3077")
    @Nullable
    private transient volatile String str;

    MyImmutableJsArray(final MyScalaVector array)
    {
        super(array);
    }

    @Override
    MyAbstractJsArray<MyScalaVector, JsObj> emptyArray()
    {
        return EMPTY;
    }

    @Override
    JsObj emptyObject()
    {
        return MyImmutableJsObj.EMPTY;
    }


    /**
     equals method is inherited, so it's implemented. The purpose of this method is to cache
     the hashcode once calculated. the object is immutable and it won't change
     Single-check idiom  Item 83 from Effective Java
     */
    @SuppressWarnings("squid:S1206")
    @Override
    public final int hashCode()
    {
        int result = hascode;
        if (result == 0)
            hascode = result = super.hashCode();
        return result;
    }

    @Override
    JsArray of(final MyScalaVector vector)
    {
        return new MyImmutableJsArray(vector);
    }

    @Override
    public JsArray toImmutable()
    {
        return this;
    }

    public JsArray toMutable()
    {
        List<JsElem> acc = new ArrayList<>();
        array.forEach(MatchExp.accept(acc::add,
                                      obj -> acc.add(obj.toMutable()),
                                      arr -> acc.add(arr.toMutable())
                                     ));
        return new MyMutableJsArray(new MyJavaVector(acc));

    }

    @Override
    public boolean isMutable()
    {
        return false;
    }

    @Override
    public boolean isImmutable()
    {
        return true;
    }

    @Override
    /**
     Single-check idiom  Item 83 from Effective Java
     */
    public final String toString()
    {
        String result = str;
        if (result == null)
            str = result = super.toString();
        return result;

    }

    @Override
    public final JsArray mapElems(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return new OpMapImmutableArrElems(this).map(requireNonNull(fn),
                                                    p -> true,
                                                    JsPath.empty()
                                                          .index(-1)
                                                   )
                                               .get();

    }

    @Override
    public JsArray mapElems(final Function<? super JsPair, ? extends JsElem> fn,
                            final Predicate<? super JsPair> predicate
                           )
    {
        return new OpMapImmutableArrElems(this).map(requireNonNull(fn),
                                                    requireNonNull(predicate),
                                                    JsPath.empty()
                                                          .index(-1)
                                                   )
                                               .get();
    }

    @Override
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn)
    {
        return new OpMapImmutableArrElems(this).map_(requireNonNull(fn),
                                                     p -> true,
                                                     JsPath.empty()
                                                           .index(-1)
                                                    )
                                               .get();
    }

    @Override
    public JsArray mapElems_(final Function<? super JsPair, ? extends JsElem> fn,
                             final Predicate<? super JsPair> predicate
                            )
    {
        return new OpMapImmutableArrElems(this).map_(requireNonNull(fn),
                                                     requireNonNull(predicate),
                                                     JsPath.empty()
                                                           .index(-1)
                                                    )
                                               .get();
    }

    @Override
    public final JsArray mapKeys(final Function<? super JsPair, String> fn)
    {
        return this;
    }

    @Override
    public final JsArray mapKeys(final Function<? super JsPair, String> fn,
                                 final Predicate<? super JsPair> predicate
                                )
    {
        return this;
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray mapKeys_(final Function<? super JsPair, String> fn)
    {
        return new OpMapImmutableArrKeys(this).map_(requireNonNull(fn),
                                                    it -> true,
                                                    JsPath.empty()
                                                          .index(-1)
                                                   )
                                              .get();
    }

    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsArray mapKeys_(final Function<? super JsPair, String> fn,
                                  final Predicate<? super JsPair> predicate
                                 )
    {
        return new OpMapImmutableArrKeys(this).map_(requireNonNull(fn),
                                                    requireNonNull(predicate),
                                                    JsPath.empty()
                                                          .index(-1)
                                                   )
                                              .get();

    }


    @Override
    public final JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                 final BiPredicate<? super JsPath, ? super JsObj> predicate
                                )
    {

        return new OpMapImmutableArrObjs(this).map(requireNonNull(fn),
                                                   requireNonNull(predicate),
                                                   JsPath.empty()
                                                         .index(-1)
                                                  )
                                              .get();

    }


    @Override
    public final JsArray mapObjs(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapImmutableArrObjs(this).map(requireNonNull(fn),
                                                   (p, o) -> true,
                                                   JsPath.empty()
                                                         .index(-1)
                                                  )
                                              .get();
    }

    @Override
    public final JsArray mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn,
                                  final BiPredicate<? super JsPath, ? super JsObj> predicate
                                 )
    {
        return new OpMapImmutableArrObjs(this).map_(requireNonNull(fn),
                                                    requireNonNull(predicate),
                                                    JsPath.empty()
                                                          .index(-1)
                                                   )
                                              .get();
    }

    @Override
    public final JsArray mapObjs_(final BiFunction<? super JsPath, ? super JsObj, JsObj> fn)
    {
        return new OpMapImmutableArrObjs(this).map_(requireNonNull(fn),
                                                    (p, o) -> true,
                                                    JsPath.empty()
                                                          .index(-1)
                                                   )
                                              .get();
    }


    @Override
    public final JsArray filterElems(final Predicate<? super JsPair> filter)
    {
        return new OpFilterImmutableArrElems(this).filter(JsPath.empty()
                                                                .index(-1),
                                                          requireNonNull(filter)
                                                         )

                                                  .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention:  xx_ traverses the whole json
    public final JsArray filterElems_(final Predicate<? super JsPair> filter)
    {
        return new OpFilterImmutableArrElems(this).filter_(JsPath.empty()
                                                                 .index(-1),
                                                           requireNonNull(filter)
                                                          )

                                                  .get();
    }

    @Override
    public final JsArray filterObjs(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterImmutableArrObjs(this).filter(JsPath.empty()
                                                               .index(-1),
                                                         requireNonNull(filter)
                                                        )

                                                 .get();
    }


    @Override
    @SuppressWarnings("squid:S00100") //  naming convention: xx_ traverses the whole json
    public final JsArray filterObjs_(final BiPredicate<? super JsPath, ? super JsObj> filter)
    {
        return new OpFilterImmutableArrObjs(this).filter_(JsPath.empty()
                                                                .index(-1),
                                                          requireNonNull(filter)
                                                         )
                                                 .get();
    }


    @Override
    public final JsArray filterKeys(final Predicate<? super JsPair> filter)
    {
        return this;
    }

    @Override
    public final JsArray filterKeys_(final Predicate<? super JsPair> filter)
    {
        return new OpFilterImmutableArrKeys(this).filter_(JsPath.empty()
                                                                .index(-1),
                                                          filter
                                                         )
                                                 .get();
    }


    /**
     * Serialize this {@code ScalaJsObj} instance.
     *
     * @serialData The {@code String}) representation of this json object.
     */
    private void writeObject(ObjectOutputStream s) throws IOException
    {
        s.defaultWriteObject();
        s.writeObject(toString());
    }

    //squid:S4508: implemented after reviewing chapter 12 from Effectiva Java!
    @SuppressWarnings("squid:S4508")
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException
    {
        s.defaultReadObject();
        final String json = (String) s.readObject();
        try
        {
            array = ((MyImmutableJsArray) JsArray.parse(json)
                                                 .orElseThrow()).array;
        }
        catch (MalformedJson malformedJson)
        {
            throw new NotSerializableException(String.format("Error deserializing a string into the class %s: %s",
                                                             JsArray.class.getName(),
                                                             malformedJson.getMessage()
                                                            ));
        }

    }


}
