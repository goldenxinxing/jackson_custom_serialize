API return value:
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