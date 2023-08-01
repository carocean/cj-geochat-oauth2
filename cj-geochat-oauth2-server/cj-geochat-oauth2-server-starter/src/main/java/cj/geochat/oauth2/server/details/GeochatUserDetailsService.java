package cj.geochat.oauth2.server.details;

import cj.geochat.ability.oauth.server.user.details.GeochatUser;
import cj.geochat.oauth2.server.remote.UserDetailsRemote;
import cj.geochat.uc.middle.LoginAccountCategory;
import cj.geochat.uc.middle.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class GeochatUserDetailsService implements UserDetailsService {
    @Autowired
    UserDetailsRemote userDetailsRemote;

    Pattern phone = Pattern.compile("1[3-9]\\d{9}");
    Pattern email = Pattern.compile("\\w{1,30}@[a-zA-Z0-9]{2,20}(\\.[a-zA-Z0-9]{2,20}){1,2}");

    @Override
    public UserDetails loadUserByUsername(String account/*此处的参数是登录账号，不一定是用户标识*/) throws UsernameNotFoundException {
        LoginAccountCategory category = parseCategory(account);
        cj.geochat.uc.middle.UserDetails userDetails = userDetailsRemote.loadUserDetailsByAccount(category, account);
        if (userDetails == null) {
            throw new UsernameNotFoundException(account);
        }
        return convertUserDetails(String.format("%s/%s", category.name(),account), userDetails);
    }

    private UserDetails convertUserDetails(String accountFullName, cj.geochat.uc.middle.UserDetails userDetails) {
        var isEnabled = userDetails.getStatus() == UserStatus.normal ? true : false;
        return GeochatUser
                .withUserAndAccount(userDetails.getId(), accountFullName)
                .password(userDetails.getPassword())
                .disabled(!isEnabled)
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(false)
                .roles(userDetails.getRoles().toArray(new String[0]))
                .build();
    }

    private LoginAccountCategory parseCategory(String username) {
        if (phone.matcher(username).matches()) {
            return LoginAccountCategory.phone;
        }
        if (email.matcher(username).matches()) {
            return LoginAccountCategory.email;
        }
        return LoginAccountCategory.geochat;
    }
}
