package com.mtt.lib.base_module.http.interceptor;/*
 * Copyright (C) 2015 Square, Inc.
 *
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
 */

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;


public final class BaseLoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");


    public interface Logger {
        void log(String message);

    }


    public BaseLoggingInterceptor(Logger logger) {
        this.logger = logger;
    }

    private final Logger logger;


    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        logger.log(" ------------------------------ START -------------------------------------");
        if (hasRequestBody) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            if (isPlaintext(buffer)) {
                logger.log(" POST " + request.url() + "?" + buffer.readString(charset) + " ");

            }
        } else {
            logger.log(" GET " + request.url());
        }

        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            logger.log(" 拦截器错误 " + e);
            throw e;
        }
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();

        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8);
            } catch (UnsupportedCharsetException e) {
                logger.log("Couldn't decode the response body; charset is likely malformed.");

                return response;
            }
        }

        if (!isPlaintext(buffer)) {
            logger.log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
            return response;
        }

        if (contentLength != 0) {
            logger.log(buffer.clone().readString(charset));
        }

        logger.log(" ------------------------------- END --------------------------------------");
        return response;

    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
