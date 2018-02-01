package pl.java.scalatech.errors_handle.handler;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode
@ToString
class ApiFieldError {
    private String field;
    private String code;
    private Object rejectedValue;

}