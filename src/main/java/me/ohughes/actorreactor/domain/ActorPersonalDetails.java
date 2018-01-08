package me.ohughes.actorreactor.domain;

import java.time.Year;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ActorPersonalDetails {
	private String fullName;
	private Year yearOfBirth;
}
