class WaitEvent extends Event {
    private final Server server;
    private final boolean print;

    public WaitEvent(double time, Customer customer, Server server, boolean print) {
        super(time, customer);
        this.server = server;
        this.print = print;
    }

    public Pair<Event, ImList<Server>> getNextEvent(ImList<Server> servers) {
        int serverIndex = this.server.getServerId() - 1;
        Server s = servers.get(serverIndex);
        ImList<Customer> queue = s.getWaitingCustomers();
        int queueSize = queue.size();
        if (!s.getIsIdle() && queueSize >= 1 && !s.isSelfCheck()) {
            return new Pair<Event, ImList<Server>>(new WaitEvent(s.getServerTime(), 
                        this.getCustomer(), s, false), servers);
        }

        if (s.isSelfCheck()) {
            for (int i = serverIndex; i < servers.size(); i++) {
                Server selfCheck = servers.get(i);
                if (selfCheck.getIsIdle()) {
                    Server updateServer = selfCheck.updateIdle(false);
                    servers = servers.set(i, updateServer);
                    return new Pair<Event, ImList<Server>>(new 
                                ServeEvent(selfCheck.getServerTime(), this.getCustomer(), 
                                            selfCheck), servers);
                }
            }

            if (serverIndex < servers.size() - 1) {
                double minTime = servers.get(serverIndex).getServerTime();
                for (int i = serverIndex + 1; i < servers.size(); i++) {
                    double anotherMinTime = servers.get(i).getServerTime();
                    if (anotherMinTime < minTime) {
                        minTime = anotherMinTime;
                    }
                }
                return new Pair<Event, ImList<Server>>(new WaitEvent(minTime, 
                this.getCustomer(), s, false), servers);
            }
        }
        
        Server updateServer = s.updateIdle(false);
        servers = servers.set(serverIndex, updateServer);
        return new Pair<Event, ImList<Server>>(new ServeEvent(s.getServerTime(), 
                    this.getCustomer(), s), servers);
    }

    @Override
    public String toString() {
        if (print) {
            return String.format("%s waits at %s\n", super.toString(), this.server.toString());
        }
        return "";
    }
}

