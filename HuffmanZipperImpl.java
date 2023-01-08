import java.io.*;

import Compressor.*;
import Decompressor.*;

public class HuffmanZipperImpl implements IFileZipper{

    @Override
    public void compress(String originalFileName, String compFileName) throws IOException{
        Icompressor compObj = new CompressorImpl();
        compObj.compress(originalFileName, compFileName);       
    }

    @Override
    public void decompress(String compFileName,String originalFileName, String decompFileName) throws IOException{
        IDecompressor decompObj = new DecompressorImpl();
        decompObj.decompress(compFileName,originalFileName, decompFileName);
    }
    
}
