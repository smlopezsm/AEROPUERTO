package mys;

import java.util.List;

public class OneServer implements ServerSelectionPolicy {

    @Override
    public Server selectServer(List<Server> servers) {
        return servers.get(0);
    }
}
