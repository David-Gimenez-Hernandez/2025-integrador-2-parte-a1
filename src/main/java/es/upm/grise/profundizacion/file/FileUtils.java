package es.upm.grise.profundizacion.file;


import java.util.zip.CRC32;
import java.util.Objects;

public class FileUtils {


	static long calculateCRC32(byte[] data) {
		Objects.requireNonNull(data, "data no puede ser null");
		if (data.length == 0) {
			throw new IllegalArgumentException("calculateCRC32 no admite arrays vacíos");
		}
		CRC32 crc = new CRC32();
		crc.update(data);
		return crc.getValue();
	}


}
