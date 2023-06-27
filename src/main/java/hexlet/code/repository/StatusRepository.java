package hexlet.code.repository;

import hexlet.code.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Long> {

    List<Status> findAllByOrderByIdAsc();

    Optional<Status> findTaskStatusById(long id);
}
