import java.io.*;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

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
            if (checkSignature(pathToModules + className + ".class", pathToModules + "sig" + className))
            {
                return defineClass(packageName + '.' + className, bytes, 0, bytes.length);
            } else {
                return null;
            }
        } catch (IOException | Error err) {
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

    private boolean checkSignature(String classFile, String signatureFile) throws IOException {
        FileInputStream signatureInputStream = null;
        try (FileInputStream dataInputStream = new FileInputStream(classFile);
             BufferedInputStream inputBuffer = new BufferedInputStream(dataInputStream)) {
            // Encoded public key
            signatureInputStream = new FileInputStream(signatureFile);
            byte[] encryptionKey = new byte[]{48, -126, 1, -72, 48, -126, 1, 44, 6, 7, 42, -122, 72, -50, 56, 4, 1, 48, -126, 1, 31, 2, -127, -127, 0, -3, 127, 83, -127, 29, 117, 18, 41, 82, -33, 74, -100, 46, -20, -28, -25, -10, 17, -73, 82, 60, -17, 68, 0, -61, 30, 63, -128, -74, 81, 38, 105, 69, 93, 64, 34, 81, -5, 89, 61, -115, 88, -6, -65, -59, -11, -70, 48, -10, -53, -101, 85, 108, -41, -127, 59, -128, 29, 52, 111, -14, 102, 96, -73, 107, -103, 80, -91, -92, -97, -97, -24, 4, 123, 16, 34, -62, 79, -69, -87, -41, -2, -73, -58, 27, -8, 59, 87, -25, -58, -88, -90, 21, 15, 4, -5, -125, -10, -45, -59, 30, -61, 2, 53, 84, 19, 90, 22, -111, 50, -10, 117, -13, -82, 43, 97, -41, 42, -17, -14, 34, 3, 25, -99, -47, 72, 1, -57, 2, 21, 0, -105, 96, 80, -113, 21, 35, 11, -52, -78, -110, -71, -126, -94, -21, -124, 11, -16, 88, 28, -11, 2, -127, -127, 0, -9, -31, -96, -123, -42, -101, 61, -34, -53, -68, -85, 92, 54, -72, 87, -71, 121, -108, -81, -69, -6, 58, -22, -126, -7, 87, 76, 11, 61, 7, -126, 103, 81, 89, 87, -114, -70, -44, 89, 79, -26, 113, 7, 16, -127, -128, -76, 73, 22, 113, 35, -24, 76, 40, 22, 19, -73, -49, 9, 50, -116, -56, -90, -31, 60, 22, 122, -117, 84, 124, -115, 40, -32, -93, -82, 30, 43, -77, -90, 117, -111, 110, -93, 127, 11, -6, 33, 53, 98, -15, -5, 98, 122, 1, 36, 59, -52, -92, -15, -66, -88, 81, -112, -119, -88, -125, -33, -31, 90, -27, -97, 6, -110, -117, 102, 94, -128, 123, 85, 37, 100, 1, 76, 59, -2, -49, 73, 42, 3, -127, -123, 0, 2, -127, -127, 0, -69, -14, 52, -94, 48, 78, -28, 51, -9, -91, -90, -27, -76, -43, 36, -121, -43, 43, -18, 102, -104, 95, 80, 59, -121, -14, -116, -53, 70, 46, -58, -27, -73, 80, 57, 113, -88, -64, 4, 37, -89, 65, 79, -107, 31, -69, -27, -74, 54, -105, 24, -127, -28, 13, 88, 120, 127, 112, -105, -63, -108, 90, 30, -96, -19, 68, 11, 87, -46, 125, 61, 52, 99, 107, -120, -9, 2, 38, 4, -26, 66, -44, 85, 34, -20, 7, 1, 12, 49, 72, -31, 106, 113, -65, 36, 31, -4, -110, -120, 90, 81, 122, -47, 84, 37, 58, 109, 56, -44, 44, 86, 77, -25, -78, -58, -36, -39, -72, 62, 93, -91, -96, 62, 30, 108, 4, 64, -73};

            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encryptionKey);
            KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            // Input the signature bytes
            byte[] signatureToVerify = new byte[signatureInputStream.available()];
            signatureInputStream.read(signatureToVerify);

            // Create a Signature object and initialize it with the public key
            Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
            signature.initVerify(publicKey);

            // Update and verify the data
            byte[] buffer = new byte[1024];
            int len;
            while (inputBuffer.available() != 0) {
                len = inputBuffer.read(buffer);
                signature.update(buffer, 0, len);
            }
            signatureInputStream.close();
            return signature.verify(signatureToVerify);
        } catch (Exception e) {
            if (signatureInputStream != null) {
                signatureInputStream.close();
            }
        }
        System.err.println("Verification error");
        return false;
    }
}