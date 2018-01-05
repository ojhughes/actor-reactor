package me.ohughes.actorreactor.publishers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.BaseStream;

import me.ohughes.actorreactor.functions.ExtractActorFunction;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class ReactiveFileLoader {
	public Flux<String> process(String filePath) {
		return fromPath(Paths.get(filePath));

	}

	private static Flux<String> fromPath(Path path) {
		return Flux.using(() -> Files.lines(path),
				Flux::fromStream,
				BaseStream::close
		);
	}

}
