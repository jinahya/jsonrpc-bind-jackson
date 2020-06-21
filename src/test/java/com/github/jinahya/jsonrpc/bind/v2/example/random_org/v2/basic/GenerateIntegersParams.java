package com.github.jinahya.jsonrpc.bind.v2.example.random_org.v2.basic;

/*-
 * #%L
 * jsonrpc-bind-jackson
 * %%
 * Copyright (C) 2019 - 2020 Jinahya, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@ToString
@Setter
@Getter
@Slf4j
public class GenerateIntegersParams {

    public static final int MIN_N = 1;

    public static final int MAX_N = 10000;

    {
        assert MAX_N == Double.valueOf(1.e+4).intValue() : "MAX_N";
    }

    public static final int MIN_MIN = -1000000000;

    public static final int MAX_MIN = +1000000000;

    {
        assert MIN_MIN == Double.valueOf(-1.0e+9).intValue() : "MIN_MIN";
        assert MAX_MIN == Double.valueOf(+1.0e+9).intValue() : "MAX_MIN";
    }

    public static final int MIN_MAX = MIN_MIN;

    public static final int MAX_MAX = MAX_MIN;

    @NotBlank
    private String apiKey;

    @Max(MAX_N)
    @Min(MIN_N)
    private int n;

    @Max(MAX_MIN)
    @Min(MIN_MIN)
    private int min;

    @Max(MAX_MAX)
    @Min(MIN_MAX)
    private int max;

    private Boolean replacement;

    private Integer base;
}
