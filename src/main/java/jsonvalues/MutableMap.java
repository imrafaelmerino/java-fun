package jsonvalues;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.util.function.Predicate;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;

/**
 Represents a mutable data structure where pairs of a JsObj are stored. Each mutable Json factory {@link MutableJsons} has an
 implementation of this interface, that can be defined using the method {@link jsonvalues.MutableJsons#withMap(Class)}.
 The default mutable implementation that {@link Jsons#mutable} uses is the Java {@link java.util.HashMap}
 */
public interface MutableMap extends MyMap<MutableMap>

{

    /**
     creates and returns a copy of this map
     @return a new instance
     */
    MutableMap copy();


    /**
     removes the key associated with this map. It will be called by the library only if the key exists
     @param key the given key
     */
    void remove(final String key);

    /**
     updates the element associated with the key with a new element. It will be called by the library only if the key exists,
     @param key the given key
     @param je the new element
     */
    void update(final String key,
                final JsElem je
               );


    default MutableMap parse(final MutableJsons fac,
                             final JsonParser parser
                            ) throws IOException
    {
        MutableMap root = this;
        while (parser.nextToken() != JsonToken.END_OBJECT)
        {
            final String key = parser.currentName();
            JsonToken elem = parser.nextToken();
            assert elem != null;
            switch (elem.id())
            {
                case 6:
                    root.update(key,
                                JsStr.of(parser.getValueAsString())
                               );
                    break;
                case 7:
                    root.update(key,
                                JsNumber.of(parser)
                               );
                    break;
                case 8:
                    root.update(key,
                                JsBigDec.of(parser.getDecimalValue())
                               );
                    break;
                case 10:
                    root.update(key,
                                FALSE
                               );
                    break;
                case 9:
                    root.update(key,
                                TRUE
                               );
                    break;
                case 11:
                    root.update(key,
                                NULL
                               );
                    break;
                case 1:
                    root.update(key,
                                new MutableJsObj(fac.object.emptyMap()
                                                           .parse(fac,
                                                                  parser
                                                                 ),
                                                 fac
                                )
                               );
                    break;
                case 3:
                    root.update(key,
                                new MutableJsArray(fac.array.emptySeq()
                                                            .parse(fac,
                                                                   parser
                                                                  ),
                                                   fac
                                )
                               );
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());


            }


        }

        return root;
    }

    default MutableMap parse(final MutableJsons fac,
                             final JsonParser parser,
                             final ParseBuilder.Options options,
                             final JsPath path
                            ) throws IOException
    {
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        MutableMap root = this;
        while (parser.nextToken() != JsonToken.END_OBJECT)
        {
            final String key = options.keyMap.apply(parser.currentName());
            final JsPath currentPath = path.key(key);
            JsonToken elem = parser.nextToken();
            assert elem != null;
            switch (elem.id())
            {
                case 6:
                    JsPair.of(currentPath,
                              JsStr.of(parser.getValueAsString())
                             )
                          .consumeIf(condition,
                                     p -> root.update(key,
                                                      options.elemMap.apply(p)
                                                     )
                                    );

                    break;
                case 7:
                    JsPair.of(currentPath,
                              JsNumber.of(parser)
                             )
                          .consumeIf(condition,
                                     p -> root.update(key,
                                                      options.elemMap.apply(p)
                                                     )
                                    );

                    break;
                case 8:
                    JsPair.of(currentPath,
                              JsBigDec.of(parser.getDecimalValue())
                             )
                          .consumeIf(condition,
                                     p -> root.update(key,
                                                      options.elemMap.apply(p)
                                                     )
                                    );

                    break;
                case 10:
                    JsPair.of(currentPath,
                              FALSE
                             )
                          .consumeIf(condition,
                                     p -> root.update(key,
                                                      options.elemMap
                                                      .apply(p)
                                                     )
                                    );

                    break;
                case 9:
                    JsPair.of(currentPath,
                              TRUE
                             )
                          .consumeIf(condition,
                                     p -> root.update(key,
                                                      options.elemMap
                                                      .apply(p)
                                                     )
                                    );

                    break;
                case 11:
                    JsPair.of(currentPath,
                              NULL
                             )
                          .consumeIf(condition,
                                     p -> root.update(key,
                                                      options.elemMap
                                                      .apply(p)
                                                     )
                                    );

                    break;
                case 1:
                    if (options.keyFilter.test(currentPath))
                    {
                        root.update(key,
                                    new MutableJsObj(fac.object.emptyMap()
                                                               .parse(fac,
                                                                      parser,
                                                                      options,
                                                                      currentPath
                                                                     ),
                                                     fac
                                    )
                                   );
                    }
                    break;
                case 3:
                    if (options.keyFilter.test(currentPath))
                    {
                        root.update(key,
                                    new MutableJsArray(fac.array.emptySeq()
                                                                .parse(fac,
                                                                       parser,
                                                                       options,
                                                                       currentPath.index(-1)
                                                                      ),
                                                       fac
                                    )
                                   );
                    }
                    break;
                default:
                    throw InternalError.tokenNotExpected(elem.name());

            }


        }
        return root;

    }
}

