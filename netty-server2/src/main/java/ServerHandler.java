import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by d.cao on 2016/12/22.
 */
public class ServerHandler extends SimpleChannelInboundHandler {

    private int counter = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message req = (Message) msg;
        System.out.println("Server received: [" + req.toString() + "], counter is " + ++counter);

        // Do something logic work
        // Here, just echo for a demo
        ctx.writeAndFlush(req);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("connect active");
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("connect inactive");
        ctx.fireChannelInactive();
    }

}
