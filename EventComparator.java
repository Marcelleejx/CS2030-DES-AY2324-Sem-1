import java.util.Comparator;

class EventComparator implements Comparator<Event> {
    public int compare(Event e1, Event e2) {
        double e1Time = e1.getTime();
        double e2Time = e2.getTime();
        int e1Id = e1.getCustomer().getCustomerId();
        int e2Id = e2.getCustomer().getCustomerId();

        if (e1Time != e2Time) {
            if (e1Time - e2Time < 0) {
                return -1;
            } else {
                return 1;
            }
        } else {
            return e1Id - e2Id;
        }
    }
}
