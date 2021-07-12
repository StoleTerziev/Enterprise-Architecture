package miu.edu.ea.cs544.ars.repository;

import miu.edu.ea.cs544.ars.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface AppointmentRepositoryInterface extends JpaRepository<Appointment, Integer> {
	
	@Query("SELECT a FROM appointments a WHERE a.session.id = ?1")
	List<Appointment> findBySessionId(int sessionId);
	

	@Query(value = "select a FROM appointments a WHERE a.session.id=?1 AND a.session.person.id =?2")
	List<Appointment> findBySessionIDAndProviderId(int id,int provider_id);
	
	@Query(value="SELECT ap from appointments ap WHERE ap.person.id = :id")
	List<Appointment> findAllAppointmentByPersonId(Integer id);

	@Query(value="SELECT ap from appointments ap WHERE ap.session.person.id = :id")
	List<Appointment> findAllAppointmentByProviderId(Integer id);
	
	@Query(value="SELECT ap from appointments ap WHERE ap.person.id = ?1 and ap.session.id = ?2 and ap.appointmentDate = ?3")
	List<Appointment> findCustomerAppointmentBySession(Integer customerId, Integer sessionId, Date date);
}
