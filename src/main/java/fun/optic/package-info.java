/**
 * Functional optics for immutable updates and composable reads.
 * <p>
 * Core abstractions:
 * <ul>
 *   <li>{@link fun.optic.Lens}: total focus ({@code S -> T}).</li>
 *   <li>{@link fun.optic.Prism}: partial focus ({@code S -> Optional<T>}).</li>
 *   <li>{@link fun.optic.Option}: optional focus with update support.</li>
 * </ul>
 */
package fun.optic;
