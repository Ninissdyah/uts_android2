package com.example.uts_android2;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class InputViewModel extends AndroidViewModel {

    private InputRepository repository;
    private LiveData<List<ModelInput>> allData;

    public InputViewModel(@NonNull Application application) {
        super(application);

        repository = new InputRepository(application);
        allData = repository.getAllData();
    }

    public void insert(ModelInput modelInput) {
        repository.insert(modelInput);
    }

    public void update(ModelInput modelInput) {
        repository.update(modelInput);
    }

    public void delete(ModelInput modelInput) {
        repository.delete(modelInput);
    }

    public void deleteAllData(ModelInput modelInput) {
        repository.deleteAllNotes();
    }

    public LiveData<List<ModelInput>> getAllData() {
        return allData;
    }
}
