package com.derbysoft.nuke.dlm.server.status;

import io.netty.channel.Channel;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by passyt on 16-9-19.
 */
public class StatsCenter {

    private static final StatsCenter INSTANCE = new StatsCenter();

    private final DefaultStats tcpTrafficStats = new DefaultStats();
    private final DefaultStats httpTrafficStats = new DefaultStats();
    private final Set<Channel> tcpChannels = new CopyOnWriteArraySet<>();
    private final Set<Channel> httpChannels = new CopyOnWriteArraySet<>();

    private StatsCenter() {
    }

    public static StatsCenter getInstance() {
        return INSTANCE;
    }

    public DefaultStats getTcpTrafficStats() {
        return tcpTrafficStats;
    }

    public DefaultStats getHttpTrafficStats() {
        return httpTrafficStats;
    }

    public Set<Channel> getHttpChannels() {
        return Collections.unmodifiableSet(httpChannels);
    }

    public Set<Channel> getTcpChannels() {
        return Collections.unmodifiableSet(tcpChannels);
    }

    public StatsCenter activeTcp(Channel channel){
        tcpChannels.add(channel);
        return this;
    }

    public StatsCenter inactiveTcp(Channel channel){
        tcpChannels.remove(channel);
        return this;
    }

    public StatsCenter activeHttp(Channel channel){
        httpChannels.add(channel);
        return this;
    }

    public StatsCenter inactiveHttp(Channel channel){
        httpChannels.remove(channel);
        return this;
    }
}
