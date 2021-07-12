package miu.edu.ea.cs544.ars.repository;

import miu.edu.ea.cs544.ars.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface SessionRepositoryInterface extends JpaRepository<Session, Integer> {

//	@Query(value = "select distinct s FROM sessions s WHERE s.person.id =?1")
//    List<Session> findByProviderId(Integer id);


    @Query(value = "select s FROM sessions s WHERE s.id=?1 AND s.person.id =?2")
    Session findByProviderIdAndSessionID(int id, int Provider_id);


    @Query("SELECT s FROM sessions s WHERE s.person.id = ?1")
    List<Session> findByProviderId(int providerId);
}
