package pl.grabla.rsocket;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketClient;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import pl.grabla.rsocket.dto.RequestDto;
import pl.grabla.rsocket.dto.ResponseDto;
import pl.grabla.rsocket.util.ObjectUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec05ConnectionSetupTest {

    private RSocketClient rSocketClient;
//    private int i ;

    @BeforeAll
    public void setup() {
//        i=1;
        Mono<RSocket> socketMono = RSocketConnector.create()
                .setupPayload(DefaultPayload.create("user1:password"))
                .connect(TcpClientTransport.create("localhost", 6565))
                .doOnNext(r-> System.out.println("going to connect"));

        this.rSocketClient =  RSocketClient.from(socketMono);
//                .block();
    }

    @Test
    public void connectionTest() throws Exception {

        Payload payload = ObjectUtil.toPayload(new RequestDto(5));
//        Queues
        Flux<ResponseDto> flux = this.rSocketClient.requestStream(Mono.just(payload))
                .map(p -> ObjectUtil.toObject(p, ResponseDto.class))

//                .delayElements(Duration.ofMillis(300))
//                .take(10)
                .doOnNext(System.out::println);
        StepVerifier.create(flux).expectNextCount(10).verifyComplete();





    }
}
