abstract class Event {
    private final double time;
    private final Customer customer;

    Event(double time, Customer customer) {
        this.time = time;
        this.customer = customer;
    }

    protected double getTime() {
        return this.time;
    }

    protected Customer getCustomer() {
        return this.customer;
    }

    protected int getEventCustId() {
        return this.customer.getCustomerId();
    }

    public int served() {
        return 0;
    }

    public double waitingTime(ImList<Server> servers) {
        return 0;
    }

    public Pair<Event, ImList<Server>> getNextEvent(ImList<Server> servers) {
        return new Pair<Event, ImList<Server>>(this, servers);
    }

    @Override
    public String toString() {
        return String.format("%.3f %d", this.time, this.getEventCustId());
    }
}

