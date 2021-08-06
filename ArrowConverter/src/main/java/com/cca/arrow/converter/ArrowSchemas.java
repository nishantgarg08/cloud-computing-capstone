package com.cca.arrow.converter;

import java.util.List;

import org.apache.arrow.vector.types.FloatingPointPrecision;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;
import org.apache.arrow.vector.types.pojo.Schema;

import static java.util.Arrays.asList;

public class ArrowSchemas {
    
    static Schema personSchema() {
        return new Schema(personFields());
    }
    

    private static List<Field> personFields() {
        return asList(
                new Field("firstName", FieldType.nullable(new ArrowType.Utf8()), null),
                new Field("lastName", FieldType.nullable(new ArrowType.Utf8()), null),
                new Field("age", FieldType.nullable(new ArrowType.Int(32, false)), null),
                new Field("salary", FieldType.nullable(new ArrowType.Int(64, false)), null),
                new Field("gender", FieldType.nullable(new ArrowType.Utf8()), null),
                new Field("time", FieldType.nullable(new ArrowType.FloatingPoint(
                    FloatingPointPrecision.SINGLE )), null)
        );
    }
}