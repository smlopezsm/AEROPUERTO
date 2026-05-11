package mys;

import java.util.List;

@FunctionalInterface
public interface ServerSelectionPolicy {
    Server selectServer(List<Server> servers);
}
