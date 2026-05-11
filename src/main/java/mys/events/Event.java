package mys;

import java.util.List;

import mys.generators.Distribution;

public interface Event {

	double clock();

	int order();

	Entity entity();

	Distribution distribution();

	void planificate(FutureEventList fel, List<Server> servers, Statistics statistics);

}
