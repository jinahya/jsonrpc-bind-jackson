package com.github.jinahya.jsonrpc.bind.v2.calculator;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
public interface ExtendedCalculatorService extends CalculatorService {

}
