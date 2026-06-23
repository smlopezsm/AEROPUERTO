package mys.policies;

import java.util.List;
import mys.resources.*;

public class OneServer implements ServerSelectionPolicy {

    @Override
    public Server selectServer(List<Server> servers) {
        return servers.get(0);
    }

    //hacer la politica para varios servidores
    
}
