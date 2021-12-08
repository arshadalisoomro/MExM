package pk.inlab.team.app.mem.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import pk.inlab.team.app.mem.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}