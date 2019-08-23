import jsonvalues.JsPath;
import jsonvalues.UserError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJsPath
{


    @Test
    public void to_string()
    {

        Assertions.assertEquals("/a/b/0/1",
                                JsPath.of("/a/b/0/1")
                                      .toString()
                               );
        Assertions.assertEquals("/a/b/0/1",
                                JsPath.of("#/a/b/0/1")
                                      .toString()
                               );
        Assertions.assertEquals("/b/0/1",
                                JsPath.of("/b/0/1")
                                      .toString()
                               );
        Assertions.assertEquals("/a b/'0'/0/1",
                                JsPath.of("#/a+b/'0'/0/1")
                                      .toString()
                               );
        Assertions.assertEquals("/'/'0'/0/1/",
                                JsPath.of("#/%27/'0'/0/1/")
                                      .toString()
                               );
        Assertions.assertEquals("/'/'0'/0/1/",
                                JsPath.of("/'/'0'/0/1/")
                                      .toString()
                               );

    }

    @Test
    public void jspath_comparator()
    {
        Assertions.assertEquals("a".compareTo("b"),
                                JsPath.of("/a/b")
                                      .compareTo(JsPath.of("/b/a"))
                               );

        Assertions.assertEquals(0,
                                JsPath.of("/a/b")
                                      .compareTo(JsPath.of("/a/b"))
                               );

        Assertions.assertEquals("1".compareTo("a"),
                                JsPath.of("/1/b")
                                      .compareTo(JsPath.of("/a/b"))
                               );
    }

    @Test
    public void map_keys()
    {

        Assertions.assertEquals(JsPath.of("/a!/1/b!/1/c!"),
                                JsPath.of("/a/1/b/1/c")
                                      .mapKeys(it -> it.concat("!"))
                               );
    }
    @Test
    public void equals()
    {
        Assertions.assertEquals(JsPath.of("#/a/b/c/d/0//%20"),
                                JsPath.of("#/a/b/c/d/0//+"));
        Assertions.assertEquals(JsPath.of("#/a/b/c/d/0//%20").hashCode(),
                                JsPath.of("#/a/b/c/d/0//+").hashCode());

        Assertions.assertEquals(JsPath.of("/1/2/3"),
                                JsPath.of("/1/2/3"));

        Assertions.assertEquals(JsPath.of("/1/2/3").hashCode(),
                                JsPath.of("/1/2/3").hashCode());

        Assertions.assertEquals(JsPath.of("#/a/1//+/'"),
                                JsPath.of("#/a/1//+/%27")
                               );

        Assertions.assertEquals(JsPath.of("#/a/1//+/'").hashCode(),
                                JsPath.of("#/a/1//+/%27").hashCode()
                               );


    }

    @Test
    public void path_dec(){
        Assertions.assertEquals(JsPath.of("/a/b/0"),JsPath.of("/a/b/1").dec());

        Assertions.assertThrows(UserError.class,()->JsPath.of("a").dec());
    }

    @Test
    public void test_position(){
        Assertions.assertTrue(JsPath.of("/a").last().isKey());
        Assertions.assertFalse(JsPath.of("/a").last().isIndex());
        Assertions.assertTrue(JsPath.of("/a").last().isKey(i->i.equals("a")));
        Assertions.assertThrows(UserError.class, ()->JsPath.of("/0").last().asKey());

        Assertions.assertTrue(JsPath.of("/0").last().isIndex());
        Assertions.assertFalse(JsPath.of("/0").last().isKey());
        Assertions.assertTrue(JsPath.of("/0").last().isIndex(i->i==0));
        Assertions.assertThrows(UserError.class,()->JsPath.of("/a").last().asIndex());

    }

    @Test
    public void empty_key_equals(){
        Assertions.assertEquals(JsPath.empty().key(""), JsPath.of("#/"));
        Assertions.assertEquals(JsPath.empty().key(""), JsPath.of("/"));
        Assertions.assertEquals(JsPath.empty().key("."), JsPath.of("#/%2E"));
        Assertions.assertEquals(JsPath.of("#/a/b/+/1"), JsPath.of("#/a/b/%20/1"));
        Assertions.assertEquals(JsPath.of("#/a/b/+/1"), JsPath.of("/a/b/ /1"));
    }

    @Test
    public void test_starts_with()
    {

        final JsPath path = JsPath.of("/a/b/c/0/1");
        Assertions.assertTrue(path.startsWith(JsPath.empty()));
        Assertions.assertTrue(path.startsWith(JsPath.of("/a")));
        Assertions.assertTrue(path.startsWith(JsPath.of("/a/b")));
        Assertions.assertTrue(path.startsWith(JsPath.of("/a/b/c")));
        Assertions.assertTrue(path.startsWith(JsPath.of("/a/b/c/0")));
        Assertions.assertTrue(path.startsWith(JsPath.of("#/a/b/c/0/1")));
        Assertions.assertFalse(path.startsWith(JsPath.of("/a/b/c/0/1/a")));
    }
    @Test
    public void test_ends_with()
    {
        final JsPath path = JsPath.of("/a/b/c/0/1");
        Assertions.assertTrue(path.endsWith(JsPath.empty()));
        Assertions.assertTrue(path.endsWith(JsPath.of("/1")));
        Assertions.assertTrue(path.endsWith(JsPath.of("/0/1")));
        Assertions.assertTrue(path.endsWith(JsPath.of("/c/0/1")));
        Assertions.assertTrue(path.endsWith(JsPath.of("/b/c/0/1")));
        Assertions.assertTrue(path.endsWith(JsPath.of("/a/b/c/0/1")));
        Assertions.assertFalse(path.endsWith(JsPath.of("/a/b/c/0/1/a")));
    }
    @Test
    public void test_same(){
        Assertions.assertEquals(JsPath.of("/a/b/c/d/0"),
                                JsPath.of("#/a/b/c/d/0")
                               );
    }

}
