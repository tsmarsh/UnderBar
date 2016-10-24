package com.tailoredshapes.util;

import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.*;

import static com.tailoredshapes.util.Die.*;
import static com.tailoredshapes.util.Dates.isoString;
import static com.tailoredshapes.util.UnderBar.*;
import static com.tailoredshapes.util.Strings.urlEncode;
import static java.util.Optional.ofNullable;
import static org.joda.time.format.ISODateTimeFormat.dateTimeParser;

public class StringMap implements JSONAware {
    private final Map<String, Object> m = new HashMap<>();

    public StringMap(Map<String, ?> m) {
        forEach(m, this::put);
    }

    public StringMap() { }

    public StringMap(StringMap m) {
        this(m.m);
    }

    public static List<StringMap> parseJsonMaps(String path) {
        return parseJsonMaps(Paths.get(path));
    }

    public static List<StringMap> parseJsonMaps(Path path) {
        String jsonString = rethrow(() -> new String(Files.readAllBytes(path)));
        JSONArray items = (JSONArray) rethrow(() -> JSONValue.parseWithException(jsonString));
        return UnderBar.map(items, i -> new StringMap(((HashMap<String, Object>) i)));
    }

    public StringMap put(String k, Object v) {
        if(v == null || v.equals(""))
            m.put(k, null);
        else
            m.put(k, v);
        return this;
    }

    public long size() {
        return m.size();
    }

    public StringMap with(String k, Object v) {
        return new StringMap(this).put(k, v);
    }

    public long long_(String k) {
        return getCast(k, t -> (long) t);
    }

    public double double_(String k) {
        return ((Number) getCast(k, x -> (Number) x)).doubleValue();
    }

    public int integer(String k) {
        return getCast(k, x -> (int) x);
    }
    public int integer(String k, int default_) {
        Integer result = (Integer) m.getOrDefault(k, default_);
        return result == null ? default_ : result;
    }

    public int integerValue(String k) {
        Number value = getCast(k, x -> (Number) x);
        return tap(value.intValue(), result ->
                dieUnless(result == value.longValue(), () -> "overflow detected!  cannot convert " + value + " to integer"));
    }
    public int parseInteger(String k, int default_) {
        return ofNullable((String) m.get(k)).map(Integer::parseInt).orElse(default_);
    }

    public long parseLong(String key) {
        return Long.parseLong(string(key));
    }

    public Optional<Integer> integerMaybe(String k) {
        return getCast(k, x -> (Optional<Integer>) x);
    }

    public Optional<DateTime> dateMaybe(String k) {
        return getCast(k, x -> (Optional<DateTime>) x);
    }

    public Optional<Double> doubleMaybe(String k) {
        return getCast(k, x -> (Optional<Double>) x);
    }

    public Optional<Long> longMaybe(String key) {
        return getCast(key, x -> (Optional<Long>) x);
    }

    public Optional<Boolean> boolMaybe(String key) {
        return getCast(key, x -> (Optional<Boolean>) x);
    }

    public long longValue(String k) {
        return ((Number) getCast(k, x -> (Number) x)).longValue();
    }

    public String string(String k) {
        return getCast(k, x -> (String) x);
    }

    public Optional<String> stringMaybe(String k) {
        return getCast(k, x -> (Optional<String>) x);
    }


    public DateTime date(String k) {
        return getCast(k, x -> (DateTime) x);
    }

    public <T> List<T> map(BiFunction<String, Object, T> toT) {
        return UnderBar.map(m, toT);
    }

    public Map<String, Object> putInto(Map<String, Object> other) {
        other.putAll(m);
        return other;
    }

    public StringMap putInto(StringMap other) {
        other.m.putAll(m);
        return other;
    }

    public boolean has(String key) {
        return m.containsKey(key);
    }
    public boolean hasValue(String key) {
        return has(key) && m.get(key) != null && (m.get(key) instanceof Optional ? ((Optional) m.get(key)).isPresent() : true );
    }

    public boolean bool(String k) {
        return getCast(k, b -> (boolean) b);
    }

    public boolean bool(String key, boolean default_) {
        return (Boolean) m.getOrDefault(key, default_);
    }

    private <T> T getCast(String k, Function<T, T> onT) {
        Object value = dieIfMissing(m, k);
        return rethrow(() -> onT.apply((T) value), () -> "failed to cast to T for key: " + k + ": value: " + value + " of type: " + value.getClass());
    }

    public StringMap smap(String k) { return getCast(k, smap -> (StringMap) smap); }
    public StringMap makeSmap(String key) { return new StringMap((Map<String, Object>)getCast(key, x -> (Map<String, Object>) x)); }

    public List<StringMap> smaps(String key) { return getCast(key, x -> (List<StringMap>) x); }
    public List<StringMap> makeSmaps(String key) { return UnderBar.map((Collection<Map<String, Object>>) getCast(key, x -> (Collection<Map<String, Object>>) x), StringMap::new); }

    public StringMap parseJson(String key) {
        return parseJSON(string(key));
    }

    @Override
    public String toJSONString() {
        return JSONValue.toJSONString(modifyValues(m, this::toJSONStringValue));
    }

    private Object toJSONStringValue(Object v) {
        if (v instanceof Optional<?>) return toJSONStringValue(((Optional<?>) v).orElse(null));
        if (v instanceof DateTime) return isoString((DateTime) v);
        if (v instanceof UUID) return v.toString();
        if (v instanceof Map) return new StringMap((Map<String, Object>) v).toJSONString();
        return v;
    }


    @Override
    public String toString() {
        return new TreeMap<>(m).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringMap stringMap = (StringMap) o;
        if(!m.keySet().equals(stringMap.m.keySet())) return false;
        for(String k : m.keySet()) {
            Object lvalue = m.get(k);
            Object rvalue = stringMap.m.get(k);
            if (((lvalue instanceof Integer && rvalue instanceof Long) || (lvalue instanceof Long && rvalue instanceof Integer))) {
                // they are all longs to JSON, so we don't want to get bogged down in chasing down int v. long issues
                if (((Number) lvalue).longValue() != ((Number) rvalue).longValue())
                    return false;
            } else if ((lvalue instanceof Double) && (rvalue instanceof Double)) {
                if (!almostEqual((Double) lvalue, (Double) rvalue))
                    return false;
            } else if (!java.util.Objects.equals(lvalue, rvalue))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(m);
    }

    public StringMap filterKeys(Predicate<String> predicate) {
        return new StringMap(UnderBar.filterKeys(m, predicate));
    }

    public StringMap rejectKeys(String ... keys) {
        return rejectKeys(list(keys));
    }

    public StringMap selectKeys(String ... keys) {
        Set<String> keySet = set(keys);
        return filterKeys(keySet::contains);
    }

    public StringMap selectKeys(Iterable<String> keys) {
        Set<String> keySet = set(keys);
        return filterKeys(keySet::contains);
    }

    public StringMap rejectKeys(List<String> keys) {
        Set<String> keySet = set(keys);
        return filterKeys(k -> !keySet.contains(k));
    }

    public Map<String, Object> toMap() {
        return new HashMap<>(m);
    }

    public Map<String, Object> toMapDeeply() {
        Map<String, Object> result = toMap();

        result.forEach((key, value) -> {
            if (value instanceof StringMap) {
                result.put(key, ((StringMap) value).toMapDeeply());
            } else if (value instanceof Iterable<?>) {
                Iterable<?> v = (Iterable<?>) value;
                List<Object> convertedValue = UnderBar.map(v, (element) -> {
                    if (element instanceof StringMap) {
                        return ((StringMap) element).toMapDeeply();
                    }
                    return element;
                });
                result.put(key, convertedValue);
            }
        });
        return result;
    }


    public <T> T remove(String key) {
        return (T) m.remove(key);
    }

    public DateTime parseDate(String key) {
        return Dates.date(string(key));
    }

    public boolean hasContent(String key) {
        return has(key) && Strings.hasContent((String) m.get(key));
    }

    public Optional<String> stringOptional(String key) {
        String value = (String) dieIfMissingKey(m, key);
        return Strings.optional(value);
    }

    public Optional<String> stringish(String key) {
        if (!has(key) || isNull(key)) return optional();
        if (type(key) == Optional.class) return stringMaybe(key);
        if (type(key) != String.class) return optional(m.get(key).toString());
        return Strings.optional(string(key));
    }

    public Optional<Long> longish(String key) {
        if (!has(key) || isNull(key)) return optional();
        if (type(key) == Optional.class) return longMaybe(key);
        return optional(long_(key));
    }

    public Optional<DateTime> dateish(String key) {
        if (!has(key) || isNull(key)) return optional();
        if (type(key) == Optional.class) return dateMaybe(key);
        return optional(date(key));
    }

    public Optional<Double> doublish(String key) { return (has(key) && !isNull(key)) ? optional(double_(key)) : optional();}

    public Optional<Boolean> boolish(String key) {  return (has(key) && !isNull(key)) ? optional(bool(key)) : optional();}

    public Optional<Boolean> booleanOptional(String key) {
        return ofNullable((Boolean) dieIfMissingKey(m, key));
    }

    public StringMap merge(StringMap overrides) {
        return new StringMap(UnderBar.merge(m, overrides.m));
    }

    public static StringMap parseJSON(String json) {
        return new StringMap((Map<String, Object>) dieIfNull(JSONValue.parse(json), ()-> "error parsing JSON: " + json));
    }


    public String asString(String key) {
        return dieIfMissing(m, key).toString();
    }

    public UUID uuid(String key) {
        return getCast(key, x -> (UUID) x);
    }

    public UUID parseUUID(String key) {
        return UUID.fromString(string(key));
    }

    public Optional<UUID> parseUUIDMaybe(String key) {
        return ofNullable(hasValue(key) ? parseUUID(key) : null);
    }

    public Number number(String key) {
        return getCast(key, x -> (Number) x);
    }

    public String asUrl() {
        return isEmpty()
                ? ""
                : "?" + Strings.join("&", m, (k, v) -> urlEncode(k) + "=" + urlEncode((v == null ? "" : v).toString()));
    }

    public boolean isEmpty() {
        return m.isEmpty();
    }

    public boolean isEmpty(String key) {
        return !hasContent(key);
    }

    public double parseDouble(String key, double default_) {
        return ofNullable((String) m.get(key)).map(Double::parseDouble).orElse(default_);
    }

    public boolean parseBoolean(String key, boolean default_) {
        return ofNullable((String) m.get(key)).map(Boolean::parseBoolean).orElse(default_);
    }

    public boolean isNull(String key) {
        return m.get(key) == null;
    }

    public JSONObject jsonObject(String key) {
        return getCast(key, x -> (JSONObject) x);
    }

    public <T> List<T> getList(String key) {
        return getCast(key, x -> (List<T>) x);
    }

    public StringMap shallowCopy() {
        return new StringMap(m);
    }

    public Optional<DateTime> parseDateMaybe(String key) {
        return stringish(key).map(Dates::date);
    }

    public Set<String> keys() {
        return m.keySet();
    }

    public Class type(String key) {
        return m.get(key).getClass();
    }

    public Optional<UUID> uuidMaybe(String key) {
        return (Optional<UUID>) dieIfMissingKey(m, key);
    }

    public UUID uuidOrNull(String key){
        return uuidMaybe(key).orElse(null);
    }

    public DateTime parseIsoDateTime(String key) {
        return DateTime.parse(string(key), dateTimeParser());
    }

    public Path path(String key) {
        return Paths.get(string(key));
    }

    public static StringMap parseJSON(Path path) {
        return parseJSON(rethrow(() -> new String(Files.readAllBytes(path))));
    }

    public long doubleToLong(String k) {
        return Double.valueOf(double_(k)).longValue();
    }

    public Object object(String k) {
        return getCast(k, o -> o);
    }
}
