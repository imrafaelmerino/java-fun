/**
 * This package provides a collection of optics for functional programming and manipulation of data structures.
 * Optics are abstractions that allow you to view, modify, and manipulate parts of complex data structures in a
 * functional and composable way.
 * <p>
 * - {@link fun.optic.Lens}: A Lens is an optic that enables you to focus on a specific part of a data structure and provides
 * functions for getting, setting, and modifying that part. It is typically used for Product types (e.g., records, tuples)
 * and their components.
 * <p>
 * - {@link fun.optic.Option}: An Option is an optic that combines the properties of both Lens and Prism. It allows you to view,
 * modify, and manipulate an optional focus within a data structure. It is a weaker Lens and Prism.
 * <p>
 * - {@link fun.optic.Prism}: A Prism is an optic that allows you to encode the relationship between Sum or CoProduct types and
 * their individual elements. It provides functions for getting, setting, and modifying optional targets within
 * a data structure.
 * <p>
 * These optics promote functional programming practices, immutability, and composability when working with complex
 * data structures. They are essential tools for dealing with nested and structured data, making code more concise,
 * readable, and maintainable.
 */
package fun.optic;