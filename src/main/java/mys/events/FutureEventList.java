package mys.events;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FutureEventList{
    private List<Event> events; 
    private Comparator<Event> comparator;

    public FutureEventList() {
        this.events = new ArrayList<>();
        this.comparator = new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                if (e1.clock() < e2.clock()) {
                    return -1;
                } else if (e1.clock() > e2.clock()) {
                    return 1;
                } else if (e1.order() < e2.order()) {
                    return -1;
                } else if (e1.order() > e2.order()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
    }

    public void insert(Event e) {
        this.events.add(e);
        this.events.sort(this.comparator);
    }

    @Override
    public String toString() {
        return "FutureEventList{" +
                "events=" + events +
                ", comparator=" + comparator +
                '}';
    }

    public Event imminent() {
        return this.events.remove(0);
    }
}
