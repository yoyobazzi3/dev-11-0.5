package s25.cs151.application.controller;

import s25.cs151.application.model.DataStore;
import s25.cs151.application.model.SemesterSettings;

import java.util.Optional;

public class SemesterController {

    private SemesterSettings settings =
            DataStore.loadSemester().orElse(null);

    public Optional<SemesterSettings> getSettings() {
        return Optional.ofNullable(settings);
    }

    public void save(SemesterSettings s) {
        settings = s;
        DataStore.saveSemester(s);
    }
}
