package com.java.g.w03.netty.outbound.netty4;

public class NettyHttpClient {
//        public void connect(String host, int port) throws Exception {
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//
//        try {
//            Bootstrap b = new Bootstrap();
//            b.group(workerGroup);
//            b.channel(NioSocketChannel.class);
//            b.option(ChannelOption.SO_KEEPALIVE, true);
//            b.handler(new ChannelInitializer<SocketChannel>() {
//                @Override
//                public void initChannel(SocketChannel ch) throws Exception {
//                    // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
//                    ch.pipeline().addLast(new HttpResponseDecoder());
//                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
//                    ch.pipeline().addLast(new HttpRequestEncoder());
//                    ch.pipeline().addLast(new NettyHttpClientOutboundHandler()); //
//                }
//            });
//
//            // Start the client.
//            ChannelFuture f = b.connect(host, port).sync();
//
//
//            f.channel().write(request);
//            f.channel().flush();
//            f.channel().closeFuture().sync();
//        } finally {
//            workerGroup.shutdownGracefully();
//        }
//
//    }
//
//    public static void main(String[] args) throws Exception {
//        NettyHttpClient client = new NettyHttpClient();
//        client.connect("127.0.0.1", 8844);
//    }
}
