import java.io.IOException;

public class HuffmanCompressionDecompressionMain {
    
    public static void main(String args[])  throws IOException{
        String originalFileName=args[0];
        String compFileName=args[1];
        String decompFileName=args[2];

        IFileZipper zipperObj = new HuffmanZipperImpl();

        zipperObj.compress(originalFileName, compFileName);
        zipperObj.decompress(compFileName,originalFileName, decompFileName);

    }
}
