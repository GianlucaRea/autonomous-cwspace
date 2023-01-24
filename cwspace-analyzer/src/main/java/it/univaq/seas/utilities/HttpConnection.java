package it.univaq.seas.utilities;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpResponse;

/**
 * @author gianlucarea
 */
public class HttpConnection {
    public static final String URLDOCKER = "http://cwspace-planner:8081/message"; //TODO change this
    public static final String URLLOCAL = "http://localhost:8081/message";

    public static void invoke(String message) throws ExecutionException, InterruptedException {
        String URI = (Utility.dockerized) ? URLDOCKER : URLLOCAL;
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();

        final SimpleHttpRequest request = SimpleRequestBuilder.post().setUri(URI).setBody(message, ContentType.APPLICATION_JSON).build();
        Future< SimpleHttpResponse> future = client.execute(request, new FutureCallback<SimpleHttpResponse>() {
            @Override
            public void completed(SimpleHttpResponse result) {
                String response = result.getBodyText();
            }

            @Override
            public void failed(Exception ex) {
                ex.printStackTrace();
            }

            @Override
            public void cancelled() {
                // EMPTY
            }
        });

        HttpResponse response = future.get();
        System.out.println("Response Code: " + response.getCode() + " , Response Verbose: " + response.getReasonPhrase());
    }

    public static void invoke(String message, String url) throws ExecutionException, InterruptedException {
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();

        final SimpleHttpRequest request = SimpleRequestBuilder.post().setUri(url).setBody(message, ContentType.APPLICATION_JSON).build();
        Future< SimpleHttpResponse> future = client.execute(request, new FutureCallback<SimpleHttpResponse>() {
            @Override
            public void completed(SimpleHttpResponse result) {
                String response = result.getBodyText();
            }

            @Override
            public void failed(Exception ex) {
                ex.printStackTrace();
            }

            @Override
            public void cancelled() {
                // EMPTY
            }
        });

        HttpResponse response = future.get();
        System.out.println("Response Code: " + response.getCode() + " , Response Verbose: " + response.getReasonPhrase());
    }
}
