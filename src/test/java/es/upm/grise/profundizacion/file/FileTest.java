package es.upm.grise.profundizacion.file;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileTest {

    private File propertyFile;
    private File imageFile;

    @BeforeEach
    void setUp() {
        propertyFile = new File(FileType.PROPERTY);
        imageFile = new File(FileType.IMAGE);
    }

    @Test
    void constructorInitializesEmptyContent() {
        assertEquals(0, propertyFile.size());
    }

    @Test
    void addPropertyWorksForPropertyFile() throws Exception {
        propertyFile.addProperty("KEY=VALUE".toCharArray());
        assertEquals("KEY=VALUE".length(), propertyFile.size());
    }

    @Test
    void addPropertyThrowsInvalidContentExceptionWhenNull() {
        assertThrows(InvalidContentException.class, () -> propertyFile.addProperty(null));
    }

    @Test
    void addPropertyThrowsWrongFileTypeExceptionForImageFile() {
        assertThrows(WrongFileTypeException.class, () -> imageFile.addProperty("X=Y".toCharArray()));
    }

    @Test
    void getCRC32ReturnsZeroWhenEmpty() {
        assertEquals(0L, propertyFile.getCRC32());
    }

    @Test
    void getCRC32UsesFileUtilsForNonEmptyContent() throws Exception {
        propertyFile.addProperty("AB".toCharArray());
        try (MockedStatic<FileUtils> mocked = mockStatic(FileUtils.class)) {
            mocked.when(() -> FileUtils.calculateCRC32(any(byte[].class))).thenReturn(1234L);
            long crc = propertyFile.getCRC32();
            assertEquals(1234L, crc);
            mocked.verify(() -> FileUtils.calculateCRC32(any(byte[].class)), times(1));
        }
    }


    @Test
    void calculateCRC32WorksForValidArray() {
        byte[] data = {1, 2, 3};
        long crc = FileUtils.calculateCRC32(data);
        assertTrue(crc > 0);
    }

    @Test
    void calculateCRC32ThrowsNullPointerExceptionForNull() {
        assertThrows(NullPointerException.class, () -> FileUtils.calculateCRC32(null));
    }

    @Test
    void calculateCRC32ThrowsIllegalArgumentExceptionForEmptyArray() {
        assertThrows(IllegalArgumentException.class, () -> FileUtils.calculateCRC32(new byte[]{}));
    }

}

