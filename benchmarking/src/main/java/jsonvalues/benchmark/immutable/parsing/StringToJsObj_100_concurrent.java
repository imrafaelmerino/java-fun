package jsonvalues.benchmark.immutable.parsing;

import jsonvalues.MalformedJson;
import jsonvalues.ScalaFactory;
import jsonvalues.benchmark.ExecutorState;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class StringToJsObj_100_concurrent
{
    private final static int NUMBER_TASKS = 50;

    private static final String object = jsonvalues.benchmark.Data.OBJ_100.get();

    @Benchmark
    public boolean scala_hash_map( final ExecutorState e
                                  ) throws InterruptedException
    {

        CountDownLatch count = new CountDownLatch(NUMBER_TASKS);
        for (int i = 0; i < NUMBER_TASKS; i++)
        {


            e.service.submit(() ->
                             {
                                 try
                                 {
                                     return ScalaFactory.hmv.object.parse(object).orElseThrow();
                                 }
                                 catch (MalformedJson ex)
                                 {
                                     throw new RuntimeException(ex);
                                 }
                                 finally
                                 {
                                     count.countDown();
                                 }
                             });
        }

        return count.await(10,
                           TimeUnit.SECONDS
                          );
    }

    ;

    @Benchmark
    public boolean scala_tree_map(final ExecutorState e) throws InterruptedException
    {
        CountDownLatch count = new CountDownLatch(NUMBER_TASKS);
        for (int i = 0; i < NUMBER_TASKS; i++)
        {


            e.service.submit(() ->
                             {
                                 try
                                 {
                                     return ScalaFactory.tmv.object.parse(object).orElseThrow();
                                 }
                                 catch (MalformedJson ex)
                                 {
                                     throw new RuntimeException(ex);
                                 }
                                 finally
                                 {
                                     count.countDown();
                                 }
                             });
        }

        return count.await(10,
                           TimeUnit.SECONDS
                          );

    }
//
//    @Benchmark
//    public boolean clojure_hash_map(final ExecutorState e) throws InterruptedException
//    {
//
//        for (int i = 0; i < POOL_SIZE; i++)
//            Executor.pool.submit(() -> ClojureFactory.hmv.object.parse(object)
//                                );
//        Executor.pool.shutdown();
//        return Executor.pool.awaitTermination(10,
//                                              TimeUnit.SECONDS
//                                             );
//    }
//
//    @Benchmark
//    public boolean clojure_tree_map(final ExecutorState e) throws InterruptedException
//    {
//
//        for (int i = 0; i < POOL_SIZE; i++)
//            Executor.pool.submit(() -> ClojureFactory.tmv.object.parse(object)
//                                );
//        Executor.pool.shutdown();
//        return Executor.pool.awaitTermination(10,
//                                              TimeUnit.SECONDS
//                                             );
//    }
//
//    @Benchmark
//    public boolean clojure_array_map(final ExecutorState e) throws InterruptedException
//    {
//
//        for (int i = 0; i < POOL_SIZE; i++)
//            Executor.pool.submit(() -> ClojureFactory.amv.object.parse(object)
//                                );
//        Executor.pool.shutdown();
//        return Executor.pool.awaitTermination(10,
//                                              TimeUnit.SECONDS
//                                             );
//    }
//
//    @Benchmark
//    public boolean vavr_hash_map(final ExecutorState e) throws InterruptedException
//    {
//
//        for (int i = 0; i < POOL_SIZE; i++)
//            Executor.pool.submit(() -> VavrFactory.hmv.object.parse(object)
//                                );
//        Executor.pool.shutdown();
//        return Executor.pool.awaitTermination(10,
//                                              TimeUnit.SECONDS
//                                             );
//    }
//
//    @Benchmark
//    public boolean vavr_tree_map(final ExecutorState e) throws InterruptedException
//    {
//
//        for (int i = 0; i < POOL_SIZE; i++)
//            Executor.pool.submit(() -> VavrFactory.tmv.object.parse(object)
//                                );
//        Executor.pool.shutdown();
//        return Executor.pool.awaitTermination(10,
//                                              TimeUnit.SECONDS
//                                             );
//    }
//
//    @Benchmark
//    public boolean pcollections_hash_map(final ExecutorState e) throws InterruptedException
//    {
//
//        for (int i = 0; i < POOL_SIZE; i++)
//            Executor.pool.submit(() -> PCollectionsFactory.hmv.object.parse(object)
//                                );
//        Executor.pool.shutdown();
//        return Executor.pool.awaitTermination(10,
//                                              TimeUnit.SECONDS
//                                             );
//
//    }
//
//    public static void main(String[] args) throws InterruptedException
//    {
//        new StringToJsObj_100_concurrent().scala_hash_map();
//    }


}
