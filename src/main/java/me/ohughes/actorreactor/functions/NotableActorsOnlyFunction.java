package me.ohughes.actorreactor.functions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import me.ohughes.actorreactor.domain.ActorPersonalDetails;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Include only actors notable enough to have a wikipedia page
 */
public class NotableActorsOnlyFunction implements Function<Flux<ActorPersonalDetails>, Flux<ActorPersonalDetails>> {

	private static final String WIKI_BASE_URL = "https://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=jsonfm&titles=";

	@Override
	public Flux<ActorPersonalDetails> apply(Flux<ActorPersonalDetails> personalDetailsFlux) {
		personalDetailsFlux
				.map(actor -> buildWebClient(actor.getFullName())
						.get()
						.retrieve()
						.bodyToMono(String.class))
						.subscribe(NotableActorsOnlyFunction::handleRespose);
	}

	private static void handleRespose(Mono<String> responseMono) {

	}

	private static WebClient buildWebClient(String actorName) {
		try {
			return WebClient.create(WIKI_BASE_URL + URLEncoder.encode(actorName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	}
}
