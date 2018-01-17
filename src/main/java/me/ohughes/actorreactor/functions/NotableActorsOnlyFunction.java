package me.ohughes.actorreactor.functions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.function.Function;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.ohughes.actorreactor.domain.ActorPersonalDetails;
import org.springframework.web.reactive.function.client.WebClient;
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
						.bodyToFlux(String.class))
						.map(NotableActorsOnlyFunction::handleResponse);
		return null;
	}

	private static Flux<String> handleResponse(Flux<String> responseMono) {
		return responseMono.map(response -> {
			JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
			String wikiContent = "";
			for (Map.Entry<String, JsonElement> dynamicKey : jsonObject.getAsJsonObject("query").getAsJsonObject("pages").entrySet()) {
				JsonObject page = (JsonObject) dynamicKey.getValue();
				JsonArray revisionsArray = page.getAsJsonArray("revisions");
				if (revisionsArray.size() == 0) {
					System.exit(1);
				}
				wikiContent = revisionsArray.get(0).getAsJsonObject().getAsJsonPrimitive("*").getAsString();
			}
			return wikiContent;
		});
	}

	private static WebClient buildWebClient(String actorName) {
		try {
			return WebClient.create(WIKI_BASE_URL + URLEncoder.encode(actorName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
