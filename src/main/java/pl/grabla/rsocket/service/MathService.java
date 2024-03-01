package pl.grabla.rsocket.service;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import pl.grabla.rsocket.dto.RequestDto;
import pl.grabla.rsocket.dto.ResponseDto;
import pl.grabla.rsocket.util.ObjectUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class MathService implements RSocket {

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        System.out.println("Receiving : "+ObjectUtil.toObject(payload, RequestDto.class));
//        var response = ObjectUtil.toObject(payload, ResponseDto.class);

        return Mono.empty();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {

        return Mono.fromSupplier(() -> {
            RequestDto requestDto = ObjectUtil.toObject(payload, RequestDto.class);
            var input = requestDto.getInput();
            var kwadrat = input*input;
            ResponseDto responseDto = new ResponseDto(input, kwadrat);
            return ObjectUtil.toPayload(responseDto);
        });
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        RequestDto requestDto = ObjectUtil.toObject(payload, RequestDto.class);

        return Flux.range(1, 10).map(i -> i*requestDto.getInput())
                .map(w -> new ResponseDto(requestDto.getInput(), w))
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(System.out::println)
                .map(ObjectUtil::toPayload);
    }
}
