import java.io.*;
import java.util.logging.*;

import compressor.*;
import decompressor.*;

public class HuffmanZipperImpl implements IFileZipper{

    Logger logger = Logger.getLogger(HuffmanDecompressorAlgoImpl.class.getName());

    @Override
    public void compress(String originalFileName, String compFileName) throws IOException{
        ICompressor compObj = new HuffmanCompressorImpl();
        long startTimeC= System.currentTimeMillis();
        compObj.compress(originalFileName, compFileName);     
        long endTimeC= System.currentTimeMillis();  
        String msg= String.format("Compression time : %d",(endTimeC-startTimeC));
        logger.log(Level.INFO, msg );

    }

    @Override
    public void decompress(String compFileName,String originalFileName, String decompFileName) throws IOException{
        IDecompressor decompObj = new HuffmanDecompressorImpl();
        long startTimeD= System.currentTimeMillis();
        decompObj.decompress(compFileName,originalFileName, decompFileName);
        long endTimeD= System.currentTimeMillis();
        String msg= String.format("Decompression time : %d",(endTimeD-startTimeD));
        logger.log(Level.INFO, msg );
    }
    
}
