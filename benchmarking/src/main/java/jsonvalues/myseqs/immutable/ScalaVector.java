package jsonvalues.myseqs.immutable;

import jsonvalues.ImmutableSeq;
import jsonvalues.JsElem;
import scala.Tuple2;
import scala.collection.generic.CanBuildFrom;
import scala.collection.immutable.Vector;
import scala.collection.mutable.Builder;
import scala.jdk.CollectionConverters;

public final class ScalaVector implements ImmutableSeq
{

    private static final Vector<JsElem> EMPTY_VECTOR = new scala.collection.immutable.Vector<>(0,
                                                                                               0,
                                                                                               0
    );
    private static final CanBuildFrom<Vector<JsElem>, JsElem, Vector<JsElem>> bf = new CanBuildFrom<Vector<JsElem>, JsElem, Vector<JsElem>>()
    {
        @Override
        public Builder<JsElem, Vector<JsElem>> apply()
        {
            return scala.collection.immutable.Vector.<JsElem>canBuildFrom().apply();
        }

        @Override
        public Builder<JsElem, Vector<JsElem>> apply(final Vector<JsElem> v)
        {
            return scala.collection.immutable.Vector.<JsElem>canBuildFrom().apply();
        }
    };

    private final Vector<JsElem> vector;

    public ScalaVector()
    {
        this.vector = EMPTY_VECTOR;
    }

    private ScalaVector(final Vector<JsElem> vector)
    {
        this.vector = vector;
    }

    @Override
    public ScalaVector appendBack(final JsElem elem)
    {
        return new ScalaVector(vector.appendBack(elem));
    }

    @Override
    public ScalaVector appendFront(final JsElem elem)
    {
        return new ScalaVector(vector.appendFront(elem));
    }

    @Override
    public boolean contains(final JsElem e)
    {
        return vector.contains(e);
    }

    @Override
    public JsElem get(final int index)
    {
        return vector.apply(index);
    }

    @Override
    public int hashCode()
    {
        return vector.hashCode();
    }

    @Override
    public JsElem head()
    {
        if (this.isEmpty()) throw new UnsupportedOperationException("head of empty");

        return vector.head();
    }

    @Override
    public ScalaVector init()
    {
        if (this.isEmpty()) throw new UnsupportedOperationException("init of empty");

        return new ScalaVector(vector.init());
    }

    @Override
    public boolean isEmpty()
    {
        return vector.isEmpty();
    }

    @Override
    public java.util.Iterator<JsElem> iterator()
    {
        return CollectionConverters.IterableHasAsJava(vector)
                                   .asJava()
                                   .iterator();
    }

    @Override
    public JsElem last()
    {
        if (this.isEmpty()) throw new UnsupportedOperationException("last of empty");

        return vector.last();
    }

    @Override
    public String toString()
    {
        if (vector.isEmpty()) return "[]";

        return vector.mkString("[",
                               ",",
                               "]"
                              );
    }

    @Override
    @SuppressWarnings("squid:S00117") // api de scala uses $ to name methods
    public ScalaVector remove(final int index)
    {
        if (index == 0) return new ScalaVector(vector.tail());
        if (index == vector.size() - 1) return new ScalaVector(vector.init());

        Tuple2<Vector<JsElem>, Vector<JsElem>> tuple = vector.splitAt(index);
        return new ScalaVector(tuple._1.$plus$plus(tuple._2.tail(),
                                                   bf
                                                  ));
    }

    @Override
    public ScalaVector add(final int index,
                           final JsElem ele
                          )
    {

        if (index == 0) return new ScalaVector(vector.appendFront(ele));
        if (index == -1 || index == vector.size()) return new ScalaVector(vector.appendBack(ele));
        if (index < -1 || index > vector.size()) throw new IndexOutOfBoundsException();
        Tuple2<Vector<JsElem>, Vector<JsElem>> tuple = vector.splitAt(index);
        return new ScalaVector(tuple._1.appendBack(ele)
                                       .$plus$plus(tuple._2,
                                                   bf
                                                  ));
    }

    @Override
    public int size()
    {
        return vector.size();
    }


    @Override
    public ScalaVector tail()
    {
        if (this.isEmpty()) throw new UnsupportedOperationException("tail of empty");

        return new ScalaVector(vector.tail());
    }

    @Override
    public ScalaVector update(final int index,
                              final JsElem ele
                             )
    {
        return new ScalaVector(vector.updateAt(index,
                                               ele
                                              ));
    }


}
