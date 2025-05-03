package s25.cs151.application.controller;

import javafx.collections.ObservableList;
import s25.cs151.application.model.DataStore;
import s25.cs151.application.model.TimeSlot;

public class TimeSlotController {
    private final ObservableList<TimeSlot> slots = DataStore.loadTimeSlots();

    public ObservableList<TimeSlot> getSlots() { return slots; }

    public boolean addSlot(TimeSlot t) {
        boolean overlap = slots.stream().anyMatch(s -> s.from().equals(t.from())
                || s.to().equals(t.to()));
        if (!overlap) slots.add(t);
        return !overlap;
    }

    public void save() { DataStore.saveTimeSlots(slots); }
}
