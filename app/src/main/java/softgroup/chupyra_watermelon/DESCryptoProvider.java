package softgroup.chupyra_watermelon;

import android.content.Context;

import java.security.*;
import javax.crypto.*;

public class DESCryptoProvider {
    private Context context;
    private  String hex;
    public  String seed;

    public DESCryptoProvider(Context current){
        this.context = current;
        this.seed = current.getResources().getString(R.string.seed);
        this.hex = current.getResources().getString(R.string.hex);
    }

    public  String decrypt(String src)
    {
        try{
            javax.crypto.spec.SecretKeySpec
                    key = new  javax.crypto.spec.SecretKeySpec(getRawKey(), context.getResources().getString(R.string.cryptographicAlgorithm));
            Cipher ecipher = Cipher.getInstance(context.getResources().getString(R.string.cryptographicAlgorithm));
            ecipher.init(Cipher.DECRYPT_MODE, key);

            byte[] utf8 = toByte(src);

            // Descrypt
            byte[] dec= ecipher.doFinal(utf8);

            return new String( dec  );
        }
        catch(Exception exc )
        {
            try{
                exc.printStackTrace();
            }catch(Exception exc2){}

        }
        return src;

    }

    public  String encrypt(String src)
    {
        try{
            javax.crypto.spec.SecretKeySpec
                    key = new  javax.crypto.spec.SecretKeySpec(getRawKey(), context.getResources().getString(R.string.cryptographicAlgorithm));
            Cipher ecipher = Cipher.getInstance(context.getResources().getString(R.string.cryptographicAlgorithm));
            ecipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] utf8 = src.getBytes(context.getResources().getString(R.string.UTF8));

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            return toHex(enc);
        }
        catch(Exception exc )
        {
            try{
                exc.printStackTrace();
            }catch(Exception exc2){}

        }
        return src;
    }

    private  byte[] getRawKey() throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(context.getResources().getString(R.string.cryptographicAlgorithm));
        SecureRandom sr = SecureRandom.getInstance(context.getResources().getString(R.string.sha));
        sr.setSeed(seed.getBytes());
        kgen.init(56, sr);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    // эти методы используются для конвертации байтов в ASCII символы

    public  String toHex(String txt) {
        return toHex(txt.getBytes());
    }
    public  String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        return result;
    }

    public  String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2*buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private  void appendHex(StringBuffer sb, byte b) {
        sb.append(hex.charAt((b>>4)&0x0f)).append(hex.charAt(b&0x0f));
    }

}
