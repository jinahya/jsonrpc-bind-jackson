package com.github.jinahya.jsonrpc.bind.v2;

/*-
 * #%L
 * jsonrpc-bind-jackson
 * %%
 * Copyright (C) 2019 Jinahya, Inc.
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;

/**
 * An abstract class for response objects.
 *
 * @param <ResultType> result type parameter.
 * @param <ErrorType>  error type parameter.
 * @param <IdType>     id type parameter.
 * @see <a href="https://github.com/FasterXML/jackson-databind">jackson-databind</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class JacksonResponse<ResultType, ErrorType extends ErrorObject<?>, IdType>
        extends ResponseObject<ResultType, ErrorType, IdType> {

}
