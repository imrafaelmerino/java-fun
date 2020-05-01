package jsonvalues.gen;

import jsonvalues.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.Objects.requireNonNull;

public class JsGens
{
  private static final int Z = 122;
  private final static int A = 97;
  private final static int ZERO = 48;
  private static final String LENGTH_EQUAL_ZERO_ERROR = "string length must be greater than zero.";

  public static JsGen<JsStr> strGen = chooseGen(0, 10).flatMap(n-> strGen(n.value));

  public static JsGen<JsStr> alphabeticGen = chooseGen(0, 10).flatMap(n-> alphabeticGen(n.value));

  public static JsGen<JsStr> alphanumericGen = chooseGen(0, 10).flatMap(n-> alphanumericGen(n.value));

  public static JsGen<JsStr> strGen(final int length)
  {
    if(length<0)throw new IllegalArgumentException(LENGTH_EQUAL_ZERO_ERROR);

    return r -> () ->
    {
      if(length==0)return JsStr.of("");
      byte[] array = new byte[r.nextInt(length)];
      new Random().nextBytes(array);
      return JsStr.of(new String(array,
                                 StandardCharsets.UTF_8
                      )
                     );
    };
  }

  public static JsGen<JsBool> boolGen = r -> () -> JsBool.of(r.nextBoolean());

  public static JsGen<JsLong> longNumGen = r -> () -> JsLong.of(r.nextLong());

  public static JsGen<JsInt> intNumGen = r -> () -> JsInt.of(r.nextInt());

  public static JsGen<JsDouble> doubleNumGen = r -> () -> JsDouble.of(r.nextDouble());

  public static JsGen<JsStr> alphabeticGen(final int length)
  {
    if(length<0)throw new IllegalArgumentException(LENGTH_EQUAL_ZERO_ERROR);

    return r -> () -> JsStr.of(r.ints(A,
                                      Z + 1
                                     )
                                .limit(length)
                                .collect(StringBuilder::new,
                                         StringBuilder::appendCodePoint,
                                         StringBuilder::append
                                        )
                                .toString());
  }

  public static JsGen<JsStr> alphanumericGen(final int length)
  {
    if(length<0)throw new IllegalArgumentException(LENGTH_EQUAL_ZERO_ERROR);

    return r -> () -> JsStr.of(r.ints(ZERO,
                                      Z + 1
                                     )
                                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                                .limit(length)
                                .collect(StringBuilder::new,
                                         StringBuilder::appendCodePoint,
                                         StringBuilder::append
                                        )
                                .toString());
  }

  public static JsGen<JsInt> chooseGen(final int min, final int max){
    if(min>max)throw new IllegalArgumentException("min must be lower than max");
    return r -> () -> JsInt.of(r.nextInt((max - min) + 1) + min);
  }

  public static JsGen<JsValue> singleGen(final JsValue value)
  {
    return r -> () -> requireNonNull(value);
  }


  public static JsGen<JsValue> oneOfGen(final JsValue a,
                                        final JsValue... others
                                       )
  {
    return r -> () ->
    {
      final int n = r.nextInt(others.length + 1);
      if (n == 0) return requireNonNull(a);
      else return requireNonNull(others)[others.length - 1];
    };
  }

  public static JsGen<?> oneOfGen(final JsGen<?> a,
                                  final JsGen<?>... others
                                 )
  {
    int n = new Random().nextInt(1 + others.length);
    final List<JsGen<?>> gens = new ArrayList<>();
    gens.add(requireNonNull(a));
    Collections.addAll(gens,
                       requireNonNull(others)
                      );
    return gens.get(n);
  }

  public static <O extends JsValue> JsGen<O> oneOfGen(final List<O> list){
    return r -> () -> {
      if(requireNonNull(list).size()==0)return list.get(0);
      final int index = r.nextInt(list.size());
      return list.get(index);
    };
  }

}
