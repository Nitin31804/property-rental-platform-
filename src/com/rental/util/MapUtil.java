package com.rental.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
    public static Map<String, Object> of() {
        return new HashMap<>();
    }
    public static Map<String, Object> of(String k1, Object v1) {
        Map<String, Object> m = new HashMap<>();
        m.put(k1, v1);
        return m;
    }
    public static Map<String, Object> of(String k1, Object v1, String k2, Object v2) {
        Map<String, Object> m = new HashMap<>();
        m.put(k1, v1);
        m.put(k2, v2);
        return m;
    }
    public static Map<String, Object> of(String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        Map<String, Object> m = new HashMap<>();
        m.put(k1, v1);
        m.put(k2, v2);
        m.put(k3, v3);
        return m;
    }
    public static Map<String, Object> of(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4) {
        Map<String, Object> m = new HashMap<>();
        m.put(k1, v1);
        m.put(k2, v2);
        m.put(k3, v3);
        m.put(k4, v4);
        return m;
    }
}
