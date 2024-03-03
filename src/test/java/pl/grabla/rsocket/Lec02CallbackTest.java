package pl.grabla.rsocket;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import pl.grabla.rsocket.client.CallbackService;
import pl.grabla.rsocket.dto.RequestDto;
import pl.grabla.rsocket.util.ObjectUtil;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec02CallbackTest {

    private RSocket rSocket;
//    private int i ;

    @BeforeAll
    public void setup(){
//        i=1;
        this.rSocket = RSocketConnector.create()
                .acceptor(SocketAcceptor.with(new CallbackService()))
                .connect(TcpClientTransport.create("localhost", 6565))
                .block();
    }
    @Test
    public void callback() throws InterruptedException {
        RequestDto requestDto = new RequestDto(5);
        Mono<Void> mono = this.rSocket.fireAndForget(ObjectUtil.toPayload(requestDto));
        StepVerifier.create(mono).verifyComplete();
        System.out.println("going to wait");
        Thread.sleep(Duration.ofSeconds(12));
    }
}
