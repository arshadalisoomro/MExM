package pk.inlab.team.app.mem.ui.settings

import android.os.Bundle
import android.text.InputType
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import pk.inlab.team.app.mem.R

class SettingsFragment : PreferenceFragmentCompat() {

    companion object{
        const val KEY_RATE_PER_KILO = "rate_per_kilo"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val currentRatePerKiloEditTextPreference : EditTextPreference? = findPreference(
            KEY_RATE_PER_KILO)

        currentRatePerKiloEditTextPreference?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        }

    }
}