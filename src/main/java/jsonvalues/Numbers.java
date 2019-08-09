package jsonvalues;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

class Numbers
{



    OptionalDouble bigDecimalToDouble(BigDecimal bigDecimal)
    {

        final double value = bigDecimal.doubleValue();
        if (value == Double.NEGATIVE_INFINITY) return OptionalDouble.empty();
        if (value == Double.POSITIVE_INFINITY) return OptionalDouble.empty();
        return OptionalDouble.of(value);

    }

    OptionalInt bigIntToInt(BigInteger bigInteger)
    {
        try
        {
            return OptionalInt.of(bigInteger.intValueExact());
        }
        catch (Exception e)
        {
            return OptionalInt.empty();
        }

    }

    OptionalInt bigDecimalToInt(BigDecimal bigDecimal)
    {

        try
        {
            return OptionalInt.of(bigDecimal.intValueExact());
        }
        catch (Exception e)
        {
            return OptionalInt.empty();
        }

    }

    OptionalLong bigDecimalToLong(BigDecimal bigDecimal)
    {
        try
        {
            return OptionalLong.of(bigDecimal.longValueExact());
        }
        catch (Exception e)
        {
            return OptionalLong.empty();
        }

    }

    Optional<BigInteger> bigDecimalToBigInteger(BigDecimal bigDecimal)
    {
        try
        {
            return Optional.of(bigDecimal.toBigIntegerExact());
        }
        catch (Exception e)
        {
            return Optional.empty();
        }

    }

    Optional<BigInteger> doubleToBigInteger(double x)
    {
        try
        {
            return Optional.ofNullable(BigDecimal.valueOf(x)
                                                 .toBigIntegerExact());
        }
        catch (Exception e)
        {
            return Optional.empty();
        }
    }

    boolean equals(BigInteger bigInteger,
                   BigDecimal bigDecimal
                  )
    {
        final Optional<BigInteger> optional = bigDecimalToBigInteger(bigDecimal);
        return optional.isPresent() && optional.get()
                                               .equals(bigInteger);
    }

    boolean equals(double d,
                   BigInteger bigInteger
                  )
    {


        final Optional<BigInteger> x = doubleToBigInteger(d);
        return x.isPresent() && x.get()
                                 .equals(bigInteger);
    }

    boolean equals(double d,
                   BigDecimal bigDecimal
                  )
    {

        //errorProne warning BigDecimalEquals -> compareTo instead of equals so 2.0 = 2.000
        return BigDecimal.valueOf(d)
                         .compareTo(bigDecimal) == 0;
    }

    boolean equals(int x,
                   BigDecimal bigDecimal
                  )
    {
        final OptionalInt optional = bigDecimalToInt(bigDecimal);
        return optional.isPresent() && optional.getAsInt() == x;
    }

    boolean equals(long x,
                   BigDecimal bigDecimal
                  )
    {
        final OptionalLong optional = bigDecimalToLong(bigDecimal);
        return optional.isPresent() && optional.getAsLong() == x;
    }

    boolean equals(BigInteger bigInteger,
                   long x
                  )
    {
        final OptionalLong optional = bigIntToLong(bigInteger);
        return optional.isPresent() && optional.getAsLong() == x;
    }

    boolean equals(BigInteger bigInteger,
                   int x
                  )
    {
        final OptionalInt optional = bigIntToInt(bigInteger);
        return optional.isPresent() && optional.getAsInt() == x;
    }

    boolean equals(BigDecimal bigDecimal,
                   long x
                  )
    {
        final OptionalLong optional = bigDecimalToLong(bigDecimal);
        return optional.isPresent() && optional.getAsLong() == x;
    }

    boolean equals(BigDecimal bigDecimal,
                   int x
                  )
    {
        final OptionalInt optional = bigDecimalToInt(bigDecimal);
        return optional.isPresent() && optional.getAsInt() == x;
    }

    OptionalLong bigIntToLong(BigInteger bigInteger)
    {
        try
        {
            return OptionalLong.of(bigInteger.longValueExact());
        }
        catch (Exception e)
        {
            return OptionalLong.empty();
        }

    }

    OptionalInt longToInt(Long a)
    {
        try
        {
            return OptionalInt.of(Math.toIntExact(a));
        }
        catch (Exception e)
        {
            return OptionalInt.empty();
        }
    }
}
