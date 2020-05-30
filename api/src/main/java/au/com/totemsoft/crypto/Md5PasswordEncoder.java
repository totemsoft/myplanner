package au.com.totemsoft.crypto;

import java.security.MessageDigest;

import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Md5PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return Digest.digest(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        byte[] expectedBytes = bytesUtf8(encodedPassword);
        byte[] actualBytes = bytesUtf8(Digest.digest(rawPassword.toString()));
        return MessageDigest.isEqual(expectedBytes, actualBytes);
    }

    private static byte[] bytesUtf8(String s) {
        if (s == null) {
            return null;
        }
        return Utf8.encode(s); // need to check if Utf8.encode() runs in constant time (probably not). This may leak length of string.
    }

}
