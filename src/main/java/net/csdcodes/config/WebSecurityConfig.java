package net.csdcodes.config;


import net.csdcodes.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter{

    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

   @Override
   @Bean
    protected UserDetailsService userDetailsService(){
       return new UserDetailServiceImpl();
/*       UserDetails user1 = User
               .withUsername("qiu")
               .password("$2a$10$BgucD7w3XisoVje2qLnky.MKbLC2gCogD.o7QoR6Zc3o59oLiQfc2")
               .roles("USER")
               .build();
       UserDetails user2 = User
               .withUsername("admin")
               .password("$2a$10$BgucD7w3XisoVje2qLnky.MKbLC2gCogD.o7QoR6Zc3o59oLiQfc2")
               .roles("USER")
               .build();
       return new InMemoryUserDetailsManager(user1,user2);*/
   }



/*    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }*/

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("select username, password, enable from [user] where username=?")
                .authoritiesByUsernameQuery("select u.username,r.name from" +
                        " user_role ur left join role r on ur.role_id=r.role_id" +
                        " left join [user] u  on ur.user_id=u.user_id" +
                        " where u.username=?")
        ;
    }

   @Override
    protected void configure(HttpSecurity http) throws Exception{

       http.csrf().disable()
               .authorizeRequests()
               .antMatchers("/","/favicon.ico","/calendar","/webjars/**","/img/**","/","/it-asset/**","/api/**/**/**","/css/**/**","/js/**/**").permitAll()
               .anyRequest().authenticated()
               .and()
               .formLogin()
                   .loginPage("/login")
                   .loginProcessingUrl("/login")
/*                    .defaultSuccessUrl("/it-asset/index",true)*/
                   .permitAll()
               .and()
               .logout()
                    .logoutSuccessUrl("/")
                    .permitAll();

   }

}
