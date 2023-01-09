package compressor;

import java.io.IOException;
import java.util.Map;

public interface IEncodeOrgFile {
    void encodeOrgFile(byte[] orgFileList, String compFileName, Map<Byte, String> huffmanMap, HuffmanNode root) throws IOException;  
}
