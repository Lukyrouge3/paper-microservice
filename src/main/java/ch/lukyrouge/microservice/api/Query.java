package ch.lukyrouge.microservice.api;

import java.util.HashMap;
import java.util.Map;

public class Query {

    private final Map<String, String> _query;

    public Query() {
        this._query = new HashMap<>();
    }

    public void add(String key, String val)
    {
        _query.put(key, val);
    }

    public String params() {
        StringBuilder params = new StringBuilder();
        if (!_query.isEmpty()) {
            params.append("?");
            for (Map.Entry<String, String> entry : _query.entrySet()) {
                params.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            params.deleteCharAt(params.length() - 1); // Remove the trailing "&"
        }
        return params.toString();
    }
}
