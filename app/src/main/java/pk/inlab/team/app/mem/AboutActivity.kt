package pk.inlab.team.app.mem

import android.os.Bundle
import com.mikepenz.aboutlibraries.LibsBuilder
import com.mikepenz.aboutlibraries.ui.LibsActivity

class AboutActivity : LibsActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        intent = LibsBuilder()
            .withActivityTitle(getString(R.string.title_about))
            .withAboutAppName(getString(R.string.app_name))
            .withLicenseShown(true)
            .withAboutIconShown(true)
            .withAboutVersionShown(true)
            .withAboutDescription("This is demo app displaying the features of <b>ZXing-Orient</b> library.")
            .withEdgeToEdge(true)
            .withSearchEnabled(true)
            .intent(this)
        super.onCreate(savedInstanceState)
    }
}