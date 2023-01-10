package compressor;

import java.io.IOException;
import java.util.Map;

public interface IHuffmanEncodeOrgFile {
    void encodeOrgFile(byte[] orgFileList, String compFileName, Map<Byte, String> huffmanMap, byte[] header) throws IOException;  
}
