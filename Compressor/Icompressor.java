package compressor;
import java.io.*;

public interface ICompressor {
    public void compress(String orgFileName, String compFileName) throws IOException;
}