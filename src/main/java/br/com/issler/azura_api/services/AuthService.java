package br.com.issler.azura_api.services;

import br.com.issler.azura_api.config.TokenProvider;
import br.com.issler.azura_api.database.models.RoleEntity;
import br.com.issler.azura_api.database.models.UserEntity;
import br.com.issler.azura_api.database.repositories.IRoleRepository;
import br.com.issler.azura_api.database.repositories.IUserRepository;
import br.com.issler.azura_api.dtos.LoginDTO;
import br.com.issler.azura_api.dtos.RegisterDTO;
import br.com.issler.azura_api.enums.RoleTypeEnum;
import br.com.issler.azura_api.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public void register(RegisterDTO registerDTO) throws Exception {
        UserEntity user = userRepository.findByEmailOrCpf(registerDTO.email(), registerDTO.cpf()).orElse(null);

        if (user != null){
            throw new BadRequestException("CPF or E-mail already exists");
        }

        RoleEntity role = roleRepository.findByName(RoleTypeEnum.ROLE_STUDENT.name())
                .orElseThrow(() -> new Exception("Role not found"));

        try {
            userRepository.save(UserEntity.builder()
                    .name(registerDTO.name())
                    .email(registerDTO.email())
                    .password(passwordEncoder.encode(registerDTO.password()))
                    .cpf(registerDTO.cpf())
                    .roles(new HashSet<RoleEntity>(Set.of(role)))
                    .build());
        } catch (Exception e) {
            throw new Exception("Error occurred while saving user on database");
        }
    }

    public String login(LoginDTO loginDTO) throws Exception {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.email(), loginDTO.password()
                    )
            );

            return tokenProvider.generateToken(auth);
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Email or password incorrect");
        } catch (Exception e) {
            throw new Exception("Error occurred while login: " + e.getMessage());
        }
    }
}
