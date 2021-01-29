package com.fooqoo56.iine.bot.publisher.infrastructure.api.interceptor;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RestRequestInterceptor implements ClientHttpRequestInterceptor {

    /**
     * interceptor.
     *
     * @param request   リクエスト
     * @param body      ボディ
     * @param execution ClientHttpRequestExecution
     * @return ClientHttpResponse
     * @throws IOException IOException
     */
    @NonNull
    @Override
    public ClientHttpResponse intercept(
            final HttpRequest request, @NonNull final byte[] body,
            final ClientHttpRequestExecution execution) throws IOException {

        log.info(
                "api request - {}: {}", request.getMethodValue(),
                request.getURI());
        log.info(
                "api request header: {}", request.getHeaders().toString());
        final ClientHttpResponse response =
                execution.execute(request, body);
        log.info(
                "api response - {}: {}",
                response.getRawStatusCode(),
                response.getStatusText());
        log.info(
                "api response header: {}", response.getHeaders().toString());

        return response;
    }
}
