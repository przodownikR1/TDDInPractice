package pl.java.scalatech.errors_handle.handler;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
class ApiGlobalError {
    private final String code;

}