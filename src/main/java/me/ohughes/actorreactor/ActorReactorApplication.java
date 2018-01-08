package me.ohughes.actorreactor;

import lombok.extern.slf4j.Slf4j;
import me.ohughes.actorreactor.domain.ActorInformation;
import me.ohughes.actorreactor.service.ActorEnrichmentService;
import me.ohughes.actorreactor.subscribers.ExportEnrichedData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import static java.lang.System.exit;

@SpringBootApplication
@Slf4j
public class ActorReactorApplication implements CommandLineRunner {

	private final ActorEnrichmentService actorEnrichmentService;
	private final ExportEnrichedData exportEnrichedData;

	@Autowired
	public ActorReactorApplication(ActorEnrichmentService actorEnrichmentService, ExportEnrichedData exportEnrichedData) {
		this.actorEnrichmentService = actorEnrichmentService;
		this.exportEnrichedData = exportEnrichedData;
	}

	public static void main(String[] args) throws Exception {

		SpringApplication app = new SpringApplication(ActorReactorApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);

	}

	@Override
	public void run(String... args) throws Exception {
		if(args.length < 2) {
			log.error("expected 2 arguments, path to input data & path to output data");
			exit(1);
		}
		String inputPath = args[0];
		String outputPath = args[1];
		if(!StringUtils.hasText(inputPath)){
			log.error("path to input data file must be proved");
			exit(1);
		}
		if(!StringUtils.hasText(outputPath)){
			log.error("path to output data file must be proved");
			exit(1);
		}
		Flux<ActorInformation> enrichedActorData = actorEnrichmentService.enrich(inputPath);
		exportEnrichedData.writeFluxToFile(enrichedActorData, outputPath);
		exit(0);
	}
}