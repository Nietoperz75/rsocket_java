package pl.grabla.rsocket;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import pl.grabla.rsocket.dto.RequestDto;
import pl.grabla.rsocket.dto.ResponseDto;
import pl.grabla.rsocket.util.ObjectUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.sql.SQLOutput;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec01RSocketTest {

    private RSocket rSocket;
//    private int i ;

    @BeforeAll
    public void setup(){
//        i=1;
        this.rSocket = RSocketConnector.create()
                .connect(TcpClientTransport.create("localhost", 6565))
                .block();
    }
    @Test
    public void fireAndForget(){

        Payload payload = ObjectUtil.toPayload(new RequestDto(1));
        Mono<Void> mono =  this.rSocket.fireAndForget(payload);
        StepVerifier.create(mono)
                .verifyComplete();
    }

    @Test
    public void requestResponse(){

        Payload payload = ObjectUtil.toPayload(new RequestDto(100));
        Mono<ResponseDto> mono = this.rSocket.requestResponse(payload).map(p-> ObjectUtil.toObject(p, ResponseDto.class)).doOnNext(System.out::println);
        StepVerifier.create(mono)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void requestStream(){

        Payload payload = ObjectUtil.toPayload(new RequestDto(12));
        Flux<ResponseDto> flux = this.rSocket.requestStream(payload)
                .map(p-> ObjectUtil.toObject(p, ResponseDto.class)).doOnNext(System.out::println);
        StepVerifier.create(flux)
                .expectNextCount(10)
                .verifyComplete();
    }


}
