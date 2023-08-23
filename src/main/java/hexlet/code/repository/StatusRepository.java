package hexlet.code.repository;

import hexlet.code.domain.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    List<Status> findAllByOrderByIdAsc();
    Optional<Status> findTaskStatusById(long id);
}
