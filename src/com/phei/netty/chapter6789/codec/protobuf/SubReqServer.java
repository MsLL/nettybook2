/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.phei.netty.chapter6789.codec.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author lilinfeng
 * @version 1.0
 * @date 2014年2月14日
 */
public class SubReqServer {
    public void bind(int port) throws Exception {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        //NOTE-UPUP 2021/4/2 上午12:49 : 添加ProtobufVarin解/编码器

                        //NOTE-UPUP 2021/4/5 上午1:51 : 1.去掉表示消息长度的消息头
                        ch.pipeline().addLast(
                            new ProtobufVarint32FrameDecoder());
                        //NOTE-UPUP 2021/4/5 上午1:52 : 2.消息体解码为对象
                        ch.pipeline().addLast(
                            new ProtobufDecoder(
                                SubscribeReqProto.SubscribeReq
                                    .getDefaultInstance()));

                        //NOTE-UPUP 2021/4/5 上午2:01 :  Netty中，可以注册多个handler。ChannelInboundHandler按照注册的先后顺序执行；ChannelOutboundHandler按照注册的先后顺序逆序执行
                        //NOTE-UPUP 2021/4/5 上午2:56 : 2.出站消息加上长度头
                        ch.pipeline().addLast(
                            new ProtobufVarint32LengthFieldPrepender());
                        //NOTE-UPUP 2021/4/5 上午2:55 : 1.出站消息先编码
                        ch.pipeline().addLast(new ProtobufEncoder());

                        ch.pipeline().addLast(new SubReqServerHandler());
                    }
                });

            // 绑定端口，同步等待成功
            ChannelFuture f = b.bind(port).sync();

            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        new SubReqServer().bind(port);
    }
}
