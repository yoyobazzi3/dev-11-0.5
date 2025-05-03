package s25.cs151.application.controller;

import javafx.collections.ObservableList;
import s25.cs151.application.model.DataStore;
import s25.cs151.application.model.ScheduledOfficeHour;

public class ScheduleController {
    private final ObservableList<ScheduledOfficeHour> list = DataStore.loadSchedule();

    public ObservableList<ScheduledOfficeHour> getEntries() { return list; }

    public boolean add(ScheduledOfficeHour o) {
        boolean conflict = list.stream().anyMatch(e ->
                e.date().equals(o.date()) &&
                        e.time().equals(o.time()) &&
                        e.course().equals(o.course()));
        if (!conflict) list.add(o);
        return !conflict;
    }

    public void replace(ScheduledOfficeHour old, ScheduledOfficeHour updated) {
        list.remove(old);
        list.add(updated);
    }

    public void remove(ScheduledOfficeHour o) { list.remove(o); }

    public void save() { DataStore.saveSchedule(list); }
}
