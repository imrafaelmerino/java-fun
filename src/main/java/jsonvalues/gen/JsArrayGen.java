package jsonvalues.gen;

import jsonvalues.JsArray;

import java.util.Random;
import java.util.function.Supplier;

 public class JsArrayGen implements JsGen<JsArray>
{

  private final int size;
  private final JsGen<?> gen;

  private JsArrayGen(final int size,
                     final JsGen<?> gen
                    )
  {
    this.size = size;
    this.gen = gen;
  }

  public static JsArrayGen of(final JsGen<?> gen,
                              final int size
                             )
  {
    return new JsArrayGen(size, gen);
  }

  @Override
  public Supplier<JsArray> apply(final Random random)
  {
    return () ->
    {
      JsArray array = JsArray.empty();
      for (int i = 0; i < size; i++) array = array.append(gen.apply(random).get());
      return array;
    };
  }


}
