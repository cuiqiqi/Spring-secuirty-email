### Spring security

Spring Security是一种基于Spring AOP和Servlet规范中的Filter实现的安全框架。它能够在Web请求级别和方法调用级别处理身份认证和授权。 
Spring Security从两个角度来解决安全性问题。

1、它使用Servlet规范中的Filter保护Web请求并限制URL级别的访问。
2、Spring Security还能够使用Spring AOP保护方法调用——借助于对象代理和使用通知，能够确保只有具备适当权限的用户才能访问安全保护的方法。

@EnableWebSecurity注解将会启动Web安全功能

原理：

​       首先，权限管理离不开登陆验证的，所以要用到登陆验证拦截器**AuthenticationProcessingFilter**； 还有就是对访问的资源管理，所以要用到资源管理拦截器**AbstractSecurityInterceptor**； 但拦截器里面的实现需要一些组件来实现，所以就有了AuthenticationManager、accessDecisionManager等组件来支撑。

大致流程：用户登陆，会被AuthenticationProcessingFilter拦截，调用AuthenticationManager的实现，而且AuthenticationManager会调用ProviderManager来获取用户验证信息（不同的Provider调用的服务不同，因为这些信息可以是在数据库上，可以是在LDAP服务器上，可以是xml配置文件上等），如果验证通过后会将用户的权限信息封装一个User放到spring的全局缓存SecurityContextHolder中，以备后面访问资源时使用。 访问资源（即授权管理），访问url时，会通过AbstractSecurityInterceptor拦截器拦截，其中会调用FilterInvocationSecurityMetadataSource的方法来获取被拦截url所需的全部权限，再调用授权管理器AccessDecisionManager，这个授权管理器会通过spring的全局缓存SecurityContextHolder获取用户的权限信息，还会获取被拦截的url和被拦截url所需的全部权限，然后根据所配的策略（decide()方法内写的授权策略），查看用户的权限，如果权限足够，则返回，权限不够则报错并调用权限不足页面

在Spring Security中，使用用户名密码认证的过程大致如下图所示

UsernamePasswordAuthenticationFilter ---->AuthenticationManger ----->DaoAuthenticationProvider ------>

UserDetailService ------>UserDetails ------>Authentication

 原理：Spring Security使用`UsernamePasswordAuthenticationFilter`过滤器来拦截用户名密码认证请求，将用户名和密码封装成一个`UsernamePasswordToken`对象交给`AuthenticationManager`处理。`AuthenticationManager`将挑出一个支持处理该类型Token的`AuthenticationProvider`（这里为`DaoAuthenticationProvider`，`AuthenticationProvider`的其中一个实现类）来进行认证，认证过程中`DaoAuthenticationProvider`将调用`UserDetailService`的`loadUserByUsername`方法来处理认证，如果认证通过（即`UsernamePasswordToken`中的用户名和密码相符）则返回一个`UserDetails`类型对象，并将认证信息保存到Session中，认证后我们便可以通过`Authentication`对象获取到认证的信息了。

拦截器：

拦截器里面配了三个处理类，**authenticationManager**，这个是处理验证的；**securityMetadataSource**这个用来加载资源与权限的全部对应关系的，并提供一个通过资源获取所有权限的方法。**accessDecisionManager**这个也称为授权器，通过登录用户的权限信息、资源、获取资源所需的权限来根据不同的授权策略来判断用户是否有权限访问资源