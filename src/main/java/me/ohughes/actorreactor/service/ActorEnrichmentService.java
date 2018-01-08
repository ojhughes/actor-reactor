package me.ohughes.actorreactor.service;

import me.ohughes.actorreactor.domain.AcclaimedPerformance;
import me.ohughes.actorreactor.domain.ActorInformation;
import me.ohughes.actorreactor.domain.ActorPersonalDetails;
import me.ohughes.actorreactor.domain.RecentNews;
import me.ohughes.actorreactor.functions.ExtractActorFunction;
import me.ohughes.actorreactor.publishers.ReactiveFileLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ActorEnrichmentService {

	private ReactiveFileLoader reactiveFileLoader;

	@Autowired
	public ActorEnrichmentService(ReactiveFileLoader reactiveFileLoader) {
		this.reactiveFileLoader = reactiveFileLoader;
	}

	public Flux<ActorInformation> enrich(String path) {
		Flux<String> fileFlux = reactiveFileLoader.process(path);
		Flux<ActorPersonalDetails> actorDetailFlux = fileFlux.transform(new ExtractActorFunction());
		Flux<ActorPersonalDetails> notableActorsOnly = filterNotableActorsOnly(actorDetailFlux);
		Flux<ActorPersonalDetails> sortedNotableActors = sortActors(notableActorsOnly);
		Flux<AcclaimedPerformance> enrichedWithAcclaimedPerformances = enrichWithAcclaimedPerforamances(sortedNotableActors);
		Flux<RecentNews> enrichedWithRecentNews = enrichWithRecentNews(sortedNotableActors);
		return mergeEnrichedData(enrichedWithAcclaimedPerformances, enrichedWithRecentNews, sortedNotableActors);
	}

	private Flux<ActorInformation> mergeEnrichedData(Flux<AcclaimedPerformance> enrichedWithAcclaimedPerformances, Flux<RecentNews> enrichedWithRecentNews, Flux<ActorPersonalDetails> sortedActors) {
		return sortedActors.map(a -> ActorInformation.builder().actorPersonalDetails(a).build());
	}

	private Flux<ActorPersonalDetails> sortActors(Flux<ActorPersonalDetails> notableActorsOnly) {
		return notableActorsOnly;
	}

	private Flux<RecentNews> enrichWithRecentNews(Flux<ActorPersonalDetails> notableActorsOnly) {
		return null;
	}

	private Flux<AcclaimedPerformance> enrichWithAcclaimedPerforamances(Flux<ActorPersonalDetails> notableActorsOnly) {
		return null;
	}

	private Flux<ActorPersonalDetails> filterNotableActorsOnly(Flux<ActorPersonalDetails> actorDetailFlux) {
		return actorDetailFlux;
	}
}
