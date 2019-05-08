package tech.washmore.autocodeplus.common.db;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ColumnName {
    String value();
}
