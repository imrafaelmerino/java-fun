/**
 * Data generators and combinators for property-based and statistical testing.
 * <p>
 * The package centers around {@link fun.gen.Gen}, a composable abstraction that turns a
 * {@link java.util.random.RandomGenerator} into a supplier of values. Concrete generators
 * cover primitive domains ({@link fun.gen.IntGen}, {@link fun.gen.DoubleGen},
 * {@link fun.gen.StrGen}), containers ({@link fun.gen.ListGen}, {@link fun.gen.SetGen},
 * {@link fun.gen.MapGen}), tuples, and record-like structures ({@link fun.gen.MyRecordGen}).
 * <p>
 * {@link fun.gen.Combinators} provides higher-level operators for weighted choices, nullable
 * values, subsets/combinations, and generator composition patterns.
 */
package fun.gen;
