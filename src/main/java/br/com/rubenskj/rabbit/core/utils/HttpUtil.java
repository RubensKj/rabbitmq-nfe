package br.com.rubenskj.rabbit.core.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * The {@link HttpUtil} class create default rest template
 * <p>
 * This provides the default rest template to create the integration
 * with oAuth2.
 *
 * @author Rubens K. Júnior
 * @since 0.1
 */
public class HttpUtil {
    private HttpUtil() {
    }

    private static RestTemplate createRestTemplate() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(20);
        connectionManager.setDefaultMaxPerRoute(20);
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connectionManager).setDefaultRequestConfig(config).build();
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplateAdd = new RestTemplate(httpRequestFactory);
        restTemplateAdd.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplateAdd;
    }

    public static RestTemplate getRestTemplate() {
        return createRestTemplate();
    }


    public static UriComponents createUriComponents(String uri) {
        return UriComponentsBuilder.fromUriString("http://localhost:8080".concat(uri))
                .build();
    }
}
