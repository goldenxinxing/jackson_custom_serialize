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

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = false)
public class ColumnTypeMap extends ColumnType {

    public static final String TYPE_NAME = "MAP";

    private final ColumnType keyType;

    private final ColumnType valueType;

    ColumnTypeMap(ColumnType keyType, ColumnType valueType) {
        this.keyType = keyType;
        this.valueType = valueType;
    }

    @Override
    public String toString() {
        return "{" + this.keyType + ":" + this.valueType + "}";
    }

    @Override
    public String getTypeName() {
        return ColumnTypeMap.TYPE_NAME;
    }

    @Override
    public Object encode(Object value, boolean rawResult) {
        if (value == null) {
            return null;
        }
        var ret = new HashMap<>();
        ((Map<?, ?>) value).forEach(
                (k, v) -> ret.put(this.keyType.encode(k, rawResult), this.valueType.encode(v, rawResult)));
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
        var ret = new HashMap<>();
        ((Map<?, ?>) value).forEach(
                (k, v) -> ret.put(this.keyType.decode(k), this.valueType.decode(v)));
        return ret;
    }
}
