package routes.groomrate;

import bostonhttp.api.APIResponse;
import routes.Route;
import routes.RouteAPI;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

class GroomRateAPI extends RouteAPI {

    public GroomRateAPI(Route route) {
        super(route);
    }

    public CompletableFuture<Void> getGroomRate(String startDate, String endDate, Consumer<APIResponse<Object>> callback) {
        return queryAsync("?start_date=" + startDate + "&end_date=" + endDate, Object.class).thenAccept(callback);
    }
}
