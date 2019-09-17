package jsonvalues;

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * char[] pool that pool instances of char[] which are expensive to create.
 *
 */
final class JsBufferPool
{
    private final ConcurrentLinkedQueue<WeakReference<char[]>> queue = new ConcurrentLinkedQueue<>();
    private static final int ARRAY_SIZE = 4096;


    /**
     * Gets a new object from the pool.
     *
     * If no object is available in the pool, this method creates a new one.
     *
     * @return always non-null.
     */
    final char[] take()
    {
        WeakReference<char[]> weakReference = Optional.ofNullable(queue.poll())
                                                      .orElseGet(() -> new WeakReference<>(new char[ARRAY_SIZE]));
        return Optional.ofNullable(weakReference.get())
                       .orElseGet(() -> new char[ARRAY_SIZE]);
    }


    /**
     * Returns an object back to the pool.
     */
    final void recycle(char[] t)
    {
        queue.offer(new WeakReference<>(t));
    }

}
