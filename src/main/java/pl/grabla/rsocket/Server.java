package pl.grabla.rsocket;

import io.rsocket.core.RSocketServer;
import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;
import pl.grabla.rsocket.service.SocketAcceptorImpl;

public class Server {
    public static void main(String[] args) {
        RSocketServer socketServer = RSocketServer.create(new SocketAcceptorImpl());
        CloseableChannel closeableChannel = socketServer.bindNow(TcpServerTransport.create(6565));

        //keep listening
        closeableChannel.onClose().block();
    }
}
