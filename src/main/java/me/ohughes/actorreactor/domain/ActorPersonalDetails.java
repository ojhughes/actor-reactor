package me.ohughes.actorreactor.domain;

import java.time.LocalDate;
import java.time.Year;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActorPersonalDetails {
	private String fullName;
	private Year yearOfBirth;
}
