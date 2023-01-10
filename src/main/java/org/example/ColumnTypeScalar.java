/*
 * Copyright 2022 Starwhale, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.example;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Map;

@Getter
public class ColumnTypeScalar extends ColumnType {

    // if the column type is UNKNOWN, all values in the column are null. It is used for those columns whose type is
    // unknown. A UNKNOWN column will be changed to other types when a value other than null is written into the column.
    public static final ColumnTypeScalar UNKNOWN = new ColumnTypeScalar("unknown", 1);
    public static final ColumnTypeScalar BOOL = new ColumnTypeScalar("bool", 1);
    public static final ColumnTypeScalar INT8 = new ColumnTypeScalar("int", 8);
    public static final ColumnTypeScalar INT16 = new ColumnTypeScalar("int", 16);
    public static final ColumnTypeScalar INT32 = new ColumnTypeScalar("int", 32);
    public static final ColumnTypeScalar INT64 = new ColumnTypeScalar("int", 64);
    public static final ColumnTypeScalar FLOAT32 = new ColumnTypeScalar("float", 32);
    public static final ColumnTypeScalar FLOAT64 = new ColumnTypeScalar("float", 64);
    public static final ColumnTypeScalar STRING = new ColumnTypeScalar("string", 8);
    public static final ColumnTypeScalar BYTES = new ColumnTypeScalar("bytes", 8);

    private static final Map<String, ColumnType> typeMap = Map.of(
            UNKNOWN.getTypeName(), UNKNOWN,
            BOOL.getTypeName(), BOOL,
            INT8.getTypeName(), INT8,
            INT16.getTypeName(), INT16,
            INT32.getTypeName(), INT32,
            INT64.getTypeName(), INT64,
            FLOAT32.getTypeName(), FLOAT32,
            FLOAT64.getTypeName(), FLOAT64,
            STRING.getTypeName(), STRING,
            BYTES.getTypeName(), BYTES);

    private final String category;
    private final int nbits;

    private ColumnTypeScalar(String category, int nbits) {
        this.category = category;
        this.nbits = nbits;
    }

    public static ColumnType getColumnTypeByName(String typeName) {
        return typeMap.get(typeName.toUpperCase());
    }

    public String getTypeName() {
        if (this.category.equals(INT32.getCategory())
                || this.category.equals(FLOAT32.getCategory())) {
            return this.category.toUpperCase() + this.nbits;
        } else {
            return this.category.toUpperCase();
        }
    }

    @Override
    public String toString() {
        return this.getTypeName();
    }

    @Override
    public Object encode(Object value, boolean rawResult) {
        if (value == null) {
            return null;
        }
        if (rawResult) {
            if (this == BOOL
                    || this == INT8
                    || this == INT16
                    || this == INT32
                    || this == INT64
                    || this == FLOAT32
                    || this == FLOAT64
                    || this == STRING) {
                return value.toString();
            } else if (this == BYTES) {
                return StandardCharsets.UTF_8.decode((ByteBuffer) value).toString();
            }
        } else {
            if (this == BOOL) {
                return (Boolean) value ? "1" : "0";
            } else if (this == INT8) {
                return StringUtils.leftPad(Integer.toHexString(((Number) value).byteValue() & 0xFF), 2, "0");
            } else if (this == INT16) {
                return StringUtils.leftPad(Integer.toHexString(((Number) value).shortValue() & 0xFFFF), 4, "0");
            } else if (this == INT32) {
                return StringUtils.leftPad(Integer.toHexString(((Number) value).intValue()), 8, "0");
            } else if (this == INT64) {
                return StringUtils.leftPad(Long.toHexString(((Number) value).longValue()), 16, "0");
            } else if (this == FLOAT32) {
                return StringUtils.leftPad(Integer.toHexString(Float.floatToIntBits((Float) value)), 8, "0");
            } else if (this == FLOAT64) {
                return StringUtils.leftPad(Long.toHexString(Double.doubleToLongBits((Double) value)), 16, "0");
            } else if (this == STRING) {
                return value;
            } else if (this == BYTES) {
                var base64 = Base64.getEncoder().encode(((ByteBuffer) value).duplicate());
                return StandardCharsets.UTF_8.decode(base64).toString();
            }
        }
        throw new IllegalArgumentException("invalid type " + this);
    }

    @Override
    public Object decode(Object value) {
        if (value == null) {
            return null;
        }
        try {
            if (!(value instanceof String)) {
                throw new IllegalArgumentException("value should be of type String");
            }
            if (this == UNKNOWN) {
                throw new IllegalArgumentException("invalid unknown value " + value);
            } else if (this == BOOL) {
                if (value.equals("1")) {
                    return true;
                } else if (value.equals("0")) {
                    return false;
                }
                throw new IllegalArgumentException("invalid bool value " + value);
            } else if (this == INT8) {
                return (byte) (Integer.parseInt((String) value, 16) & 0xFF);
            } else if (this == INT16) {
                return (short) (Integer.parseInt((String) value, 16) & 0xFFFF);
            } else if (this == INT32) {
                return Integer.parseUnsignedInt((String) value, 16);
            } else if (this == INT64) {
                return Long.parseUnsignedLong((String) value, 16);
            } else if (this == FLOAT32) {
                return Float.intBitsToFloat(Integer.parseUnsignedInt((String) value, 16));
            } else if (this == FLOAT64) {
                return Double.longBitsToDouble(Long.parseUnsignedLong((String) value, 16));
            } else if (this == STRING) {
                return value;
            } else if (this == BYTES) {
                return ByteBuffer.wrap(Base64.getDecoder().decode((String) value));
            }
            throw new IllegalArgumentException("invalid type " + this);
        } catch (Exception e) {
            throw new RuntimeException(
                    MessageFormat.format("can not decode value {0} for type {1}", value, this),
                    e);
        }
    }

}
