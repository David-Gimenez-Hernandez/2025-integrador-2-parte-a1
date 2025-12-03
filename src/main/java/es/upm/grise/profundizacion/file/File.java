package es.upm.grise.profundizacion.file;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class File {

    private FileType type;
    private List<Character> content;

	/*
	 * Constructor
	 */
    public File() {
		this.type = Objects.requireNonNull(type, "type no puede ser null");
		this.content = new ArrayList<>(); // vacío, pero NO nul

	}

	/*
	 * Method to code / test
	 */
    public void addProperty(char[] newcontent)
			throws InvalidContentException, WrongFileTypeException {
		if (newcontent == null) {
			throw new InvalidContentException("El contenido proporcionado es null");
		}
		if (this.type == FileType.IMAGE) {
			throw new WrongFileTypeException("No se puede añadir propiedades a un archivo de tipo IMAGE");
		}
		for (char c : newcontent) {
			this.content.add(c);
		}
	}


	/*
	 * Method to code / test
	 */
    public long getCRC32() {
		if (this.content.isEmpty()) {
			return 0L; // La especificación establece devolver 0 si no hay contenido
		}
		byte[] bytes = toByteArray();
		// Aquí el array NO está vacío; la utilidad no admite arrays vacíos.
		return FileUtils.calculateCRC32(bytes);


	}


	/**
	 * Conversión del content a byte[] según el tipo de archivo:
	 * - PROPERTY: 2 bytes por char (MSB, LSB) -> equivalente a big-endian.
	 * - IMAGE: 1 byte por char (LSB).
	 */
	private byte[] toByteArray() {
		if (this.type == FileType.PROPERTY) {
			// 2 bytes por carácter
			byte[] out = new byte[this.content.size() * 2];
			int i = 0;
			for (char c : this.content) {
				int msb = (c >>> 8) & 0xFF;
				int lsb = c & 0xFF;
				out[i++] = (byte) msb; // MSB
				out[i++] = (byte) lsb; // LSB
			}
			return out;
		} else {
			// IMAGE: solo LSB
			byte[] out = new byte[this.content.size()];
			int i = 0;
			for (char c : this.content) {
				int lsb = c & 0xFF;
				out[i++] = (byte) lsb;
			}
			return out;
		}
	}



	/*
	 * Setters/getters
	 */
    public void setType(FileType type) {
    	
    	this.type = type;
    	
    }
    
    public List<Character> getContent() {
    	
    	return content;
    	
    }
    
}
