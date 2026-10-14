package br.com.issler.azura_api.database.seeders;

import br.com.issler.azura_api.database.models.RoleEntity;
import br.com.issler.azura_api.database.repositories.IRoleRepository;
import br.com.issler.azura_api.enums.RoleTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(1)
public class RoleSeeder implements CommandLineRunner {
    private final IRoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        for(RoleTypeEnum role : RoleTypeEnum.values()){
            if(!roleRepository.existsByName(role.name())){
                RoleEntity roleEntity = new RoleEntity();
                roleEntity.setName(role.name());
                roleRepository.save(roleEntity);
            }
        }
    }
}
