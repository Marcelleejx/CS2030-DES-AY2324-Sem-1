class RestEvent extends Event {
    private final Server server;

    public RestEvent(double time, Customer customer, Server server) {
        super(time, customer);
        this.server = server;
    }

    public Pair<Event, ImList<Server>> getNextEvent(ImList<Server> servers) {
        int serverIndex = this.server.getServerId() - 1;
        Server s = servers.get(serverIndex);
        Server updateServer = s.updateIdle(true);
        servers = servers.set(serverIndex, updateServer);
        return new Pair<Event, ImList<Server>>(this, servers);
    }

    @Override
    public String toString() {
        return "";
        //return String.format("%s %d rest finish\n", super.toString(), this.server.getServerId());
    }
}
