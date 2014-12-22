/*
 * Copyright 2014, Simon Matić Langford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.betfair.cougar.transport.impl.protocol.http;

import com.betfair.cougar.core.api.builder.DehydratedExecutionContextBuilder;
import com.betfair.cougar.transport.api.DehydratedExecutionContextComponent;
import com.betfair.cougar.transport.api.SingleComponentResolver;
import com.betfair.cougar.transport.api.protocol.http.HttpCommand;

import javax.servlet.http.HttpServletRequest;

/**
 * Default HTTP trace logging resolver. Checks for existence of the 'X-Trace-Me' HTTP header.
 */
public class HttpTraceLoggingResolver<Ignore> extends SingleComponentResolver<HttpCommand, Ignore> {

    public static final String TRACE_ME_HEADER_PARAM = "X-Trace-Me";

    public HttpTraceLoggingResolver() {
        super(DehydratedExecutionContextComponent.TraceLoggingEnabled);
    }

    @Override
    public void resolve(HttpCommand httpCommand, Ignore ignore, DehydratedExecutionContextBuilder builder) {
        builder.setTraceLoggingEnabled(httpCommand.getRequest().getHeader(TRACE_ME_HEADER_PARAM)!=null);
    }
}