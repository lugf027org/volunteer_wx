package cn.wx.ycloudtech.config;


import cn.wx.ycloudtech.auth.TokenAuthenticationProvider;
import cn.wx.ycloudtech.auth.filter.CorsFilter;
import cn.wx.ycloudtech.auth.filter.TokenAuthenticationFilter;
import cn.wx.ycloudtech.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private TokenService tokenService;

    @Configuration
    @Order(1)
    class PredictorSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(new TokenAuthenticationProvider(tokenService));
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .addFilterBefore(new CorsFilter(), UsernamePasswordAuthenticationFilter.class)
                    .addFilterAfter(new TokenAuthenticationFilter(), BasicAuthenticationFilter.class)
//                    .addFilterAfter(new BodyReaderFilter(),BasicAuthenticationFilter.class)
//                    .addFilterAfter(new ResultExceptionTranslationFilter(), ExceptionTranslationFilter.class)
                    .authorizeRequests()

//                    ????????????????????????
//                    .antMatchers("/**").permitAll()

                    // ??????????????????
                    .antMatchers("/user/login", "/admin/login", "/resources/**", "/user/**").permitAll()
                    .antMatchers("/user/getParamValueByName").permitAll()  // 2021.04.18 ?????????????????????
                    .antMatchers("/admin/**", "/admin/**/**").hasRole("ADMIN")
//                    .antMatchers("/user/**", "/user/**/**").hasRole("USER")
                    .antMatchers("/visitor/**", "/visitor/**").hasRole("VISITOR")
                    .and()
                    .csrf()
                    .disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }
}
