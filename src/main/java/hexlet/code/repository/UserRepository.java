package hexlet.code.repository;

import hexlet.code.model.User;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByOrderByIdAsc();
    Optional<User> findUserById(long id);
    Optional<User> findByEmail(String email);
    Optional<User> findUserByLastName(String lastName);
    boolean existsUserByEmailIgnoreCase(String email);

}

