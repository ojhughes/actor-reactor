package me.ohughes.actorreactor.subscribers;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import lombok.extern.slf4j.Slf4j;
import me.ohughes.actorreactor.domain.ActorInformation;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.stereotype.Component;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.prefs.CsvPreference;
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

			enrichedData.map(this::formatFileLine).doOnNext(csvLine -> log.info(new String(csvLine))).blockLast();

//			Flux<DataBuffer> bufferedData = enrichedData.map(fileLine -> {
//				byte[] bytes = formatFileLine(fileLine);
//				return dataBufferFactory.wrap(bytes);
//			});
//			DataBufferUtils.write(bufferedData, asyncFile, 0);
//			bufferedData.blockLast();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private byte[] formatFileLine(ActorInformation actorInformation) {

		try (
				ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
				Writer inMemoryWriter = new BufferedWriter(new OutputStreamWriter(byteOutputStream));
				CsvMapWriter csvWriter = new CsvMapWriter(inMemoryWriter, CsvPreference.STANDARD_PREFERENCE);
		) {

			String[] fields = {
					"fullName",
					"yearOfBirth",
					"acclaimedMovieId",
					"acclaimedMovieName",
					"recentNewsSource",
					"recentNewsContent",
			};
			CellProcessor[] processors = new CellProcessor[] {
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional(),
					new Optional()
			};

					csvWriter.writeHeader(fields);
			csvWriter.write(actorInformation.getFlattenedActorInformation(), fields, processors);
			return byteOutputStream.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
