package jsonvalues;

/**
Singleton which contains the default Json factories.
 */
public final class Jsons
{

    private Jsons()
    {
    }

    /**
     Default factory of immutable jsons. It's a singleton and can not be modified. New factories can be created from this instance overwriting
     the default implementations using the methods {@link ImmutableJsons#withSeq(Class)} and {@link ImmutableJsons#withMap(Class)} (Class)}
     */
    public static final ImmutableJsons immutable = new ImmutableJsons(ScalaImmutableMap.class,
                                                                      ScalaImmutableVector.class
    );

    /**
     Default factory of mutable jsons. It's a singleton and can not be modified. New factories can be created from this instance overwriting
     the default implementations using the methods {@link MutableJsons#withSeq(Class)} and {@link MutableJsons#withMap(Class)} (Class)}
     */
    public static final MutableJsons mutable = new MutableJsons(JavaMap.class,
                                                                JavaList.class
    );

}
