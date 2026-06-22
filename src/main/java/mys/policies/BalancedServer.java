package mys.policies;

import java.util.List;

import mys.resources.Server;
import mys.resources.ServerSelectionPolicy;

public class BalancedServer implements ServerSelectionPolicy {

    @Override
    public Server selectServer(List<Server> servers) {
        if (servers == null || servers.isEmpty()) {
            throw new IllegalArgumentException("La lista de servidores no puede estar vacia");
        }

        Server selected = null;
        for (Server server : servers) {
            if (!server.isBusy()
                    && (selected == null
                    || server.getDurability() > selected.getDurability())) {
                selected = server;
            }
        }

        if (selected != null) {
            return selected;
        }

        selected = servers.get(0);
        for (int i = 1; i < servers.size(); i++) {
            Server candidate = servers.get(i);
            int queueComparison = Integer.compare(
                    candidate.queue().size(),
                    selected.queue().size());

            if (queueComparison < 0
                    || (queueComparison == 0
                    && candidate.getDurability() > selected.getDurability())) {
                selected = candidate;
            }
        }

        return selected;
    }
}
