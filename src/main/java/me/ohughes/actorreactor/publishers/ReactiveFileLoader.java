package me.ohughes.actorreactor.publishers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class ReactiveFileLoader {
	public Flux<String> process(String filePath) {
		return fromPath(Paths.get(filePath));

	}

	private static Flux<String> fromPath(Path sourcePath) {
		return Flux.fromStream(() -> {
			try {
				return Files.lines(sourcePath);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

}
