package me.ohughes.actorreactor.subscribers;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import lombok.extern.slf4j.Slf4j;
import me.ohughes.actorreactor.domain.ActorInformation;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.stereotype.Component;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.quote.AlwaysQuoteMode;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class ExportEnrichedData {

	public void writeFluxToFile(Flux<ActorInformation> enrichedData, String outputPath){

		DefaultDataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
		Path file = Paths.get(outputPath);

		try(AsynchronousFileChannel asyncFile = AsynchronousFileChannel.open(file,
				StandardOpenOption.WRITE,
				StandardOpenOption.CREATE)) {

			Flux<DataBuffer> bufferedData = enrichedData.map(fileLine -> {
				byte[] bytes = formatFileLine(fileLine);
				return dataBufferFactory.wrap(bytes);
			});			Flux<DataBuffer> writeResult = DataBufferUtils.write(bufferedData, asyncFile, 0);
			writeResult.map(DataBufferUtils::release).blockLast();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private byte[] formatFileLine(ActorInformation actorInformation) {

		try (
				ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
				Writer inMemoryWriter = new BufferedWriter(new OutputStreamWriter(byteOutputStream));
				CsvMapWriter csvWriter = new CsvMapWriter(inMemoryWriter,
						new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE).useQuoteMode(new AlwaysQuoteMode()).build(),
						false);
		) {

			String[] fields = {
					"fullName",
					"yearOfBirth",
					"acclaimedMovieId",
					"acclaimedMovieName",
					"recentNewsSource",
					"recentNewsContent",
			};

			csvWriter.write(actorInformation.getFlattenedActorInformation(), fields);
			inMemoryWriter.flush();
			return byteOutputStream.toByteArray();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
