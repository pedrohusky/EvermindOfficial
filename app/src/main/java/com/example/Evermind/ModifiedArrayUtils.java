package com.example.Evermind;

import java.lang.reflect.Array;


/**
 * ModifiedArrayUtils contains some methods that you can call to find out
 * the most efficient increments by which to grow arrays.
 *
 *
 * Modified newUnpadded*Array methods because VMRuntime is not accessible
 */
public class ModifiedArrayUtils {
    private static final int CACHE_SIZE = 73;
    private static Object[] sCache = new Object[CACHE_SIZE];

    private ModifiedArrayUtils() { /* cannot be instantiated */ }

    public static char[] newUnpaddedCharArray(int minLen) {
        return new char[minLen];
    }

    public static int[] newUnpaddedIntArray(int minLen) {
        return new int[minLen];
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] newUnpaddedArray(Class<T> clazz, int minLen) {
        return (T[]) Array.newInstance(clazz, minLen);
    }

    /**
     * Returns an empty array of the specified type.  The intent is that
     * it will return the same empty array every time to avoid reallocation,
     * although this is not guaranteed.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] emptyArray(Class<T> kind) {
        if (kind == Object.class) {
            return (T[]) ModifiedEmptyArray.OBJECT;
        }

        int bucket = (kind.hashCode() & 0x7FFFFFFF) % CACHE_SIZE;
        Object cache = sCache[bucket];

        if (cache == null || cache.getClass().getComponentType() != kind) {
            cache = Array.newInstance(kind, 0);
            sCache[bucket] = cache;

            // Log.e("cache", "new empty " + kind.getName() + " at " + bucket);
        }

        return (T[]) cache;
    }
}
