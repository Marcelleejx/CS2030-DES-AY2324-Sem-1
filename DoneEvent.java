public class DoneEvent extends Event {
    private final Server server;

    public DoneEvent(double time, Customer customer, Server server) {
        super(time, customer);
        this.server = server;
    }
    
    /**
     * Generates the next event.
     *
     * @servers The list of servers.
     * @return A pair containing the same DoneEvent and updated list of servers.
     */

    public Pair<Event, ImList<Server>> getNextEvent(ImList<Server> servers) {
        int serverIndex = this.server.getServerId() - 1;
        Server s = servers.get(serverIndex);
        double restTime = s.getRestTime();
        Server updateServer = s.updateTime(this.getTime() + restTime).updateIdle(false);
        servers = servers.set(serverIndex, updateServer);
        return new Pair<Event, ImList<Server>>(new RestEvent(this.getTime() + restTime, 
                    getCustomer(), updateServer), servers);
    }

    @Override
    public String toString() {
        return String.format("%s done serving by %s\n", 
                super.toString(), this.server.toString());
    }
}
