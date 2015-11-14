package nbmtools;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import yokohama.unit.annotations.As;

@As
public class TestUtils {
    @As
    public static InputStream asInputStream(String s) throws IOException {
        return IOUtils.toInputStream(s, "UTF-8");
    }
}
