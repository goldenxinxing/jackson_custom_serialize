package org.example;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonSerialize(using = VirtualColumnSerializer.class)
public class RecordList {

    private Map<String, ColumnType> columnTypeMap;
    private List<Map<String, Object>> records;
    private String lastKey;
}