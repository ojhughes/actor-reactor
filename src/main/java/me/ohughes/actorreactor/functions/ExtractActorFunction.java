package me.ohughes.actorreactor.functions;

import java.time.Year;
import java.util.function.Function;

import me.ohughes.actorreactor.domain.ActorPersonalDetails;
import reactor.core.publisher.Flux;

import static java.lang.Integer.parseInt;

public class ExtractActorFunction implements Function<Flux<String>, Flux<ActorPersonalDetails>> {
	@Override
	public  Flux<ActorPersonalDetails> apply(Flux<String> fileLinesFlux) {
		return fileLinesFlux.map(s -> s.split("\t"))
							.filter(a -> a.length > 2)
							.filter(a -> isNumber(a[2]))
							.map(a -> ActorPersonalDetails.builder()
									.fullName(a[1])
									.yearOfBirth(Year.of(parseInt(a[2])))
									.build());
	}

	private boolean isNumber(String s) {
		try {
			parseInt(s);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
}
