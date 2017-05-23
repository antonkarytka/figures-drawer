import java.io.*;
import java.security.*;

class GenerateSignature
{

    public static void main(String[] args)
    {
        String pathToDir = "./out/production/figures-drawer/shapes";
        File dir = new File(pathToDir);
        File[] modules = dir.listFiles();
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");

            keyGen.initialize(1024, random);

            KeyPair pair = keyGen.generateKeyPair();
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();


            // Create a Signature object and initialize it with the private key
            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(privateKey);
            byte[] key = publicKey.getEncoded();
            for (byte keyElement : key) {
                System.out.print(keyElement + ", ");
            }
            if (modules != null) {
                for (File module : modules) {
                    if (module.getName().contains(".class")) {
                        // Update and sign the data
                        FileInputStream inputStream = new FileInputStream(module);
                        BufferedInputStream inputBuffer = new BufferedInputStream(inputStream);
                        byte[] buffer = new byte[1024];
                        int len;
                        while (inputBuffer.available() != 0) {
                            len = inputBuffer.read(buffer);
                            dsa.update(buffer, 0, len);
                        }
                        inputBuffer.close();

                        /* Now that all the data to be signed has been read in,
                        generate a signature for it */
                        byte[] signature = dsa.sign();
                        System.out.println("\n" + module.getParent() + "/sig" + module.getName().split(".class")[0]);

                        // Save the signature in a file
                        FileOutputStream signatureFile = new FileOutputStream(module.getParent() + "/sig" + module.getName().split(".class")[0]);
                        signatureFile.write(signature);
                        signatureFile.close();


                        /* Save the public key in a file */
                        /*byte[] key = publicKey.getEncoded();
                        FileOutputStream keyOutputStream = new FileOutputStream(module.getParent() + "/publicKey" + module.getName().split(".class")[0]);
                        keyOutputStream.write(key);
                        keyOutputStream.close();
                        byte[] encodedPrivateKey = privateKey.getEncoded();
                        FileOutputStream encodedPrivateKeyOutputStream = new FileOutputStream(module.getParent() + "/privateKey" + module.getName().split(".class")[0]);
                        encodedPrivateKeyOutputStream.write(encodedPrivateKey);
                        encodedPrivateKeyOutputStream.close();*/
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
 
