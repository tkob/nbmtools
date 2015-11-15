package nbmtools;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import lombok.Value;
import org.apache.commons.collections4.EnumerationUtils;
import org.apache.commons.io.IOUtils;

public class ZipUtils {
    public static interface Entry extends Closeable {};

    @Value
    public static class DirEntry implements Entry {
        ZipEntry zipEntry;

        @Override public void close() {}
    }

    @Value
    public static class FileEntry implements Entry {
        ZipEntry zipEntry;
        InputStream inputStream;

        @Override
        public void close() throws IOException {
            inputStream.close();
        }
    }

    public static <T> T fold(
            BiFunction<Entry, T, T> f,
            T init,
            ZipInputStream zis)
            throws IOException {
        T result = init;
        for (ZipEntry zipEntry = zis.getNextEntry();
                zipEntry != null;
                zipEntry = zis.getNextEntry()) {
            Entry entry = zipEntry.isDirectory()
                    ? new DirEntry(zipEntry)
                    : new FileEntry(zipEntry, zis);
            result = f.apply(entry, result);
        }
        return result;
    }

    public static <T> T fold(
            BiFunction<Entry, T, T> f,
            T init,
            ZipFile zipFile)
            throws IOException {
        T result = init;
        List<ZipEntry> zipEntries = EnumerationUtils.toList(zipFile.entries());
        for (ZipEntry zipEntry : zipEntries) {
            Entry entry = zipEntry.isDirectory()
                    ? new DirEntry(zipEntry)
                    : new FileEntry(
                            zipEntry, zipFile.getInputStream(zipEntry));
            result = f.apply(entry, result);
        }
        return result;
    }

    public static void mapToFile(
            Function<Entry, Entry> f,
            ZipFile zipFile,
            File toFile)
            throws IOException {
        try (FileOutputStream fos = new FileOutputStream(toFile);
                ZipOutputStream zos = new ZipOutputStream(fos)) {
            List<ZipEntry> zipEntries =
                    EnumerationUtils.toList(zipFile.entries());
            for (ZipEntry zipEntry : zipEntries) {
                Entry entry = zipEntry.isDirectory()
                        ? new DirEntry(zipEntry)
                        : new FileEntry(
                                zipEntry, zipFile.getInputStream(zipEntry));
                try (Entry newEntry = f.apply(entry)) {
                    if (newEntry instanceof DirEntry) {
                        zos.putNextEntry(((DirEntry)newEntry).getZipEntry());
                    } else {
                        zos.putNextEntry(((FileEntry)newEntry).getZipEntry());
                        IOUtils.copy(
                                ((FileEntry)newEntry).getInputStream(), zos);
                    }
                }
            }
            zos.finish();
        }
    }
}
