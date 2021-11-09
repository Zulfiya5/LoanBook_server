package uz.pdp.loanbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.loanbook.entity.role.RoleDatabase;
import uz.pdp.loanbook.entity.role.RoleName;

public interface RoleRepository extends JpaRepository<RoleDatabase, Integer> {

    RoleDatabase findByRoleName(RoleName roleName);
}
