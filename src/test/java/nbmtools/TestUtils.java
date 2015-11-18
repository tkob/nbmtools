package nbmtools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;
import scala.collection.JavaConversions;
import yokohama.unit.annotations.As;

@As
public class TestUtils {
    public static InputStream asInputStream(String s) throws IOException {
        return IOUtils.toInputStream(s, "UTF-8");
    }

    public static URI asURI(String s) {
        return URI.create(s);
    }

    public static scala.collection.immutable.List<URI> uris(List<String> urls) {
        List<URI> uris =
                urls.stream().map(URI::create).collect(Collectors.toList());
        return JavaConversions.asScalaBuffer(uris).toList();
    }

    public static void createZipFile(File file, List<ZipEntry> entries)
            throws IOException {
        try(FileOutputStream fos = new FileOutputStream(file);
                ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (ZipEntry entry : entries) {
                if (entry instanceof DirEntry) continue;
                FileEntry fileEntry = (FileEntry)entry;
				zos.putNextEntry(fileEntry.getZipEntry());
                IOUtils.copy(fileEntry.getInputStream(), zos);
            }
        }
    }
}
