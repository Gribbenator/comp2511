package unsw.test;

import org.junit.jupiter.api.Test;

import unsw.archaic_fs.ArchaicFileSystem;
import unsw.archaic_fs.FileWriteOptions;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.util.EnumSet;

public class ArchaicFsTest {
    @Test
    public void testCdInvalidDirectory() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        // Try to change directory to an invalid one
        assertThrows(NoSuchFileException.class, () -> {
            fs.cd("/usr/bin/cool-stuff");
        });
    }

    @Test
    public void testCdValidDirectory() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertDoesNotThrow(() -> {
            fs.mkdir("/usr/bin/cool-stuff", true, false);
            fs.cd("/usr/bin/cool-stuff");
        });
    }

    @Test
    public void testCdAroundPaths() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertDoesNotThrow(() -> {
            fs.mkdir("/usr/bin/cool-stuff", true, false);
            fs.cd("/usr/bin/cool-stuff");
            assertEquals("/usr/bin/cool-stuff", fs.cwd());
            fs.cd("..");
            assertEquals("/usr/bin", fs.cwd());
            fs.cd("../bin/..");
            assertEquals("/usr", fs.cwd());
        });
    }

    @Test
    public void testCreateFile() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertDoesNotThrow(() -> {
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
            assertEquals("My Content", fs.readFromFile("test.txt"));
            fs.writeToFile("test.txt", "New Content", EnumSet.of(FileWriteOptions.TRUNCATE));
            assertEquals("New Content", fs.readFromFile("test.txt"));
        });
    }

    // Now write some more!
    // Some ideas to spark inspiration;
    // - File Writing/Reading with various options (appending for example)
    // - Cd'ing .. on the root most directory (shouldn't error should just remain on root directory)
    // - many others...
    
    
    @Test
    public void testCdRootDirectory() {
        ArchaicFileSystem fs = new ArchaicFileSystem();
        assertDoesNotThrow(() -> {
            fs.cd("/usr");
        });
    }
    
    @Test
    public void testMkdirFileAlreadyExists() {
        ArchaicFileSystem fs = new ArchaicFileSystem();
        
        assertDoesNotThrow(() -> {
            fs.mkdir("/usr/bin/stuff", true, false);
            fs.mkdir("/usr/bin/stuff", true, true);
        });

        // Try to make a directory that already exists
        assertThrows(FileAlreadyExistsException.class, () -> {
            fs.mkdir("/usr/bin/stuff", true, false);
        });
    }
    
    
    @Test
    public void integrationTest() {
        ArchaicFileSystem fs = new ArchaicFileSystem();
    
        assertDoesNotThrow(() -> {
            fs.mkdir("/usr/bin/example", true, false);
            fs.cd("/usr/bin/example");
            fs.writeToFile("stuff.txt", "STUFF", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
            assertEquals("STUFF", fs.readFromFile("stuff.txt"));
        });
    }
}
