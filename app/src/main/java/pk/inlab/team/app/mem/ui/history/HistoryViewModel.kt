package pk.inlab.team.app.mem.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is reflow Fragment"
    }
    val text: LiveData<String> = _text

    private val _texts = MutableLiveData<List<String>>().apply {
        value = (1..60).mapIndexed { _, i ->
            "Item # $i"
        }
    }

    val texts: LiveData<List<String>> = _texts
}