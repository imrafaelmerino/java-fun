package jsonvalues;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.*;

/**
 *
 */
public class TestJsElems
{

  @Test
  public void test_dates()
  {
    Assertions.assertTrue(JsStr.of(Instant.now()
                                          .toString())
                               .isInstant());
    Assertions.assertFalse(JsStr.of(LocalDateTime.now()
                                                 .toString())
                                .isInstant(i -> true));
    Assertions.assertTrue(JsStr.of(Instant.now()
                                          .toString())
                               .isInstant(i -> i.isBefore(Instant.now()
                                                                 .plusSeconds(1))));
    Assertions.assertFalse(JsStr.of(ISO_LOCAL_TIME.format(LocalDateTime.now()))
                                .isInstant());
    Assertions.assertTrue(JsStr.of(ISO_LOCAL_DATE.format(LocalDateTime.now()))
                               .isLocalDate(ISO_LOCAL_DATE));
    Assertions.assertTrue(JsStr.of(ISO_LOCAL_DATE.format(LocalDateTime.now()))
                               .isLocalDate(ISO_LOCAL_DATE,
                                            it -> it.isBefore(LocalDate.now()
                                                                       .plusYears(1))
                                           ));
    Assertions.assertFalse(JsStr.of(ISO_LOCAL_TIME.format(LocalDateTime.now()))
                                .isLocalDate(ISO_LOCAL_DATE));
    Assertions.assertFalse(JsStr.of(ISO_LOCAL_TIME.format(LocalDateTime.now()))
                                .isLocalDate(ISO_LOCAL_DATE,
                                             i -> true
                                            ));
    Assertions.assertFalse(JsStr.of(ISO_LOCAL_TIME.format(LocalDateTime.now()))
                                .isInstant());
    Assertions.assertTrue(JsStr.of(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()))
                               .isLocalDateTime(ISO_LOCAL_DATE_TIME));
    Assertions.assertFalse(JsStr.of(ISO_LOCAL_DATE.format(LocalDate.now()))
                                .isLocalDateTime(ISO_LOCAL_DATE_TIME,
                                                 i -> true
                                                ));
    Assertions.assertFalse(JsStr.of(ISO_LOCAL_DATE.format(LocalDate.now()))
                                .isLocalDateTime(ISO_LOCAL_DATE_TIME
                                                ));
    Assertions.assertTrue(JsStr.of(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()))
                               .isLocalDateTime(ISO_LOCAL_DATE_TIME,
                                                it -> it.isBefore(LocalDateTime.now()
                                                                               .plusYears(1))
                                               ));
  }

  @Test
  public void test_elements()
  {
    Assertions.assertTrue(JsInt.of(10)
                               .test(i -> i % 2 == 0));
    Assertions.assertTrue(JsBigInt.of(BigInteger.ONE)
                                  .test(i -> i.equals(BigInteger.ONE)));
    Assertions.assertTrue(JsBigDec.of(BigDecimal.ONE)
                                  .test(i -> i.equals(BigDecimal.ONE)));
    Assertions.assertTrue(JsStr.of("abcd")
                               .test(s -> s.length() == 4));

    Assertions.assertTrue(JsLong.of(20L)
                                .test(l -> l == 20L));
    Assertions.assertTrue(JsDouble.of(0.5d)
                                  .test(d -> d == 0.5d));
    Assertions.assertTrue(JsBool.TRUE.isTrue());

    Assertions.assertTrue(JsBigInt.of(BigInteger.ONE)
                                  .isBigInt(i -> i.equals(BigInteger.ONE)));

    Assertions.assertFalse(JsDouble.of(1.5d)
                                   .test(i -> i % 2 == 0));

  }

  @Test
  public void test_equals()
  {
    Assertions.assertEquals(JsBigDec.of(BigDecimal.valueOf(1.00)),
                            JsBigDec.of(BigDecimal.ONE)
                           );
    Assertions.assertEquals(JsBigDec.of(BigDecimal.valueOf(1.00))
                                    .hashCode(),
                            JsBigDec.of(BigDecimal.ONE)
                                    .hashCode()
                           );
    Assertions.assertEquals(JsLong.of(1L),
                            JsBigDec.of(new BigDecimal(1)));
    Assertions.assertEquals(JsLong.of(1L),
                            JsDouble.of(1.00));
    Assertions.assertEquals(JsInt.of(1),
                            JsDouble.of(1.00));

    Assertions.assertEquals(JsInt.of(1).hashCode(),
                            JsDouble.of(1.00).hashCode());

    Assertions.assertEquals(JsBigInt.of(BigInteger.ONE).hashCode(),
                            JsDouble.of(1.00).hashCode()
                           );

    Assertions.assertEquals(JsDouble.of(100_000_000_000L).hashCode(),JsLong.of(100_000_000_000L).hashCode());
    Assertions.assertEquals(JsDouble.of(100_000_000_000L).hashCode(),JsBigInt.of(new BigInteger("100000000000")).hashCode());
  }

  @Test
  public void test_jselem_casting_exceptions()
  {
    Assertions.assertThrows(UserError.class,
                            () -> JsInt.of(1)
                                       .toJson()
                           );
    Assertions.assertThrows(UserError.class,
                            () -> JsInt.of(1)
                                       .toJsStr()
                           );

    Assertions.assertThrows(UserError.class,
                            () -> JsInt.of(1)
                                       .toJsObj()
                           );

    Assertions.assertThrows(UserError.class,
                            () -> JsStr.of("1")
                                       .toJsInt()
                           );

    Assertions.assertThrows(UserError.class,
                            () -> JsStr.of("1")
                                       .toJsBigInt()
                           );

    Assertions.assertThrows(UserError.class,
                            () -> JsStr.of("1")
                                       .toJsBigDec()
                           );

    Assertions.assertThrows(UserError.class,
                            () -> JsDouble.of(1d)
                                          .toJsInt()
                           );

    Assertions.assertThrows(UserError.class,
                            () -> JsDouble.of(1d)
                                          .toJsStr()
                           );

    Assertions.assertThrows(UserError.class,
                            () -> JsLong.of(1)
                                        .toJsStr()
                           );


    Assertions.assertThrows(UserError.class,
                            () -> JsBigInt.of(BigInteger.ONE)
                                          .toJsStr()
                           );

    Assertions.assertThrows(UserError.class,
                            () -> JsBigInt.of(BigInteger.ONE)
                                          .toJsArray()
                           );


    Assertions.assertThrows(UserError.class,
                            () -> JsBigInt.of(BigInteger.ONE)
                                          .toJsDouble()
                           );

    Assertions.assertThrows(UserError.class,
                            () -> JsBigInt.of(BigInteger.ONE)
                                          .toJsInt()
                           );

    Assertions.assertThrows(UserError.class,
                            () -> JsBigInt.of(BigInteger.ONE)
                                          .toJsLong()
                           );

    Assertions.assertThrows(UserError.class,
                            () -> JsBigInt.of(BigInteger.ONE)
                                          .toJsBool()
                           );
  }

  @Test
  public void test_map_elements()
  {

    Assertions.assertEquals(JsInt.of(10),
                            JsInt.of(5)
                                 .map(i -> i * 2)
                           );
    Assertions.assertEquals(JsBigInt.of(BigInteger.valueOf(2)),
                            JsBigInt.of(BigInteger.ONE)
                                    .map(i -> i.add(BigInteger.ONE))
                           );
    Assertions.assertEquals(JsBigDec.of(BigDecimal.valueOf(1.5d)),
                            JsBigDec.of(BigDecimal.valueOf(1d))
                                    .map(i -> i.add(BigDecimal.valueOf(0.5d)))
                           );
    Assertions.assertEquals(JsInt.of(10),
                            JsInt.of(5)
                                 .map(i -> i * 2)
                           );
    Assertions.assertEquals(JsStr.of("abcd"),
                            JsStr.of("ABCD")
                                 .map(String::toLowerCase)
                           );
    Assertions.assertEquals(JsLong.of(Long.MAX_VALUE - 2),
                            JsLong.of(2)
                                  .map(i -> Long.MAX_VALUE - 2)
                           );
    Assertions.assertEquals(JsDouble.of(0.5d),
                            JsDouble.of(0.1d)
                                    .map(i -> i * 5)
                           );


  }

  @Test
  public void test_map_pairs()
  {

    Assertions.assertEquals(JsPair.of(JsPath.path("/a/b/2"),
                                      JsInt.of(1)
                                     ),
                            JsPair.of(JsPath.path("/a/b/1"),
                                      JsInt.of(1)
                                     )
                                  .mapPath(JsPath::inc)
                           );

    final JsPair pair1 = JsPair.of(JsPath.path("/a/b/1"),
                                   JsInt.of(1)
                                  )
                               .mapElem(e -> e.toJsInt()
                                              .map(i -> i + 1));

    Assertions.assertEquals(JsPair.of(JsPath.path("/a/b/1"),
                                      JsInt.of(2)
                                     ),
                            pair1
                           );

  }


}
