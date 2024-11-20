package fun.gen.readme;

import fun.gen.Combinators;
import fun.gen.Gen;
import fun.gen.MyRecordGen;
import fun.gen.StrGen;
import fun.optic.Prism;

import java.util.Optional;
import java.util.function.Predicate;


public class Readme {


    private static final String LOGIN_FIELD = "login";
    private static final String PASSWORD_FIELD = "password";
    private static final String NAME_FIELD = "name";
    private static final int MAX_LOGIN_GEN_LENGTH = 100;

    private static final int MAX_PASSWORD_GEN_LENGTH = 100;

    private static final int MAX_NAME_GEN_LENGTH = 100;


    public static void main(String[] args) {


        Gen<String> loginGen =
                Combinators.oneOf(StrGen.alphabetic(0,
                                                    MAX_LOGIN_GEN_LENGTH),
                                  StrGen.biased(0,
                                                MAX_LOGIN_GEN_LENGTH)
                );

        Gen<String> passwordGen =
                Combinators.oneOf(StrGen.alphabetic(0,
                                                    MAX_PASSWORD_GEN_LENGTH),
                                  StrGen.biased(0,
                                                MAX_PASSWORD_GEN_LENGTH)
                );

        Gen<String> nameGen =
                Combinators.oneOf(StrGen.alphabetic(0,
                                                    MAX_NAME_GEN_LENGTH),
                                  StrGen.biased(0,
                                                MAX_NAME_GEN_LENGTH)
                );


        Gen<User> userGen = MyRecordGen.of(LOGIN_FIELD,
                                           loginGen,
                                           PASSWORD_FIELD,
                                           passwordGen,
                                           NAME_FIELD,
                                           nameGen)
                                       .withAllOptKeys()
                                       .map(record ->
                                                  new User(record.getOptStr(LOGIN_FIELD).orElse(null),
                                                           record.getStr(NAME_FIELD),
                                                           record.getOptStr(PASSWORD_FIELD).orElse(null))
                                     );

        Predicate<User> isValid = user ->
                user.getLogin() != null &&
                        user.getPassword() != null && user.getName() != null &&
                        !user.getLogin().trim().isEmpty() &&
                        !user.getName().trim().isEmpty() &&
                        !user.getPassword().trim().isEmpty();

        Gen<User> validUserGen = userGen.suchThat(isValid);

        Gen<User> invalidUserGen = userGen.suchThat(isValid.negate());

        validUserGen.sample(100)
                    .forEach(System.out::println);


        Prism<Exception, RuntimeException> prism =
                new Prism<>(e -> {
                    if (e instanceof RuntimeException) return Optional.of(((RuntimeException) e));
                    return Optional.empty();
                },
                            r -> r);

        Prism<String, Integer> intPrism =
                new Prism<>(str -> {
                    try {
                        return Optional.of(Integer.parseInt(str));
                    } catch (NumberFormatException e) {
                        return Optional.empty();
                    }
                },
                            integer -> Integer.toString(integer)
                );


        Gen<String> gen =  StrGen.biased(0,3);

        gen.sample(10).forEach(x -> {
            System.out.println(x.length());
            System.out.println(x);
        });

        Gen<Integer> seq = Gen.seq(n-> n);

        seq.sample(5).forEach(System.out::println);

    }







}
