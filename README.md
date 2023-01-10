- custom JsonSerializer
```java
public class VirtualColumnSerializer extends StdSerializer<RecordList> {

    public VirtualColumnSerializer() {
        this(null);
    }

    public VirtualColumnSerializer(Class<RecordList> t) {
        super(t);
    }

    @Override
    public void serialize(RecordList value,
                          JsonGenerator jgen,
                          SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        var virtualCols = value.getColumnTypeMap().entrySet().stream()
                .filter(entry -> entry.getValue().getTypeName().equals(ColumnTypeVirtual.TYPE_NAME))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> (ColumnTypeVirtual) entry.getValue()));
        if (!virtualCols.isEmpty()) {
            var records = value.getRecords();

            // 此处的record是一个nested的map，即object是nested的，对于list和map key为非str的解析较复杂

            records.forEach(record -> virtualCols.forEach((colAlias, colType) -> {
                var path = colType.getPath();
                var newProperty = this.search(path, record);
                // update record
                record.put(colAlias, newProperty);
            }));
        }
        jgen.writeObjectField("cols", value.getColumnTypeMap());
        jgen.writeObjectField("records", value.getRecords());
        jgen.writeObjectField("lastKey", value.getLastKey());
        jgen.writeEndObject();
    }

    private Object search(String path, Object value) {
        var index = path.indexOf(".");
        if (index == -1) {
            // last
            if (value instanceof Map) {
                return ((Map<?, ?>) value).get(path);
            } else {
                // TODO support list or map<obj, ?>
                throw new RuntimeException("not support col");
            }
        } else {
            var subPath = path.substring(index + 1);
            var property = path.substring(0, index);
            if (value instanceof Map) {
                var subValue = ((Map<?, ?>) value).get(property);
                return this.search(subPath, subValue);
            } else {
                // TODO support list or map<obj, ?>
                throw new RuntimeException("not support col");
            }
        }
    }
}
```
- add decorator for Response Entity 
```java
@JsonSerialize(using = VirtualColumnSerializer.class)
public class RecordList {

    private Map<String, ColumnType> columnTypeMap;
    private List<Map<String, Object>> records;
    private String lastKey;
}
```
- API return value:
```json
{
    "cols": {
        "x": {
            "category": "string",
            "nbits": 8,
            "typeName": "STRING"
        },
        "a": {
            "pythonType": "dict",
            "attributes": {
                "b": {
                    "category": "string",
                    "nbits": 8,
                    "typeName": "STRING"
                }
            },
            "typeName": "OBJECT"
        },
        "v": {
            "path": "a.b",
            "type": {
                "category": "string",
                "nbits": 8,
                "typeName": "STRING"
            }
        }
    },
    "records": [
        {
            "x": "hello",
            "a": {
                "b": "hi-1,sw!"
            },
            "v": "hi-1,sw!"
        },
        {
            "x": "hello",
            "a": {
                "b": "hi-2,sw!"
            },
            "v": "hi-2,sw!"
        },
        {
            "x": "hello",
            "a": {
                "b": "hi-3,sw!"
            },
            "v": "hi-3,sw!"
        },
        {
            "x": "hello",
            "a": {
                "b": "hi-4,sw!"
            },
            "v": "hi-4,sw!"
        },
        {
            "x": "hello",
            "a": {
                "b": "hi-5,sw!"
            },
            "v": "hi-5,sw!"
        }
    ],
    "lastKey": null
}
```
