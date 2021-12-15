package pk.inlab.team.app.mem.ui.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pk.inlab.team.app.mem.repository.CurrentMonthPurchaseRepository

class CurrentViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CurrentMonthPurchaseRepository::class.java)
            .newInstance(CurrentMonthPurchaseRepository())
    }

}