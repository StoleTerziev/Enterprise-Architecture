package miu.edu.ea.cs544.ars.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(name = "sessions")
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String location;
    private float duration;
    @Temporal(TemporalType.TIME)
    private Date startTime;
    private float price;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "provider")
    private Person person;

    public Session(String location, float duration, String startTime, float price) {
        super();
        this.location = location;
        this.duration = duration;

        SimpleDateFormat startTimeFormatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.startTime = startTimeFormatter.parse(startTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.price = price;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
