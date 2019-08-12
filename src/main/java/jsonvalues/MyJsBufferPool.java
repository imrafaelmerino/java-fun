package jsonvalues;

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * char[] pool that pool instances of char[] which are expensive to create.
 *
 */
class MyJsBufferPool
{
    // volatile since multiple threads may access queue reference
    private
    volatile WeakReference<ConcurrentLinkedQueue<char[]>> queue = new WeakReference<>(new ConcurrentLinkedQueue<>());

    /**
     * Gets a new object from the pool.
     *
     * If no object is available in the pool, this method creates a new one.
     *
     * @return always non-null.
     */
    final char[] take()
    {
        return Optional.ofNullable(getQueue().poll())
                       .orElseGet(() -> new char[4096]);

    }

    private ConcurrentLinkedQueue<char[]> getQueue()
    {
        ConcurrentLinkedQueue<char[]> d;
        d = queue.get();
        if (d != null) return d;
        d = new ConcurrentLinkedQueue<>();
        queue = new WeakReference<>(d);
        return d;
    }

    /**
     * Returns an object back to the pool.
     */
    final void recycle(char[] t)
    {
        getQueue().offer(t);
    }

}
