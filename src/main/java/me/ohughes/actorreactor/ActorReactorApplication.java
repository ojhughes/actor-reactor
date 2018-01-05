package me.ohughes.actorreactor;

import me.ohughes.actorreactor.publishers.ReactiveFileLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.System.exit;


@SpringBootApplication
public class ActorReactorApplication implements CommandLineRunner {

	final ReactiveFileLoader reactiveFileLoader;

	@Autowired
	public ActorReactorApplication(ReactiveFileLoader reactiveFileLoader) {
		this.reactiveFileLoader = reactiveFileLoader;
	}

	public static void main(String[] args) throws Exception {

		SpringApplication app = new SpringApplication(ActorReactorApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);

	}

	// Put your logic here.
	@Override
	public void run(String... args) throws Exception {

		reactiveFileLoader.process("/Users/ohughes/datasets/top-actors.tsv");
		exit(0);
	}
}