package axl.compiler;

import java.util.List;

// TODO
public interface IRef {

    String getName();

    String getSuperName();

    List<String> getInterfaces();

    byte[] getBuffer();

    byte[] getMetadata();

}
