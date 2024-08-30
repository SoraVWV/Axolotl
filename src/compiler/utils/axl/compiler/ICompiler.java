package axl.compiler;

import java.util.List;

public interface ICompiler {

    List<IRef> compile(
            IFile target,
            List<IFile> context,
            List<Class<?>> libraries
    );

}

