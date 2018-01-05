package me.ohughes.actorreactor.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ActorPersonalDetails {
	private String firstNames;
	private String lastNames;
	private LocalDate yearOfBirth;
}
