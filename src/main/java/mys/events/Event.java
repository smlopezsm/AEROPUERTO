package mys.events;
import mys.entities.Entity;
import mys.generators.Distribution;
import java.util.List;
import mys.resources.Server;

public interface Event {

	double clock();

	int order();

	Entity entity();

	Distribution distribution();

	void planificate(FutureEventList fel, List<Server> servers, Statistics statistics);

}
