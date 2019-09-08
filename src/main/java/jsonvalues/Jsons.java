package jsonvalues;

public final class Jsons
{

    private Jsons()
    {
    }

    public static final ImmutableJsons immutable = new ImmutableJsons(ScalaImmutableMap.class,
                                                                      ScalaImmutableVector.class
    );

    public static final MutableJsons mutable = new MutableJsons(JavaMap.class,
                                                                JavaList.class
    );

}
