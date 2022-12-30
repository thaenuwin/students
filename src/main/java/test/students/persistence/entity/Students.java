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

    private static final long serialVersionUID = 1L;


    @EmbeddedId
    protected StudentPk studentPk;

    @Size(max = 255)
    @Column(name = "stu_ph_num")
    private String stuPhoneNumber;

    @Size(max = 255)
    @Column(name = "stu_enbl")
    private int studentEnable;

    @Basic(optional = false)
    @NotNull
    @Column(name = "crt_dttm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "upd_dttm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;


}
