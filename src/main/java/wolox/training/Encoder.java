package wolox.training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import wolox.training.security.PasswordEncoderBCrypt;

public class Encoder implements PasswordEncoder {

    @Autowired
    private PasswordEncoderBCrypt passwordEncoderBCrypt;

    private PasswordEncoder encoder() {
        return this.passwordEncoderBCrypt.encoder();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return encoder().encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encoder().matches(rawPassword, encodedPassword);
    }
}
