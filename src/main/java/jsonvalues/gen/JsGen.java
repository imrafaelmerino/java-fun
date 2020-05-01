package jsonvalues.gen;

import jsonvalues.JsNothing;
import jsonvalues.JsValue;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public interface JsGen<R extends JsValue> extends Function<Random, Supplier<R>> {


  default JsGen<?> optional(){

    return this.flatMap(value -> JsGens.oneOfGen(value,
                                          JsNothing.NOTHING)
                );

  }

   default <T extends JsValue> JsGen<T> flatMap(Function<R,JsGen<T>> f){
     return random ->  f.apply(JsGen.this.apply(random).get()).apply(random);
   }
}
