package com.parking_project.parking.business.domain;

import java.util.Date;

public class SlotReservation {
    private long slotId;
    private Date start;
    private Date end;

    public long getSlotId() {
        return slotId;
    }

    public void setSlotId(long slotId) {
        this.slotId = slotId;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "SlotReservation{" +
                "slotId=" + slotId +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
