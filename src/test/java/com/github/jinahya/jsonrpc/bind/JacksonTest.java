package com.github.jinahya.jsonrpc.bind;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static com.github.jinahya.jsonrpc.bind.JacksonUtils.OBJECT_MAPPER;

@Slf4j
class JacksonTest {

    @Disabled
    @Test
    void mapException() throws IOException {
        try {
            BigDecimal.ONE.divide(BigDecimal.ZERO);
        } catch (final ArithmeticException ae) {
            ae.printStackTrace();
            log.debug("arithmeticException: {}",
                      OBJECT_MAPPER.writer().withDefaultPrettyPrinter().writeValueAsString(ae));
        }
    }
}
