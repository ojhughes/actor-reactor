package me.ohughes.actorreactor.functions;

import java.util.function.Function;

import reactor.core.publisher.Flux;

public class ExtractActorFunction implements Function<Flux<String>, Flux<String>> {
	@Override
	public  Flux<String> apply(Flux<String> fileLinesFlux) {
		return fileLinesFlux.map(s -> s.split("\t"))
							.filter(a -> a.length > 1)
							.map(a -> a[1]);
	}
}
