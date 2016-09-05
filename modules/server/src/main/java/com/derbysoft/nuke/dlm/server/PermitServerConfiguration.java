package com.derbysoft.nuke.dlm.server;

import com.derbysoft.nuke.dlm.IPermitManager;
import com.derbysoft.nuke.dlm.IPermitService;
import com.derbysoft.nuke.dlm.PermitService;
import com.derbysoft.nuke.dlm.server.initializer.PermitServerInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by passyt on 16-9-4.
 */
@Configuration
@ComponentScan
public class PermitServerConfiguration {

    @Bean(name = "bossGroup")
    public EventLoopGroup bossGroup() {
        return new NioEventLoopGroup();
    }

    @Bean(name = "workerGroup")
    public EventLoopGroup workerGroup() {
        return new NioEventLoopGroup();
    }

    @Bean(name = "permitService")
    public IPermitService permitService(IPermitManager permitManager) {
        return new PermitService(permitManager);
    }

    @Bean(name = "permServerInitializers")
    public Map<Integer, PermitServerInitializer> initializers(
            @Value("${server.tcp.port}") int tcpPort, @Qualifier("permitServerTcpInitializer") PermitServerInitializer tcpInitializer,
            @Value("${server.http.port}") int httpPort, @Qualifier("permitServerHttpInitializer") PermitServerInitializer httpInitializer
    ) {
        Map<Integer, PermitServerInitializer> initializers = new HashMap<>();
        initializers.put(tcpPort, tcpInitializer);
        initializers.put(httpPort, httpInitializer);
        return initializers;
    }

}
