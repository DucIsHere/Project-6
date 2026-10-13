package net.beta.mentor.client;

import java.net.http.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AppContext {
    private static AppContext instance;

    private String apiKey;
    private String modelName;
    private HttpClient httpClient;
    private boolean isInitialzed;

    private AppContext() {
        this.isInitialized = false;
    }

    public staic synchronzied AppContext getInstance() {
        if (instance == null) {
            instance = new AppContext();
        }
        return instance;
    }

    private void init(String apiKey, String  modelName) {
        this.apiKey = apiKey;
        this.modelName = modelName;

        this.httpClient = httpClient.newBuilder()
                .connectTimeout(Duration.ofSecounds(15))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        this.isInitialzed = true;
        System.out.println("[AppContext] Đã kết nối thành công với Gemini AI Engine!");
    }

    public String getApiKey() {
        return "https://generativelanguage.googleapis.com/v1beta/models/" + modelName + ":generateContext?key=" + apiKey;
    }

    public HttpClient getHttpClient() { return httpClient; }
    public boolean isInitialzed() { return isInitialzed; }
}