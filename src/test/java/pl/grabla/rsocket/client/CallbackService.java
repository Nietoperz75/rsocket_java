package pl.grabla.rsocket.client;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import pl.grabla.rsocket.dto.ResponseDto;
import pl.grabla.rsocket.util.ObjectUtil;
import reactor.core.publisher.Mono;

public class CallbackService implements RSocket {

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        System.out.println("Client received : "+ ObjectUtil.toObject(payload, ResponseDto.class));

        return Mono.empty();
    }
}
