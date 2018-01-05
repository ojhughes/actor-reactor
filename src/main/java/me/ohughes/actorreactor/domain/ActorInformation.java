package me.ohughes.actorreactor.domain;

import lombok.Data;

@Data
public class ActorInformation {
	private ActorPersonalDetails actorPersonalDetails;
	private RecentNews recentNews;
	private AcclaimedPerformance acclaimedPerformance;
}
