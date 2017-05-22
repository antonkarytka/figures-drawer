import java.io.*;

public class ModuleLoader extends ClassLoader {

    private String pathToModules;
    private String packageName;

    ModuleLoader(String pathToModules, String packageName, ClassLoader parent) {
        super(parent);
        this.pathToModules = pathToModules;
        this.packageName = packageName;
    }

    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
        try {
            byte bytes[] = fetchClass(pathToModules + className + ".class");
            return defineClass(packageName + '.' + className, bytes, 0, bytes.length);
        } catch (Exception | Error ex) {
            return null;
        }
    }

    private byte[] fetchClass(String path) throws IOException {
        try (InputStream is = new FileInputStream(new File(path))) {
            long length = new File(path).length();
            byte[] bytes = new byte[(int) length];
            int offset = 0;
            int numRead;
            while ((offset < bytes.length) && ((numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)) {
                offset += numRead;
            }
            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + path);
            }
            return bytes;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}