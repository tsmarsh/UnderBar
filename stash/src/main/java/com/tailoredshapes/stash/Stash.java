package com.tailoredshapes.stash;

import com.google.gson.Gson;
import com.tailoredshapes.underbar.dates.Dates;
import com.tailoredshapes.underbar.ocho.UnderBar;
import com.tailoredshapes.underbar.ocho.UnderString;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.function.*;

import static com.tailoredshapes.underbar.dates.Dates.isoString;
import static com.tailoredshapes.underbar.ocho.Die.*;
import static com.tailoredshapes.underbar.ocho.UnderBar.*;
import static com.tailoredshapes.underbar.ocho.UnderString.urlEncode;

import static java.util.Optional.ofNullable;

/**
 * A STring hASH.
 * <p>
 * All keys are Strings.
 * <p>
 * With basic functionality for type safe access to members
 */
public class Stash extends HashMap<String, Object> implements Cloneable {
    
    public Stash(Map<String, ?> m) {
        each(m, this::update);
    }

    public Stash() {
    }

    public static <T> Stash stashFromPairs(Iterable<T> ts, Function<T, Map.Entry<String, Object>> toEntry) {
        return new Stash(mapFromEntry(ts, toEntry));
    }

    public static <V> Stash stashFromKeys(Iterable<String> ks, Function<String, V> toV) {
        return stashFromPairs(ks, k -> UnderBar.entry(k, toV.apply(k)));
    }

    public static Stash parseJSON(String json) {
        Gson gson = new Gson();
        return new Stash(dieIfNull(gson.fromJson(json, Stash.class), () -> "error parsing JSON: " + json));
    }

    public static Stash stash() {
        return new Stash();
    }

    /**
     * Helper methods for creating a stash
     *
     * @param k1
     * @param v1
     * @return
     */
    public static Stash stash(String k1, Object v1) {
        return stash().update(k1, v1);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2) {
        return stash(k1, v1).update(k2, v2);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        return stash(k1, v1, k2, v2).update(k3, v3);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4) {
        return stash(k1, v1, k2, v2, k3, v3).update(k4, v4);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5) {
        return stash(k1, v1, k2, v2, k3, v3, k4, v4).update(k5, v5);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6) {
        return stash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5).update(k6, v6);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7) {
        return stash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6).update(k7, v7);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8) {
        return stash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7).update(k8, v8);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9) {
        return stash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8).update(k9, v9);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10) {
        return stash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9).update(k10, v10);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11) {
        return stash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10).update(k11, v11);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12) {
        return stash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11).update(k12, v12);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12, String k13, Object v13) {
        return stash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11, k12, v12).update(k13, v13);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12, String k13, Object v13, String k14, Object v14) {
        return stash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11, k12, v12, k13, v13).update(k14, v14);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12, String k13, Object v13, String k14, Object v14, String k15, Object v15) {
        return stash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11, k12, v12, k13, v13, k14, v14).update(k15, v15);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12, String k13, Object v13, String k14, Object v14, String k15, Object v15, String k16, Object v16) {
        return stash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15).update(k16, v16);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12, String k13, Object v13, String k14, Object v14, String k15, Object v15, String k16, Object v16, String k17, Object v17) {
        return stash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16).update(k17, v17);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12, String k13, Object v13, String k14, Object v14, String k15, Object v15, String k16, Object v16, String k17, Object v17, String k18, Object v18) {
        return stash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17).update(k18, v18);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12, String k13, Object v13, String k14, Object v14, String k15, Object v15, String k16, Object v16, String k17, Object v17, String k18, Object v18, String k19, Object v19) {
        return stash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18).update(k19, v19);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12, String k13, Object v13, String k14, Object v14, String k15, Object v15, String k16, Object v16, String k17, Object v17, String k18, Object v18, String k19, Object v19, String k20, Object v20) {
        return stash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18, k19, v19).update(k20, v20);
    }

    public static Stash stash(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4, String k5, Object v5, String k6, Object v6, String k7, Object v7, String k8, Object v8, String k9, Object v9, String k10, Object v10, String k11, Object v11, String k12, Object v12, String k13, Object v13, String k14, Object v14, String k15, Object v15, String k16, Object v16, String k17, Object v17, String k18, Object v18, String k19, Object v19, String k20, Object v20, String k21, Object v21) {
        return stash(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18, k19, v19, k20, v20).update(k21, v21);
    }

    /**
     * Updates a smash in place
     *
     * @param k
     * @param v
     * @return
     */
    public Stash update(String k, Object v) {
        if (v == null || v.equals(""))
            this.put(k, null);
        else
            this.put(k, v);
        return this;
    }
    

    /**
     * Returns a copy of nonce Stash apply new values
     *
     * @param k
     * @param v
     * @return
     */
    public Stash assoc(String k, Object v) {
        return new Stash(this).update(k, v);
    }

    public Stash dissoc(String k) {
        Stash newStash = new Stash(this);
        newStash.remove(k);
        return newStash;
    }

    public Number number(String key) {
        return getCast(key, x -> (Number) x);
    }


    public <T> Optional<T> optional(String key) {
        return getCast(key, t -> t);
    }

    public <T> Optional<T> maybe(String key) {
        if (!contains(key) || isNull(key)) return UnderBar.optional();
        if (type(key) == Optional.class) return optional(key);
        return UnderBar.optional(get(key));
    }

    public <T> T get(String key) {
        return getCast(key, x -> x);
    }

    public <T> T get(String key, Class<T> clazz) {
        return getCast(key, x -> x);
    }

    public <T> T get(String k, T missing) {
        return (T) this.getOrDefault(k, missing);
    }

    //Accessors

    public long l(String k) {
        return getCast(k, t -> (long) t);
    }

    public double d(String k) {
        return ((Number) getCast(k, x -> x)).doubleValue();
    }

    public int i(String k) {
        return getCast(k, x -> (int) x);
    }

    public short s(String k) {
        return ((Number) getCast(k, x -> x)).shortValue();
    }


    public float f(String k) {
        return ((Number) getCast(k, x -> x)).floatValue();
    }

    public byte b(String k) {
        return ((Number) getCast(k, x -> x)).byteValue();
    }


    public boolean bool(String k) {
        return ((Boolean) getCast(k, x -> x)); //Because you can't use logic on Boolean
    }

    //Coercion

    public long asLong(String k) {
        return ((Number) getCast(k, x -> x)).longValue();
    }

    public int asInteger(String k) {
        Number value = getCast(k, x -> (Number) x);
        return tap(value.intValue(), result ->
                dieUnless(result == value.longValue(), () -> value + " to large for int"));
    }

    public double asDouble(String k) {
        return ((Number) getCast(k, x -> x)).doubleValue();
    }

    public float asFloat(String k) {
        Number value = getCast(k, x -> (Number) x);
        return tap(value.floatValue(), result ->
                dieUnless(result == value.longValue(), () -> value + " to large for float"));
    }

    public short asShort(String k) {
        Number value = getCast(k, x -> (Number) x);
        return tap(value.shortValue(), result ->
                dieUnless(result == value.shortValue(), () -> value + " to large for short"));
    }

    public byte asByte(String k) {
        Number value = getCast(k, x -> (Number) x);
        return tap(value.byteValue(), result ->
                dieUnless(result == value.byteValue(), () -> value + " to large for byte"));
    }

    public String asString(String k) {
        return  super.get(k).toString();
    }

    public Stash asStash(String key) {
        return new Stash((Map<String, Object>) getCast(key, x -> x));
    }

    public List<Stash> asStashes(String key) {
        return UnderBar.map((Collection<Map<String, Object>>) getCast(key, x -> x), Stash::new);
    }

    public Path asPath(String key) {
        return Paths.get(get(key));
    }


    //String to ...


    public int parseInteger(String k) {
        return Integer.parseInt((String) super.get(k));
    }

    public int parseInteger(String k, int missing) {
        return ofNullable((String) super.get(k)).map(Integer::parseInt).orElse(missing);
    }

    public long parseLong(String k) {
        return Long.parseLong((String) super.get(k));
    }

    public long parseLong(String key, long missing) {
        return ofNullable((String) super.get(key)).map(Long::parseLong).orElse(missing);
    }

    public double parseDouble(String k) {
        return Double.parseDouble((String) super.get(k));
    }

    public double parseDouble(String key, double missing) {
        return ofNullable((String) super.get(key)).map(Double::parseDouble).orElse(missing);
    }

    public float parseFloat(String k) {
        return Float.parseFloat((String) super.get(k));
    }

    public float parseFloat(String key, float missing) {
        return ofNullable((String) super.get(key)).map(Float::parseFloat).orElse(missing);
    }

    public boolean parseBoolean(String k) {
        return Boolean.parseBoolean((String) super.get(k));
    }

    public boolean parseBoolean(String key, boolean missing) {
        return ofNullable((String) super.get(key)).map(Boolean::parseBoolean).orElse(missing);
    }

    public short parseShort(String k) {
        return Short.parseShort((String) super.get(k));
    }

    public short parseShort(String key, short missing) {
        return ofNullable((String) super.get(key)).map(Short::parseShort).orElse(missing);
    }

    public Stash parseJson(String key) {
        return parseJSON(get(key));
    }


    public Stash parseJson(String key, Stash missing) {
        return ofNullable((String) super.get(key)).map(Stash::parseJSON).orElse(missing);
    }

    public Date parseDate(String key) {
        return Dates.date((String) get(key));
    }

    public Instant parseInstant(String k) {
        return Dates.instant(get(k));
    }

    public UUID parseUUID(String key) {
        return UUID.fromString(get(key));
    }

    public <T> List<T> map(BiFunction<String, Object, T> toT) {
        return UnderBar.map(this, toT);
    }

    public Map<String, Object> putInto(Map<String, Object> other) {
        other.putAll(this);
        return other;
    }

    public Stash putInto(Stash other) {
        other.putAll(this);
        return other;
    }

    public boolean contains(String key) {
        return this.containsKey(key);
    }

    public boolean containsValue(String key) {
        return contains(key) &&  super.get(key) != null && (!( super.get(key) instanceof Optional) || ((Optional)  super.get(key)).isPresent());
    }


    private <T> T getCast(String k, Function<T, T> onT) {
        Object value = dieIfMissing(this, k);
        return rethrow(() -> onT.apply((T) value),
                () -> "Value at key " + k + ":" + value + " is a " + value.getClass());
    }


//    @Override
//    public String toJSONString() {
//        return JSONValue.toJSONString(modifyValues(m, this::toJSONString));
//    }


    public String toJSONString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


//
//    private Object toJSONString(Object v) {
//        if (v instanceof Optional<?>) return toJSONString(((Optional<?>) v).orElse(null));
//        if (v instanceof Date) return isoString((Date) v);
//        if (v instanceof Instant) return isoString((Instant) v);
//        if (v instanceof UUID) return v.toString();
//        if (v instanceof File) return ((File) v).getAbsolutePath();
//        if (v instanceof Path) return v.toString();
//        if (v instanceof Map) return new Stash((Map<String, Object>) v).toJSONString();
//        return v;
//    }

    public Stash filterKeys(Predicate<String> predicate) {
        return new Stash(UnderBar.filterKeys(this, predicate));
    }

    public Stash rejectKeys(String... keys) {
        return rejectKeys(UnderBar.list(keys));
    }

    public Stash selectKeys(String... keys) {
        Set<String> keySet = set(keys);
        return filterKeys(keySet::contains);
    }

    public Stash selectKeys(Iterable<String> keys) {
        Set<String> keySet = set(keys);
        return filterKeys(keySet::contains);
    }

    public Stash rejectKeys(List<String> keys) {
        Set<String> keySet = set(keys);
        return filterKeys(k -> !keySet.contains(k));
    }

    public Map<String, Object> toMap() {
        return new HashMap<>(this);
    }

    public Map<String, Object> toMapDeep() {
        Map<String, Object> result = toMap();

        result.forEach((key, value) -> {
            if (value instanceof Stash) {
                result.put(key, ((Stash) value).toMapDeep());
            } else if (value instanceof Iterable<?>) {
                Iterable<?> v = (Iterable<?>) value;
                List<Object> convertedValue = UnderBar.map(v, (element) -> {
                    if (element instanceof Stash) {
                        return ((Stash) element).toMapDeep();
                    }
                    return element;
                });
                result.put(key, convertedValue);
            }
        });
        return result;
    }

    public <T> T remove(String key) {
        return (T) super.remove(key);
    }

    public boolean hasContent(String key) {
        return contains(key) && UnderString.hasContent((String)  super.get(key));
    }

    public Stash merge(Stash overrides) {
        return new Stash(UnderBar.merge(this, overrides));
    }


    public boolean isEmpty() {
        return super.isEmpty();
    }

    public boolean isEmpty(String key) {
        return !hasContent(key);
    }

    public boolean isNull(String key) {
        return super.get(key) == null;
    }

    public Set<String> keys() {
        return super.keySet();
    }

    public Class type(String key) {
        return super.get(key).getClass();
    }

    public String join(String seperator, BiFunction<String, Object, String> bf){
        return UnderString.join(seperator, map(bf));
    }

    public String toUrlParameters() {
        return isEmpty()
                ? ""
                : "?" + join("&", (k, v) -> urlEncode(k) + "=" + urlEncode((v == null ? "" : v).toString()));
    }

    public <V, R> R ifLet(String key, Function<V, R> onK, Supplier<R> noK){
        return contains(key) ? onK.apply(get(key)) : noK.get();
    }

    public <V, R> Optional<R> ifLet(String key, Function<V, R> onK){
        return contains(key) ? UnderBar.optional(onK.apply(get(key))) : UnderBar.optional();
    }

    public <V, R> R ifLetPun(String key, Function<V, R> onK){
        return contains(key) ? onK.apply(get(key)) : null;
    }

    @Override
    public Stash clone() {
        return new Stash(this);
    }

    @Override
    public String toString() {
        return new TreeMap<>(this).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stash stringMap = (Stash) o;
        if (!this.keySet().equals(stringMap.keySet())) return false;
        for (String k : this.keySet()) {
            Object us =  super.get(k);
            Object them = stringMap.get(k);
            if (((us instanceof Integer && them instanceof Long) || (us instanceof Long && them instanceof Integer))) {
                // JSON has no notion of longs vs int
                if (((Number) us).longValue() != ((Number) them).longValue())
                    return false;
            } else if ((us instanceof Double) && (them instanceof Double)) {
                if (!almostEqual((Double) us, (Double) them))
                    return false;
            } else if (!java.util.Objects.equals(us, them))
                return false;
        }
        return true;
    }
}
