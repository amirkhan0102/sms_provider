package dasturlash.uz.smsprovider.service;

import dasturlash.uz.smsprovider.dto.request.AdminLoginRequest;
import dasturlash.uz.smsprovider.dto.response.AdminLoginResponse;
import dasturlash.uz.smsprovider.entity.ProfileEntity;
import dasturlash.uz.smsprovider.enums.GeneralStatus;

import dasturlash.uz.smsprovider.exceptions.AppException;
import dasturlash.uz.smsprovider.repository.ProfileRepository;
import dasturlash.uz.smsprovider.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AdminLoginResponse login(AdminLoginRequest request) {


        ProfileEntity profile = profileRepository
                .findByLoginAndVisibleTrue(request.getLogin())
                .orElseThrow(() -> new AppException("Login yoki parol noto'g'ri",
                        HttpStatus.UNAUTHORIZED.value()));


        if (profile.getStatus() == GeneralStatus.BLOCKED) {
            throw new AppException("Akkaunt bloklangan",
                    HttpStatus.FORBIDDEN.value());
        }

        if (!passwordEncoder.matches(request.getPassword(), profile.getPassword())) {
            throw new AppException("Login yoki parol noto'g'ri",
                    HttpStatus.UNAUTHORIZED.value());
        }


        String token = jwtUtil.generateToken(
                profile.getId(),
                profile.getLogin(),
                profile.getRole().name());

        return AdminLoginResponse.builder()
                .id(profile.getId())
                .name(profile.getName())
                .surname(profile.getSurname())
                .login(profile.getLogin())
                .role(profile.getRole().name())
                .token(token)
                .build();
    }
}