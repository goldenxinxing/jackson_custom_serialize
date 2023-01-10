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

import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode(callSuper = false)
public class ColumnTypeList extends ColumnType {

    public static final String TYPE_NAME = "LIST";

    protected final ColumnType elementType;

    ColumnTypeList(ColumnType elementType) {
        this.elementType = elementType;
    }

    @Override
    public String toString() {
        return "[" + elementType + "]";
    }

    @Override
    public String getTypeName() {
        return ColumnTypeList.TYPE_NAME;
    }

    @Override
    public Object encode(Object value, boolean rawResult) {
        if (value == null) {
            return null;
        }
        return ((List<?>) value).stream()
                .map(element -> this.elementType.encode(element, rawResult))
                .collect(Collectors.toList());
    }

    @Override
    public Object decode(Object value) {
        if (value == null) {
            return null;
        }
        if (!(value instanceof List)) {
            throw new RuntimeException("value should be of type List, but is " + value.getClass());
        }
        return ((List<?>) value).stream()
                .map(this.elementType::decode)
                .collect(Collectors.toList());
    }

}
