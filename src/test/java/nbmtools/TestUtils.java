package nbmtools;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import scala.collection.JavaConversions;
import yokohama.unit.annotations.As;

@As
public class TestUtils {
    @As
    public static InputStream asInputStream(String s) throws IOException {
        return IOUtils.toInputStream(s, "UTF-8");
    }

    public static scala.collection.immutable.List<URI> uris(List<String> urls) {
        List<URI> uris =
                urls.stream().map(URI::create).collect(Collectors.toList());
        return JavaConversions.asScalaBuffer(uris).toList();
    }
}
