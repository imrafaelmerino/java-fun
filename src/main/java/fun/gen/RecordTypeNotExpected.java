package fun.gen;

public final class RecordTypeNotExpected extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private static final String TEMPLATE =
            "The type you expect of the value associated to the key %s is %s, however the " +
            "real type is %s. Consider using other getXXX method or the generator is producing not-expected values";
    public RecordTypeNotExpected(String message) {
        super(message);
    }

    public RecordTypeNotExpected(String typeExpected,Class<?> realType, String key){
        this(String.format(TEMPLATE,typeExpected,key,realType.getName()));
    }
}
