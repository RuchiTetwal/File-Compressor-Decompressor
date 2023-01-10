import java.io.*;

import compressor.*;
import decompressor.*;

public class HuffmanZipperImpl implements IFileZipper{

    @Override
    public void compress(String originalFileName, String compFileName) throws IOException{
        ICompressor compObj = new HuffmanCompressorImpl();
        compObj.compress(originalFileName, compFileName);       
    }

    @Override
    public void decompress(String compFileName,String originalFileName, String decompFileName) throws IOException{
        IDecompressor decompObj = new HuffmanDecompressorImpl();
        decompObj.decompress(compFileName,originalFileName, decompFileName);
    }
    
}
