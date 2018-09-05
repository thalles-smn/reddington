package br.com.smnti.reddington.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.marker.Markers;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public abstract class RetrofitBase<T> {

    private List<Converter.Factory> converters;

    protected abstract String getServiceName();

    protected RetrofitBase(){
        this.converters = new ArrayList<>();
        this.converters.add(gsonConverterFactory());
    }

    private GsonConverterFactory gsonConverterFactory(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .serializeNulls()
                .enableComplexMapKeySerialization()
                .create();
        return GsonConverterFactory.create(gson);
    }

    @SuppressWarnings("unchecked")
    protected T getApi( String baseUrl ){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl);

        this.converters.forEach(builder::addConverterFactory);

        Retrofit retrofit = builder.build();
        return retrofit.create((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0] );

    }

    protected <RESPONSE> Response<RESPONSE> execute(Call<RESPONSE> call, String operationName) throws IOException {

        Response<RESPONSE> res = null;

        try {
            res = call.execute();
            return res;
        }
        finally {
            this.loggerRequest(call, res, operationName);
        }
    }

    /**
     * Loga informações de requisição
     *
     * @param request Request a ser logado
     * @param response Response a ser logado
     */
    private void loggerRequest( Call<?> request, Response<?> response, String operationName){

            Map<String, Object> data = new HashMap<>();
            data.put("request_method", request.request().method());
            data.put("path", request.request().url().uri().getPath());
            data.put("request_header", formatHeaders(request.request().headers()));
            data.put("request_body", formatBody(request.request().body()));
            data.put("url", request.request().url().url().toString());
            data.put("operation_name", operationName);

            Integer statusCode = 500;

            if(response != null && response.raw() != null) {
                data.put("response_body", response.body() != null ? formatBody(response.body()) : formatBody(response.errorBody()));
                data.put("response_header", formatHeaders(response.raw().headers()));
                statusCode = response.raw().code();
                data.put("status_code", statusCode);
                data.put("latency_seconds", String.valueOf(response.raw().receivedResponseAtMillis() / 1000L));

            }

        String message = new StringBuilder().append("Call: ").append(this.getServiceName()).toString();

        if (statusCode < 300) {
            log.info(Markers.appendEntries(data), message);
        } else if (statusCode < 500) {
            log.warn(Markers.appendEntries(data), message);
        } else {
            log.error(Markers.appendEntries(data), message);
        }
    }

    /**
     * Formata o objeto Headers para ser logado
     *
     * @param headers objeto Headers
     * @return string com objeto formatado
     */
    private  static String formatHeaders(Headers headers){
        StringBuilder format = new StringBuilder();

        headers.names().forEach(key -> format.append(headers.get(key)).append(","));

        return format.toString();
    }

    /**
     * Formata objeto para string a ler logada
     *
     * @param body objeto a ser formatado
     * @return string com objeto no formato json
     */
    private static String formatBody( Object body ){

        if(body != null) {

            try {
                if(body instanceof ResponseBody){
                    return ((ResponseBody) body).string();
                }

                return new ObjectMapper().writeValueAsString(body);

            } catch (Exception ex) {
                return "";
            }
        }

        return "";

    }

}
