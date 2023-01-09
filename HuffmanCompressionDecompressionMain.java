import java.io.IOException;
import java.util.logging.*;

public class HuffmanCompressionDecompressionMain {
    
    public static void main(String[] args)  throws IOException{
        
        Logger logger = Logger.getLogger(HuffmanCompressionDecompressionMain.class.getName());

        if(args.length!=3){
            logger.log(Level.WARNING, "Command to run : HuffmanCompressionDecompressionMain OriginalFileName CompressionFileName DecompressionFileName");
            return;
        }
        String originalFileName=args[0];
        String compFileName=args[1];
        String decompFileName=args[2];

        IFileZipper zipperObj = new HuffmanZipperImpl();

        zipperObj.compress(originalFileName, compFileName);
        zipperObj.decompress(compFileName,originalFileName, decompFileName);

    }
}
