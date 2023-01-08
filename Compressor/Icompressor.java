package Compressor;
import java.io.*;

public interface Icompressor {
    public void compress(String orgFileName, String compFileName) throws IOException;
}