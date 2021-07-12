package miu.edu.ea.cs544.ars.repository;

import miu.edu.ea.cs544.ars.domain.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, String> {
}
