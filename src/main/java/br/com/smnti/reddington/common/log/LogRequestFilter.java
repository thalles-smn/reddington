package br.com.smnti.reddington.common.log;

import br.com.smnti.reddington.common.util.AppUtil;
import br.com.smnti.reddington.common.util.JsonUtil;
import net.logstash.logback.marker.Markers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LogRequestFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(LogRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        long start = System.currentTimeMillis();
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(requestWrapper, responseWrapper);
        this.logger(requestWrapper, responseWrapper, this.getResponseTimeSeconds(start));
    }

    private void logger(final ContentCachingRequestWrapper requestWrapper, final ContentCachingResponseWrapper responseWrapper, double responseTimeSeconds) throws IOException {
        String requestBody = this.getRequestBody(requestWrapper);
        String responseBody = this.getResponseBody(responseWrapper);
        String basePath = requestWrapper.getServletPath();
        String message = requestWrapper.getMethod() + " " + basePath;
        int status = responseWrapper.getStatus();
        responseWrapper.copyBodyToResponse();
        String requestHeaders = this.getRequestHeaders(requestWrapper);
        String responseHeaders = this.getResponseHeaders(responseWrapper);
        Map<String, Object> data = new HashMap();
        data.put("request_method", requestWrapper.getMethod());
        data.put("path", basePath);
        data.put("request_header", requestHeaders);
        data.put("request_body", requestBody);
        data.put("response_header", responseHeaders);
        data.put("response_body", responseBody);
        data.put("status_code", responseWrapper.getStatus());
        data.put("latency_seconds", String.valueOf(responseTimeSeconds));
        data.put("response_time", responseTimeSeconds);
        data.put("url", requestWrapper.getRequestURL().toString());
        data.put("application_origin", this.getApplicationOrigin(requestWrapper));

        if (status < 300) {
            log.info(Markers.appendEntries(data), message);
        } else if (status < 500) {
            log.warn(Markers.appendEntries(data), message);
        } else {
            log.error(Markers.appendEntries(data), message);
        }
    }

    private double getResponseTimeSeconds(long start) {
        long end = System.currentTimeMillis();
        return (double)(end - start) / 1000.0D;
    }

    private String getRequestHeaders(final ContentCachingRequestWrapper requestWrapper) {
        return Collections.list(requestWrapper.getHeaderNames()).stream()
                .map((t) -> t + "=" + requestWrapper.getHeader(t))
                .collect(Collectors.joining(";"));
    }

    private String getResponseHeaders(final ContentCachingResponseWrapper responseWrapper) {
        return responseWrapper.getHeaderNames().stream()
                .map((t) -> t + "=" + responseWrapper.getHeader(t))
                .collect(Collectors.joining(";"));
    }

    private String getRequestBody(final ContentCachingRequestWrapper requestWrapper) {
        return JsonUtil.removeNewlineTabFromString(new String(requestWrapper.getContentAsByteArray()));
    }

    private String getResponseBody(final ContentCachingResponseWrapper responseWrapper) {
        return JsonUtil.removeNewlineTabFromString(new String(responseWrapper.getContentAsByteArray()));
    }

    private String getApplicationOrigin( final ContentCachingRequestWrapper requestWrapper){
        return Optional.ofNullable(requestWrapper.getHeader("application_origin")).orElse(AppUtil.getServer());
    }
}
