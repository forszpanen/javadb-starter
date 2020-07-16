package org.example.jpa.starter.relations;

import javax.persistence.*;

@Entity
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seatId;
    private String columnNumber;
    private Integer rowNumber;
    private Integer seatNumber;

    @OneToOne(mappedBy = "seat")
    private StudentEntity student;

    public SeatEntity(String columnNumber, Integer rowNumber, Integer seatNumber) {
        this.columnNumber = columnNumber;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
    }

    private SeatEntity() {
    }

    public StudentEntity getStudent() {
        return student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }

    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

    public String getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(String columnNumber) {
        this.columnNumber = columnNumber;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    @Override
    public String toString() {
        return "SeatEntity{" +
                "columnNumber='" + columnNumber + '\'' +
                ", rowNumber=" + rowNumber +
                ", seatNumber=" + seatNumber +
                '}';
    }

}