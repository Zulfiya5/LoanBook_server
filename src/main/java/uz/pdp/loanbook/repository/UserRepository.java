package uz.pdp.loanbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.loanbook.entity.user.UserDatabase;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDatabase, Integer> {
    boolean existsByUsername(String username);
    boolean existsByUsernameAndIdNot(String username, Integer id);

    @Override
    Optional<UserDatabase> findById(Integer integer);

    Optional<UserDatabase> findByUsername(String username);
}
