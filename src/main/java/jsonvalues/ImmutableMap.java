package jsonvalues;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.util.function.Predicate;

import static jsonvalues.JsBool.FALSE;
import static jsonvalues.JsBool.TRUE;
import static jsonvalues.JsNull.NULL;

/**
 Represents an immutable data structure where pairs of a JsObj are stored. Each immutable Json factory {@link ImmutableJsons} has an
 implementation of this interface, that can be defined using the method {@link jsonvalues.ImmutableJsons#withMap(Class)}.
 The default immutable implementation that {@link Jsons#immutable} uses is the <a href="https://scala-lang.org/files/archive/api/2.12.0/scala/collection/seq/HashMap.html">immutable Scala HashMap</a>.
 */
public interface ImmutableMap extends MyMap<ImmutableMap>
{

    /**
     removes the key associated with this map. It will be called by the library only if the key exists
     @param key the given key
     @return a map with the key removed
     */
    ImmutableMap remove(final String key);

    /**
     updates the element associated with the key with a new element. It will be called by the library only if the key exists,
     @param key the given key
     @param je the new element
     @return a map with the key element updated
     */
    ImmutableMap update(final String key,
                        final JsElem je
                       );


    default ImmutableMap parse(final ImmutableJsons fac,
                               final JsonParser parser
                              ) throws IOException
    {
        ImmutableMap root = this;
        while (parser.nextToken() != JsonToken.END_OBJECT)
        {
            final String key = parser.getCurrentName();
            final JsonToken elem = parser.nextToken();
            switch (elem.id())
            {
                case 6:
                    root = root.update(key,
                                       JsStr.of(parser.getValueAsString())
                                      );
                    break;
                case 7:
                    root = root.update(key,
                                       JsNumber.of(parser)
                                      );
                    break;
                case 8:
                    root = root.update(key,
                                       JsBigDec.of(parser.getDecimalValue())
                                      );
                    break;
                case 10:
                    root = root.update(key,
                                       FALSE
                                      );
                    break;
                case 9:
                    root = root.update(key,
                                       TRUE
                                      );
                    break;
                case 11:
                    root = root.update(key,
                                       NULL
                                      );
                    break;
                case 1:
                    final ImmutableMap newObj = fac.object.emptyMap.parse(fac,
                                                                          parser
                                                                         );
                    root = root.update(key,
                                       new ImmutableJsObj(newObj,
                                                          fac
                                       )
                                      );
                    break;
                case 3:
                    final ImmutableSeq newArr = fac.array.emptySeq.parse(fac,
                                                                         parser
                                                                        );
                    root = root.update(key,
                                       new ImmutableJsArray(newArr,
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

    default ImmutableMap parse(final ImmutableJsons fac,
                               final JsonParser parser,
                               final ParseBuilder.Options options,
                               final JsPath path
                              ) throws IOException
    {

        ImmutableMap newRoot = this;
        final Predicate<JsPair> condition = p -> options.elemFilter.test(p) && options.keyFilter.test(p.path);
        while (parser.nextToken() != JsonToken.END_OBJECT)
        {
            final String key = options.keyMap.apply(parser.getCurrentName());
            final JsPath currentPath = path.key(key);
            final JsonToken elem = parser.nextToken();
            final JsPair pair;
            assert elem != null;
            switch (elem.id())
            {
                case 6:
                    pair = JsPair.of(currentPath,
                                     JsStr.of(parser.getValueAsString())
                                    );
                    newRoot = (condition.test(pair)) ? newRoot.update(key,
                                                                      options.elemMap.apply(pair)
                                                                     ) : newRoot;
                    break;
                case 7:
                    pair = JsPair.of(currentPath,
                                     JsNumber.of(parser)
                                    );
                    newRoot = (condition.test(pair)) ? newRoot.update(key,
                                                                      options.elemMap.apply(pair)
                                                                     ) : newRoot;
                    break;
                case 8:
                    pair = JsPair.of(currentPath,
                                     JsBigDec.of(parser.getDecimalValue())
                                    );
                    newRoot = (condition.test(pair)) ? newRoot.update(key,
                                                                      options.elemMap.apply(pair)
                                                                     ) : newRoot;
                    break;
                case 9:
                    pair = JsPair.of(currentPath,
                                     TRUE
                                    );
                    newRoot = (condition.test(pair)) ? newRoot.update(key,
                                                                      options.elemMap.apply(pair)
                                                                     ) : newRoot;
                    break;
                case 10:
                    pair = JsPair.of(currentPath,
                                     FALSE
                                    );
                    newRoot = (condition.test(pair)) ? newRoot.update(key,
                                                                      options.elemMap.apply(pair)
                                                                     ) : newRoot;
                    break;
                case 11:
                    pair = JsPair.of(currentPath,
                                     NULL
                                    );
                    newRoot = (condition.test(pair)) ? newRoot.update(key,
                                                                      options.elemMap.apply(pair)
                                                                     ) : newRoot;
                    break;

                case 1:
                    if (options.keyFilter.test(currentPath))
                    {
                        newRoot = newRoot.update(key,
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
                case 3:
                    if (options.keyFilter.test(currentPath))
                    {
                        newRoot = newRoot.update(key,
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
                    throw InternalError.tokenNotExpected(elem.name());
            }
        }
        return newRoot;
    }
}
