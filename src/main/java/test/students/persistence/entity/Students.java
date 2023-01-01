package test.students.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Table(name = "student_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder(toBuilder = true)
public class Students implements Serializable {



    @EmbeddedId
    public StudentPk studentPk;


    @Size(max = 255)
    @Column(name = "stu_ph_num")
    public String stuPhoneNumber;


    @Column(name = "stu_enbl")
    public int studentEnable;

    @Basic(optional = false)
    @NotNull
    @Column(name = "crt_dttm")
    @Temporal(TemporalType.TIMESTAMP)
    public Date createdDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "upd_dttm")
    @Temporal(TemporalType.TIMESTAMP)
    public Date updatedDate;


}
