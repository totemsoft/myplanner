package au.com.totemsoft.crypto;

import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BCryptPasswordEncoderTest {

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11);

    private static String encodedPassword;

    @Test
    public void encode() {
        encodedPassword = passwordEncoder.encode("Passw0rd");
        //encodedPassword = "$2a$11$7K1WTfBcFaXewgn.EVttLOc9hrTJ459LZQieo8F3Qioc3YxzQjtzu";
    }

    @Test
    public void matches() {
        assertTrue(passwordEncoder.matches("Passw0rd", encodedPassword));
    }

}
