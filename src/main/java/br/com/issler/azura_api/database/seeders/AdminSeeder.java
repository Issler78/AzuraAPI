package br.com.issler.azura_api.database.seeders;

import br.com.issler.azura_api.database.models.RoleEntity;
import br.com.issler.azura_api.database.models.UserEntity;
import br.com.issler.azura_api.database.repositories.IRoleRepository;
import br.com.issler.azura_api.database.repositories.IUserRepository;
import br.com.issler.azura_api.enums.RoleTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Order(2)
public class AdminSeeder implements CommandLineRunner {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.application.admin-name}")
    private String adminName;

    @Value("${spring.application.admin-email}")
    private String adminEmail;

    @Value("${spring.application.admin-password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0 && userRepository.findByEmail(adminEmail).isEmpty()) {
            RoleEntity role = roleRepository.findByName(RoleTypeEnum.ROLE_ADMIN.name())
                    .orElseThrow(() -> new Exception("Role not found"));

            userRepository.save(UserEntity.builder()
                    .name(adminName)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .cpf("00000000000")
                    .roles(new HashSet<RoleEntity>(Set.of(role)))
                    .build());
        }
    }
}
