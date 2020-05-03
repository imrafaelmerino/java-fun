package jsonvalues.gen;

import jsonvalues.JsArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

final class JsTupleGen implements JsGen<JsArray>
{
  private List<JsGen<?>> gens = new ArrayList<>();

   JsTupleGen(final JsGen<?> gen,
                     final JsGen<?>... others
                    )
  {
    gens.add(gen);
    gens.addAll(Arrays.asList(others));
  }

  @Override
  public Supplier<JsArray> apply(final Random random)
  {

    return () ->
    {
      JsArray array = JsArray.empty();
      for (final JsGen<?> gen : gens)
        array = array.append(gen.apply(random)
                                .get());
      return array;
    };
  }



}
