package com.derbysoft.nuke.dlm.server;

import com.derbysoft.nuke.dlm.server.initializer.PermitServerInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by passyt on 16-9-4.
 */
@Configuration
public class PermitServerConfiguration {

    @Value("${server.tcp.port}")
    private int tcpPort;
    @Autowired
    @Qualifier("permitServerTcpInitializer")
    private PermitServerInitializer tcpInitializer;

    @Value("${server.http.port}")
    private int httpPort;
    @Autowired
    @Qualifier("permitServerHttpInitializer")
    private PermitServerInitializer httpInitializer;

    @Bean(name = "bossGroup")
    public EventLoopGroup bossGroup() {
        return new NioEventLoopGroup();
    }

    @Bean(name = "workerGroup")
    public EventLoopGroup workerGroup() {
        return new NioEventLoopGroup();
    }

    @Bean(name = "permServerInitializers")
    public Map<Integer, PermitServerInitializer> initializers() {
        Map<Integer, PermitServerInitializer> initializers = new HashMap<>();
        initializers.put(tcpPort, tcpInitializer);
        initializers.put(httpPort, httpInitializer);
        return initializers;
    }

}
