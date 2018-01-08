package me.ohughes.actorreactor.domain;

import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ActorInformation {
	private ActorPersonalDetails actorPersonalDetails;
	private RecentNews recentNews;
	private AcclaimedPerformance acclaimedPerformance;
	private Map<String, String> flattenedActorInformation;

	public Map<String, String> getFlattenedActorInformation(){
		AcclaimedPerformance acclaimedPerformance = Optional.ofNullable(getAcclaimedPerformance()).orElse(new AcclaimedPerformance());
		ActorPersonalDetails personalDetails = Optional.ofNullable(getActorPersonalDetails()).orElse(new ActorPersonalDetails("", Year.of(1)));
		RecentNews recentNews = Optional.ofNullable(getRecentNews()).orElse(new RecentNews());

		Map<String, String> actorData = new HashMap<>();

		actorData.put("fullName", personalDetails.getFullName());
		actorData.put("yearOfBirth", personalDetails.getYearOfBirth().toString());
		actorData.put("acclaimedMovieId", acclaimedPerformance.getMovieId());
		actorData.put("acclaimedMovieName", acclaimedPerformance.getMovieName());
		actorData.put("recentNewsSource", recentNews.getSource());
		actorData.put("recentNewsContent", recentNews.getTweet());
		return actorData;
	}

}
