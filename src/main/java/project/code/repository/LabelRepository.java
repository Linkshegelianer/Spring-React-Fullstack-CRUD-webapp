package project.code.repository;

import project.code.domain.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {

    List<Label> findAllByOrderByIdAsc();
    Optional<Label> findLabelById(long id);
}
