/******************************************************************************
* ~ Copyright (c) 2018 [jasonandy@hotmail.com | https://github.com/Jasonandy] *
* ~                                                                           *
* ~ Licensed under the Apache License, Version 2.0 (the "License”);           * 
* ~ you may not use this file except in compliance with the License.          *
* ~ You may obtain a copy of the License at                                   *
* ~                                                                           *
* ~    http://www.apache.org/licenses/LICENSE-2.0                             *
* ~                                                                           *
* ~ Unless required by applicable law or agreed to in writing, software       *
* ~ distributed under the License is distributed on an "AS IS" BASIS,         *
* ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *
* ~ See the License for the specific language governing permissions and       *
* ~ limitations under the License.                                            *
******************************************************************************/
package cn.ucaner.netty.demo.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**     
 * @Package：cn.ucaner.netty.demo.client   
 * @ClassName：DiscardClientHandler   
 * @Description：   <p> DiscardClientHandler  </br> channelActive、channelRead和exceptionCaught </p>
 * @Author： - Jason   
 * @Modify By：   
 * @ModifyTime：  2018年4月15日
 * @Modify marker：   
 * @version    V1.0
 */
public class DiscardClientHandler extends ChannelHandlerAdapter{

	
	private final ByteBuf firstMessage;
	
	/**
	 * 指令处理器
	 * DiscardClientHandler.
	 */
	public DiscardClientHandler() {
	    byte[] req = "I WANT KNOW WHO IS JASON".getBytes();
	    firstMessage = Unpooled.buffer(req.length);
	    firstMessage.writeBytes(req);
	}

	
	//当客户端和服务端TCP链路建立成功之后，Netty的NIO线程会调用channelActive方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //发送查询时间的指令给服务端
        //调用ChannelHandlerContext的writeAndFlush方法将请求消息发送给服务端。
        ctx.writeAndFlush(firstMessage);
    }
    
    
    //当服务端返回应答消息时，channelRead方法被调用
	@Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
        try {  
            ByteBuf buf = (ByteBuf) msg;  
            byte[] data = new byte[buf.readableBytes()];  
            buf.readBytes(data);
            //String body = new String(data, "UTF-8");
            System.out.println("Client Call Channel Read :" + new String(data,"UTF-8").trim());  
        } finally {  
            ReferenceCountUtil.release(msg);  
        }  
    }  
	  
	  
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception { 
    	
        cause.printStackTrace();  
        ctx.close();  
    }  
}
