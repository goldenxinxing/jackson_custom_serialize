package org.example;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode(callSuper = false)
public class ColumnTypeObject extends ColumnType {
    private static final Pattern ATTRIBUTE_NAME_PATTERN = Pattern.compile("^[\\p{Alnum}_]+$");

    public static final String TYPE_NAME = "OBJECT";

    private final String pythonType;

    private final Map<String, ColumnType> attributes;

    ColumnTypeObject(@NonNull String pythonType, @NonNull Map<String, ColumnType> attributes) {
        this.pythonType = pythonType;
        this.attributes = attributes;
        attributes.keySet().forEach(key -> {
            if (!ATTRIBUTE_NAME_PATTERN.matcher(key).matches()) {
                throw new IllegalArgumentException(
                        "invalid attribute name " + key + ". only alphabets, digits, and underscore are allowed");
            }
        });
    }

    @Override
    public String toString() {
        var ret = new StringBuilder(this.getPythonType());
        ret.append('{');
        var len = ret.length();
        attributes.forEach((key, value) -> {
            if (ret.length() > len) {
                ret.append(',');
            }
            ret.append(key);
            ret.append(':');
            ret.append(value);
        });
        ret.append('}');
        return ret.toString();
    }

    @Override
    public String getTypeName() {
        return ColumnTypeObject.TYPE_NAME;
    }

    @Override
    public Object encode(Object value, boolean rawResult) {
        if (value == null) {
            return null;
        }
        var ret = new HashMap<String, Object>();
        //noinspection unchecked
        ((Map<String, ?>) value).forEach((k, v) -> {
            var type = this.attributes.get(k);
            if (type == null) {
                throw new IllegalArgumentException("invalid attribute " + k);
            }
            ret.put(k, type.encode(v, rawResult));
        });
        return ret;
    }

    @Override
    public Object decode(Object value) {
        if (value == null) {
            return null;
        }
        if (!(value instanceof Map)) {
            throw new RuntimeException("value should be of type Map, but is " + value.getClass());
        }
        var ret = new HashMap<String, Object>();
        //noinspection unchecked
        ((Map<String, ?>) value).forEach((k, v) -> {
            var type = this.attributes.get(k);
            if (type == null) {
                throw new RuntimeException("invalid attribute " + k);
            }
            ret.put(k, type.decode(v));
        });
        return ret;
    }
}
