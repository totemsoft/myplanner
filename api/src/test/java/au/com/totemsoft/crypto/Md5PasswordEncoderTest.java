package au.com.totemsoft.crypto;

import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.security.crypto.password.PasswordEncoder;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Md5PasswordEncoderTest {

    private PasswordEncoder passwordEncoder = new Md5PasswordEncoder();

    private static String encodedPassword;

    @Test
    public void encode() {
        encodedPassword = passwordEncoder.encode("Passw0rd");
        encodedPassword = "D41E98D1EAFA6D6011D3A70F1A5B92F0";
    }

    @Test
    public void matches() {
        assertTrue(passwordEncoder.matches("Passw0rd", encodedPassword));
    }

}
