package pk.inlab.team.app.mem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import pk.inlab.team.app.mem.databinding.ActivityMainBinding
import pk.inlab.team.app.mem.ui.views.InputDialogFragment

class MainActivity : AppCompatActivity(), InputDialogFragment.InputDialogListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var rootView: View
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        rootView = binding.root
        setContentView(rootView)

        setSupportActionBar(binding.toolbar)

        // In favor of androidx.fragment.app.FragmentContainerView in XML file
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment

        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener {
            showInputDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

    }

    private fun showInputDialog() {
        // Inflate Custom alert dialog view
        val inputAlertDialogView = LayoutInflater.from(this)
            .inflate(R.layout.input_purchase_item, null, false)

        // ContextThemeWrapper(this, R.style.AlertDialogTheme)
        MaterialAlertDialogBuilder(this)
            .setCancelable(false)
            .setTitle(resources.getString(R.string.new_item))
            .setView(inputAlertDialogView)
            .setPositiveButton(resources.getString(R.string.save))
            { /*dialog*/ _ , /*which*/ _ ->
                Snackbar.make(rootView, "Save clicked", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            .setNegativeButton(resources.getString(R.string.cancel))
            {  /*dialog*/ _ , /*which*/ _ ->
                // Just Placeholder
            }
            .show()
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        Log.e("CLICK", "Save")
        dialog.view?.let {
            Snackbar.make(it, "Save clicked", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Log.e("CLICK", "Cancel")
        dialog.view?.let {
            Snackbar.make(it, "Cancel clicked", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}