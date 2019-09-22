package jsonvalues.myseqs.immutable;


import jsonvalues.ImmutableSeq;
import jsonvalues.JsElem;
import scala.collection.JavaConverters;
import scala.collection.Seq;
import scala.collection.generic.CanBuildFrom;
import scala.collection.immutable.List;
import scala.collection.mutable.Builder;

import java.util.Iterator;

public class ScalaList implements ImmutableSeq
{
    private static final CanBuildFrom<List<JsElem>, JsElem, List<JsElem>> bf = new CanBuildFrom<List<JsElem>, JsElem, List<JsElem>>()
    {
        @Override
        public Builder<JsElem, List<JsElem>> apply()
        {
            return List.<JsElem>canBuildFrom().apply();
        }

        @Override
        public Builder<JsElem, List<JsElem>> apply(final List<JsElem> v)
        {
            return List.<JsElem>canBuildFrom().apply();
        }
    };

    private static final CanBuildFrom<Seq<JsElem>, JsElem, List<JsElem>> bf1 = new CanBuildFrom<Seq<JsElem>, JsElem, List<JsElem>>()
    {
        @Override
        public Builder<JsElem, List<JsElem>> apply()
        {
            return List.<JsElem>canBuildFrom().apply();
        }

        @Override
        public Builder<JsElem, List<JsElem>> apply(final Seq<JsElem> jsElemSeq)
        {
            return List.<JsElem>canBuildFrom().apply();
        }
    };

    private final List<JsElem> list;
    private static final List<JsElem> EMPTY = List.<JsElem>empty();


    public ScalaList()
    {
        list = EMPTY;
    }

    private ScalaList(final List<JsElem> list)
    {
        this.list = list;
    }

    @Override
    public JsElem head()
    {
        return list.head();
    }

    @Override
    public Iterator<JsElem> iterator()
    {
        final scala.collection.Iterator<JsElem> it = list.iterator()
                                                         .toIterator();
        return JavaConverters.<JsElem>asJavaIterator(it);
    }

    @Override
    public ImmutableSeq tail()
    {
        final List<JsElem> tail = (List<JsElem>) list.tail();
        return new ScalaList(tail);
    }

    @Override
    public ImmutableSeq init()
    {
        final List<JsElem> init = (List<JsElem>) list.init();
        return new ScalaList(init);
    }

    @Override
    public JsElem last()
    {
        return list.last();
    }

    @Override
    public JsElem get(final int i)
    {
        List<JsElem> l = list;
        for (int j = i; j > 0; j--) l = (List<JsElem>) l.tail();
        return l.head();
    }

    @Override
    public int size()
    {
        return list.size();
    }

    @Override
    public boolean isEmpty()
    {
        return list.isEmpty();
    }

    @Override
    public boolean contains(final JsElem jsElem)
    {
        return false;
    }

    @Override
    public ImmutableSeq appendFront(final JsElem e)
    {
        return new ScalaList(list.<JsElem>$colon$colon(e));
    }

    @Override
    public ImmutableSeq appendBack(final JsElem e)
    {

        return new ScalaList(list.$colon$plus(e,
                                              bf1
                                             ));
    }

    @Override
    public ImmutableSeq update(final int i,
                               final JsElem e
                              )
    {
        if (i == 0)
        {
            final List<JsElem> xs = (List<JsElem>) list.tail();
            return new ScalaList(xs.<JsElem>$colon$colon(e));
        }
        final List<JsElem> xs = list.take(i);
        final List<JsElem> ys = list.drop(i + 1);
        final List<JsElem> zs = xs.$plus$plus(ys.<JsElem>$colon$colon(e),
                                              bf
                                             );
        return new ScalaList(zs);
    }

    @Override
    public ImmutableSeq add(final int i,
                            final JsElem e
                           )
    {
        if (i == 0) return new ScalaList(list.<JsElem>$colon$colon(e));
        final List<JsElem> xs = list.take(i);
        final List<JsElem> ys = list.drop(i).<JsElem>$colon$colon(e);
        final List<JsElem> zs = xs.$plus$plus(ys,
                                              bf
                                             );
        return new ScalaList(zs);
    }

    @Override
    public ImmutableSeq remove(final int i)
    {
        if (i == 0) return new ScalaList((List<JsElem>) list.tail());
        final List<JsElem> xs = list.take(i);
        final List<JsElem> ys = list.drop(i + 1);
        final List<JsElem> zs = xs.$plus$plus(ys,
                                              bf
                                             );
        return new ScalaList(zs);
    }

}
