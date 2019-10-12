package jsonvalues;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonTokenId;
import scala.Tuple2;
import scala.collection.Iterator;
import scala.collection.JavaConverters;
import scala.collection.immutable.HashMap;
import scala.runtime.AbstractFunction1;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;

final class ScalaImmutableMap extends ImmutableMap
{
    private static final HashMap<String, JsElem> EMPTY_HASH_MAP = new HashMap<>();
    private final scala.collection.immutable.HashMap<String, JsElem> persistentMap;

    ScalaImmutableMap()
    {
        this.persistentMap = EMPTY_HASH_MAP;
    }

    ScalaImmutableMap(final scala.collection.immutable.HashMap<String, JsElem> map)
    {
        this.persistentMap = map;
    }

    static AbstractFunction1<String, String> af1(UnaryOperator<String> uo)
    {
        return new AbstractFunction1<String, String>()
        {
            @Override
            public String apply(final String str)
            {
                return uo.apply(str);
            }
        };
    }

    @Override
    public boolean contains(final String key)
    {
        return persistentMap.contains(key);
    }


    @Override
    public final Set<String> keys()
    {
        return JavaConverters.setAsJavaSet(persistentMap.keys()
                                                        .toSet());


    }

    @Override
    public JsElem get(final String key)
    {
        try
        {
            return persistentMap.apply(key);
        }
        catch (NoSuchElementException e)
        {
            throw InternalError.keyNotFound(key);
        }
    }

    @Override
    public Optional<JsElem> getOptional(final String key)
    {
        return persistentMap.contains(key) ? Optional.of(persistentMap.get(key)
                                                                      .get()) : Optional.empty();
    }

    @Override
    @SuppressWarnings("squid:S1206") //equals method never used. Implemented in AbstractJsObj
    public int hashCode()
    {
        return persistentMap.hashCode();
    }


    @Override
    public java.util.Map.Entry<String, JsElem> head()
    {
        if (this.isEmpty()) throw UserError.headOfEmptyObj();

        final Tuple2<String, JsElem> head = persistentMap.head();

        return new AbstractMap.SimpleEntry<>(head._1,
                                             head._2
        );
    }

    @Override
    public boolean isEmpty()
    {
        return persistentMap.isEmpty();
    }

    @Override
    public java.util.Iterator<java.util.Map.Entry<String, JsElem>> iterator()
    {
        final Iterator<Tuple2<String, JsElem>> iterator = persistentMap.iterator();
        return JavaConverters.asJavaIterator(iterator.map(it -> new AbstractMap.SimpleEntry<>(it._1,
                                                                                              it._2
                                                          )
                                                         ));
    }

    @Override
    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public ScalaImmutableMap remove(final String key)
    {
        return new ScalaImmutableMap(((HashMap<String, JsElem>) persistentMap).$minus(key));
    }

    @Override
    public int size()
    {
        return persistentMap.size();
    }

    @Override
    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public ScalaImmutableMap tail(String head)
    {
        if (this.isEmpty()) throw UserError.tailOfEmptyObj();
        return new ScalaImmutableMap(((HashMap<String, JsElem>) persistentMap).$minus(head));
    }

    @Override
    public String toString()
    {
        if (persistentMap.isEmpty()) return "{}";


        return persistentMap.keysIterator()
                            .map(af1(key -> String.format("\"%s\":%s",
                                                          key,
                                                          persistentMap.apply(key)
                                                         )))
                            .mkString("{",
                                      ",",
                                      "}"
                                     );
    }

    @Override
    public ScalaImmutableMap update(final String key,
                                    final JsElem e
                                   )
    {
        return new ScalaImmutableMap(persistentMap.updated(key,
                                                           e
                                                          ));
    }

    public ImmutableMap parse(final ImmutableJsons fac,
                              final JsonParser parser
                             ) throws IOException
    {
        HashMap<String, JsElem> map = EMPTY_HASH_MAP;
        String key = parser.nextFieldName();
        for (; key != null; key = parser.nextFieldName())
        {
            JsElem elem;
            switch (parser.nextToken()
                          .id())
            {
                case JsonTokenId.ID_STRING:
                    elem = JsStr.of(parser.getValueAsString());
                    break;
                case JsonTokenId.ID_NUMBER_INT:
                    elem = JsNumber.of(parser);
                    break;
                case JsonTokenId.ID_NUMBER_FLOAT:
                    elem = JsBigDec.of(parser.getDecimalValue());
                    break;
                case JsonTokenId.ID_FALSE:
                    elem = FALSE;
                    break;
                case JsonTokenId.ID_TRUE:
                    elem = TRUE;
                    break;
                case JsonTokenId.ID_NULL:
                    elem = NULL;
                    break;
                case JsonTokenId.ID_START_OBJECT:
                    elem = new ImmutableJsObj(Jsons.immutable.object.emptyMap.parse(fac,
                                                                                    parser
                                                                                   ),
                                              fac

                    );
                    break;
                case JsonTokenId.ID_START_ARRAY:
                    elem = new ImmutableJsArray(Jsons.immutable.array.emptySeq.parse(fac,
                                                                                     parser
                                                                                    ),
                                                fac

                    );
                    break;
                default:
                    throw InternalError.tokenNotExpected(parser.currentToken()
                                                               .name());


            }
            map = map.updated(key,
                              elem
                             );
        }

        return new ScalaImmutableMap(map);

    }

    public ImmutableMap parse(final ImmutableJsons fac,
                              final JsonParser parser,
                              final ParseBuilder.Options options,
                              final JsPath path
                             ) throws IOException
    {

        HashMap<String, JsElem> map = EMPTY_HASH_MAP;
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while (parser.nextToken() != JsonToken.END_OBJECT)
        {
            final String key = options.keyMap.apply(parser.getCurrentName());
            final JsPath currentPath = path.key(key);
            final JsPair pair;
            switch (parser.nextToken()
                          .id())
            {
                case JsonTokenId.ID_STRING:
                    pair = JsPair.of(currentPath,
                                     JsStr.of(parser.getValueAsString())
                                    );
                    map = (condition.test(pair)) ? map.updated(key,
                                                               options.elemMap.apply(pair)
                                                              ) : map;
                    break;
                case JsonTokenId.ID_NUMBER_INT:
                    pair = JsPair.of(currentPath,
                                     JsNumber.of(parser)
                                    );
                    map = (condition.test(pair)) ? map.updated(key,
                                                               options.elemMap.apply(pair)
                                                              ) : map;
                    break;
                case JsonTokenId.ID_NUMBER_FLOAT:
                    pair = JsPair.of(currentPath,
                                     JsBigDec.of(parser.getDecimalValue())
                                    );
                    map = (condition.test(pair)) ? map.updated(key,
                                                               options.elemMap.apply(pair)
                                                              ) : map;
                    break;
                case JsonTokenId.ID_TRUE:
                    pair = JsPair.of(currentPath,
                                     TRUE
                                    );
                    map = (condition.test(pair)) ? map.updated(key,
                                                               options.elemMap.apply(pair)
                                                              ) : map;
                    break;
                case JsonTokenId.ID_FALSE:
                    pair = JsPair.of(currentPath,
                                     FALSE
                                    );
                    map = (condition.test(pair)) ? map.updated(key,
                                                               options.elemMap.apply(pair)
                                                              ) : map;
                    break;
                case JsonTokenId.ID_NULL:
                    pair = JsPair.of(currentPath,
                                     NULL
                                    );
                    map = (condition.test(pair)) ? map.updated(key,
                                                               options.elemMap.apply(pair)
                                                              ) : map;
                    break;

                case JsonTokenId.ID_START_OBJECT:
                    if (options.keyFilter.test(currentPath))
                    {
                        map = map.updated(key,
                                          new ImmutableJsObj(fac.object.emptyMap.parse(fac,
                                                                                       parser,
                                                                                       options,
                                                                                       currentPath
                                                                                      ),
                                                             fac
                                          )
                                         );
                    }
                    break;
                case JsonTokenId.ID_START_ARRAY:
                    if (options.keyFilter.test(currentPath))
                    {
                        map = map.updated(key,
                                          new ImmutableJsArray(fac.array.emptySeq.parse(fac,
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
                    throw InternalError.tokenNotExpected(parser.currentToken()
                                                               .name());
            }
        }
        return new ScalaImmutableMap(map);
    }
}
