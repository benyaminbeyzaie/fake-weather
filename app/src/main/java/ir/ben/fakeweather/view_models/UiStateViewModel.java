package ir.ben.fakeweather.view_models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ir.ben.fakeweather.enums.SelectedPage;

public class UiStateViewModel extends ViewModel {
    private MutableLiveData<SelectedPage> selectedPageMutableLiveData;
    private MutableLiveData<SelectedPage> lastSelectedPageMutableLiveData;

    public LiveData<SelectedPage> getSelectedPage() {
        if (selectedPageMutableLiveData == null) {
            selectedPageMutableLiveData = new MutableLiveData<>();
        }
        return selectedPageMutableLiveData;
    }

    public void changePage(SelectedPage selectedPage) {
        selectedPageMutableLiveData.postValue(selectedPage);
    }

    public void changeLastPage(SelectedPage selectedPage){
        lastSelectedPageMutableLiveData.postValue(selectedPage);
    }

    public LiveData<SelectedPage> getLastSelectedPage(){
        if (lastSelectedPageMutableLiveData == null) {
            lastSelectedPageMutableLiveData = new MutableLiveData<>();
        }
        return lastSelectedPageMutableLiveData;
    }

}


