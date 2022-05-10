package jsonvalues;


import fun.optic.Lens;

/**
 Represent a Lens which focus is the path of a json pair
 */
final class JsPathPairLens extends Lens<JsPair, JsPath> {
    JsPathPairLens() {
        super(pair -> pair.path,
              path -> pair -> JsPair.of(path,
                                        pair.value)
             );

    }
}
