package miu.edu.ea.cs544.ars.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Entity(name = "appointments")
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @Temporal(TemporalType.DATE)
    private Date modifiedDate;


    @Temporal(TemporalType.DATE)
    private Date appointmentDate;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    public Appointment(Date creationDate,
                       Date appointmentDate,
                       Person person,
                       Session session,
                       AppointmentStatus status) {
        this.creationDate = creationDate;
        this.appointmentDate = appointmentDate;
        this.person = person;
        this.session = session;
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {

        this.creationDate = creationDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.appointmentDate = dateFormatter.parse(appointmentDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getAppointmentDateText() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        return df.format(appointmentDate);
    }


}