package com.derby.nuke.dlm.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.derby.nuke.dlm.server.netty.ServerInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 
 * @author Passyt
 *
 */
public class NettyServer {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors() * 2;
	protected static final int BIZTHREADSIZE = 4;

	private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
	private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);

	private final ServerInitializer initializer;
	private final int port;

	public NettyServer(ServerInitializer initializer, int port) {
		this.initializer = initializer;
		this.port = port;
	}

	public void run() throws Exception {
		Thread thread = new Thread(() -> {
			try {
				ServerBootstrap b = new ServerBootstrap();
				b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(this.initializer);

				logger.info("Server started at port {} with {}", port, initializer);
				Channel ch = b.bind(this.port).sync().channel();
				ch.closeFuture().sync();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			} finally {
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
			}
		});
		thread.start();
	}
}
