import java.io.IOException;

public interface IFileZipper {

    void compress(String originalFileName, String compFileName) throws IOException;

    void decompress(String compFileName,String originalFileName, String decompFileName) throws IOException;
    
}
