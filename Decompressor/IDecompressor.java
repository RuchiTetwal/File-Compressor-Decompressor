package Decompressor;
import java.io.*;

public interface IDecompressor {
    public void decompress(String compFileName,String orgFileName, String decompFileName) throws IOException;
}
