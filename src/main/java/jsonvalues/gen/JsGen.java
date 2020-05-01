package jsonvalues.gen;

import jsonvalues.JsValue;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public interface JsGen<R extends JsValue> extends Function<Random, Supplier<R>> {

   default <T extends JsValue> JsGen<T> map(Function<R,T> f){
     return random -> () -> f.apply(JsGen.this.apply(random).get());
   }

   default <T extends JsValue> JsGen<T> flatMap(Function<R,JsGen<T>> f){
     return random ->  f.apply(JsGen.this.apply(random).get()).apply(random);
   }
}
