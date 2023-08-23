package hexlet.code.repository;

import hexlet.code.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByOrderByIdAsc();
    Optional<User> findUserById(long id);
    Optional<User> findUserByEmailIgnoreCase(String email);
    Optional<User> findUserByLastName(String lastName);
    boolean existsUserByEmailIgnoreCase(String email);
}
