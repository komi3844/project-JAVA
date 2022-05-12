package com.mmtr.finance.parser.utils;

import java.util.HashMap;
import java.util.Map;

public interface MapFromArrays {
    static <K, V> Map<K, V> convert(K[] keys, V[] values) {
        HashMap<K, V> result = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            result.put(keys[i], values[i]);
        }
        return result;
    }
}
