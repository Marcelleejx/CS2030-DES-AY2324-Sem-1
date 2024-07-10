class ServeEvent extends Event {
    private final Server server;

    public ServeEvent(double time, Customer customer, Server server) {
        super(time, customer);
        this.server = server;
    }

    public Pair<Event, ImList<Server>> getNextEvent(ImList<Server> servers) {
        int serverIndex = this.server.getServerId() - 1;
        Server s = servers.get(serverIndex);
        int firstSelfCheck = 0;
        ImList<Customer> queue;
        if (this.server.isSelfCheck()) {
            for (int i = 0; i < servers.size(); i++) {
                if (servers.get(i).isSelfCheck()) {
                    firstSelfCheck = i;
                    break;
                }
            }
            queue = servers.get(firstSelfCheck).getWaitingCustomers();
        } else {
            queue = s.getWaitingCustomers();
        }
        
        if (queue.size() > 0) {
            queue = queue.remove(0);
        }

        double serviceTime = this.getCustomer().getServiceTime();
        if (!this.server.isSelfCheck() || serverIndex == firstSelfCheck) {
            Server updateServer = s.updateQueue(queue)
                    .updateTime(this.getTime() + serviceTime).updateIdle(false);
            servers = servers.set(serverIndex, updateServer);
            return new Pair<Event, ImList<Server>>(new DoneEvent(this.getTime() + serviceTime, 
                        getCustomer(), updateServer), servers);
        } else {
            Server firstSelfCheckServer = servers.get(firstSelfCheck);
            Server updateQueue = firstSelfCheckServer.updateQueue(queue);
            servers = servers.set(firstSelfCheck, updateQueue);
            Server updateServer = s
                    .updateTime(this.getTime() + serviceTime).updateIdle(false);
            servers = servers.set(serverIndex, updateServer);
            return new Pair<Event, ImList<Server>>(new DoneEvent(this.getTime() + serviceTime, 
                        getCustomer(), updateServer), servers);
        }
    }
    
    @Override
    public double waitingTime(ImList<Server> servers) {
        double arrival = super.getCustomer().getArrivalTime();
        double serve = this.getTime();
        double wait = serve - arrival;
        return wait;
    }

    @Override
    public int served() {
        return 1;
    }

    @Override
    public String toString() {
        return String.format("%s serves by %s\n", super.toString(), this.server.toString());
    }
}
