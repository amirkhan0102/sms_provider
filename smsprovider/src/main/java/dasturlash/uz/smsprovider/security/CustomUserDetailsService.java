package dasturlash.uz.smsprovider.security;

import dasturlash.uz.smsprovider.entity.ClientEntity;
import dasturlash.uz.smsprovider.entity.ProfileEntity;
import dasturlash.uz.smsprovider.enums.GeneralStatus;
import dasturlash.uz.smsprovider.repository.ClientRepository;
import dasturlash.uz.smsprovider.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ProfileRepository profileRepository;
    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Optional<ProfileEntity> profileOpt = profileRepository.findByLoginAndVisibleTrue(login);
        if (profileOpt.isPresent()) {
            ProfileEntity profile = profileOpt.get();
            return new CustomUserDetails(
                    profile.getId(),
                    profile.getLogin(),
                    profile.getPassword(),
                    profile.getRole().name(),
                    profile.getStatus() == GeneralStatus.ACTIVE
            );
        }

        Optional<ClientEntity> clientOpt = clientRepository.findByLoginAndVisibleTrue(login);
        if (clientOpt.isPresent()) {
            ClientEntity client = clientOpt.get();
            return new CustomUserDetails(
                    client.getId(),
                    client.getLogin(),
                    client.getPassword(),
                    "CLIENT",
                    client.getStatus() == GeneralStatus.ACTIVE
            );
        }

        throw new UsernameNotFoundException("Foydalanuvchi topilmadi: " + login);
    }
}