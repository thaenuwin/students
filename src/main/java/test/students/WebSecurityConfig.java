/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.students;

import test.students.utils.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.security.crypto.util.EncodingUtils.concatenate;
import static org.springframework.security.crypto.util.EncodingUtils.subArray;


/**
 * @author thaenuwin
 */

@Configuration
@Log4j2
@EnableWebSecurity(debug = false)
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String TAG = WebSecurityConfig.class.getName();
    public static final String DEFAULT_USER="ANONYMOUS";
    public static final String DEFAULT_ROLE="ROLE_ANONYMOUS";

    private final JwtFilter jwtfilter ;
    @Autowired
    private DataSource dataSource;



    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .addFilterBefore(createFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                . authorizeRequests()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                ;
    }


    @Bean
    public PreAuthenticatedAuthenticationProvider preAuthProvider() {
        PreAuthenticatedAuthenticationProvider provider
                = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(userDetailsServiceWrapper());
        return provider;
    }
    @Bean
    public AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper() {

        /**
         AuthenticationUserDetailsService service
         = new UacUserDetailsService();
         return service;
         */
        return new UacUserDetailsService();
    }

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        final String userQuery = "select usr_id_num,login_pwd,login_enable "
                + "from usr_data "
                + "where usr_id_num = ?";

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(userQuery)
                .authoritiesByUsernameQuery(
                        "SELECT usr_id_num, 'ROLE_ANONYMOUS' FROM usr_data WHERE usr_id_num=?");

        auth.authenticationProvider(preAuthProvider());
    }





    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AbstractPreAuthenticatedProcessingFilter createFilter() throws Exception {
        AbstractPreAuthenticatedProcessingFilter filter = new UacFilter();
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    public static class UacFilter extends AbstractPreAuthenticatedProcessingFilter {

        public UacFilter() {
            setAuthenticationDetailsSource(new UacAuthenticationSource());
        }

        @Override
        protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
            return "N/A";
        }

        @Override
        protected Object getPreAuthenticatedPrincipal(HttpServletRequest servReq) {

            return "Anonymous";
        }

    }
    public static class UacAuthenticationSource implements
            AuthenticationDetailsSource<HttpServletRequest, Map<String,Object>> {

        @Override
        public Map buildDetails(HttpServletRequest servReq) {

            String reqUri = servReq.getRequestURI();
            String authorization = servReq.getHeader("Authorization");
            log.debug(TAG, "reqUri: " + reqUri);

            if (authorization != null && !authorization.isEmpty()) {

                return TokenUtil.retrieveUserInfo(authorization);
            }

            return null;
        }

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder() {

            @Override
            public String encode(CharSequence rawPassword) {
                byte[] salt = KeyGenerators.secureRandom(64).generateKey();
                return String.valueOf(Hex.encode(enc(rawPassword, salt)));

            }

            byte[] enc(CharSequence rawPassword, byte[] salt) {
                try {

                    PBEKeySpec spec = new PBEKeySpec(rawPassword.toString().toCharArray(),
                            concatenate(salt, rawPassword.toString().getBytes()), 100000, 32);
                    SecretKeyFactory skf = SecretKeyFactory.getInstance(SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256.name());
                    byte[] encoded = concatenate(salt, skf.generateSecret(spec).getEncoded());
                    return encoded;
                } catch (GeneralSecurityException e) {
                    throw new IllegalStateException("Could not create hash", e);
                }
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                byte[] expected = Hex.decode(encodedPassword);
                byte[] salt = subArray(expected, 0, 64);
                return mtch(expected, enc(rawPassword, salt));
            }

            boolean mtch(byte[] expected, byte[] actual) {
                if (expected.length != actual.length) {
                    return false;
                }

                int result = 0;
                for (int i = 0; i < expected.length; i++) {
                    result |= expected[i] ^ actual[i];
                }
                return result == 0;
            }
        };


        return encoder;
    }

    public static class UacUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

        @Override
        public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            String username = DEFAULT_USER;
            Object details = token.getDetails();
            GrantedAuthority auth = new SimpleGrantedAuthority(DEFAULT_ROLE);
            authorities.add(auth);
            User user = new User(username, "", authorities);
            log.debug(TAG, JsonUtil.toJsonString(user));
            return user;
        }

    }
}
