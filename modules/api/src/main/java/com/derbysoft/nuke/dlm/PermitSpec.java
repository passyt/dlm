package com.derbysoft.nuke.dlm;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by passyt on 16-9-3.
 */
public final class PermitSpec {

    private static final Splitter KEYS_SPLITTER = Splitter.on(',').trimResults();
    private static final Splitter KEY_VALUE_SPLITTER = Splitter.on('=').trimResults();

    public final String specification;
    private final Map<String, String> parameters;
    private boolean required = false;

    public PermitSpec(String specification) {
        this.specification = specification;
        this.parameters = toParameters(specification);
    }

    public PermitSpec required(boolean required) {
        this.required = required;
        return this;
    }

    protected static Map<String, String> toParameters(String specification) {
        Map<String, String> parameters = new HashMap<>();
        if (Strings.isNullOrEmpty(specification)) {
            return parameters;
        }

        for (Iterator<String> it = KEYS_SPLITTER.split(specification).iterator(); it.hasNext(); ) {
            String keyValuePair = it.next();
            ImmutableList keyAndValue = ImmutableList.copyOf(KEY_VALUE_SPLITTER.split(keyValuePair));
            Preconditions.checkArgument(!keyAndValue.isEmpty(), "blank key-value pair");
            Preconditions.checkArgument(keyAndValue.size() <= 2, "key-value pair %s with more than one equals sign", new Object[]{keyValuePair});
            String key = (String) keyAndValue.get(0);
            String value = keyAndValue.size() == 1 ? null : (String) keyAndValue.get(1);
            parameters.put(key, value);
        }

        return parameters;
    }

    public String stringValueOf(String key) {
        return valueOf(key, String.class);
    }

    public Integer intValueOf(String key) {
        return valueOf(key, Integer.class);
    }

    public Double doubleValueOf(String key) {
        return valueOf(key, Double.class);
    }

    public Float floatValueOf(String key) {
        return valueOf(key, Float.class);
    }

    public Long longValueOf(String key) {
        return valueOf(key, Long.class);
    }

    public BigDecimal bigDecimalValueOf(String key) {
        return valueOf(key, BigDecimal.class);
    }

    public <T extends Enum<T>> Enum<T> enumValueOf(String key, Class<T> clazz) {
        return valueOf(key, clazz);
    }

    protected <T> T valueOf(String key, Class<T> clazz) {
        String value = parameters.get(key);
        if (value == null) {
            if (required) {
                throw new IllegalArgumentException(key + " is required!");
            }
            return null;
        }

        if (String.class == clazz) {
            return (T) value;
        } else if (Integer.class == clazz) {
            return (T) Integer.valueOf(value);
        } else if (Double.class == clazz) {
            return (T) Double.valueOf(value);
        } else if (Long.class == clazz) {
            return (T) Long.valueOf(value);
        } else if (Float.class == clazz) {
            return (T) Float.valueOf(value);
        } else if (BigDecimal.class == clazz) {
            return (T) new BigDecimal(value);
        } else if (clazz.isEnum()) {
            Class enumType = clazz;
            return (T) Enum.valueOf(enumType, value);
        }

        throw new IllegalArgumentException("Unknown value " + value + " by type " + clazz);
    }

    public String getSpecification() {
        return specification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PermitSpec)) return false;
        PermitSpec that = (PermitSpec) o;
        return Objects.equal(specification, that.specification);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(specification);
    }

    @Override
    public String toString() {
        return specification;
    }
}
