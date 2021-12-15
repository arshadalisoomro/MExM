package pk.inlab.team.app.mem

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pk.inlab.team.app.mem.databinding.ActivityMainBinding
import pk.inlab.team.app.mem.databinding.InputPurchaseItemBinding
import pk.inlab.team.app.mem.model.PurchaseItem
import pk.inlab.team.app.mem.ui.current.CurrentMonthFragmentDirections
import pk.inlab.team.app.mem.ui.current.CurrentMonthViewModel
import pk.inlab.team.app.mem.ui.current.CurrentViewModelFactory
import pk.inlab.team.app.mem.ui.settings.SettingsFragment.Companion.KEY_RATE_PER_KILO
import pk.inlab.team.app.mem.utils.DateUtils
import pk.inlab.team.app.mem.utils.State
import pk.inlab.team.app.mem.utils.liveprefs.LiveSharedPreferences
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var rootView: View
    private lateinit var navController: NavController

    private lateinit var preferences: SharedPreferences
    private lateinit var liveSharedPreferences: LiveSharedPreferences

    // Define View Model
    private lateinit var currentMonthViewModel: CurrentMonthViewModel

    // Coroutine Scope
    private val uiScope = CoroutineScope(Dispatchers.Main)

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

        navController.addOnDestinationChangedListener { /*controller*/ _, destination, /*arguments*/ _ ->
            run {
                val currentFragment = destination.id

                if (currentFragment == R.id.nav_current) {
                    // Show Fab on Current Month fragment
                    binding.fab.visibility = View.VISIBLE
                }

                if (currentFragment == R.id.nav_history) {
                    // Hide Fab on History fragment
                    binding.fab.visibility = View.GONE
                }

                if (currentFragment == R.id.nav_settings) {
                    // Hide Fab on Settings fragment
                    binding.fab.visibility = View.GONE
                }

            }
        }

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener {
            showInputDialog()
        }

        // Init Prefs Live Change is observed
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        liveSharedPreferences = LiveSharedPreferences(preferences)

        // Init Current Month ViewModel to fetch data from Collection
        currentMonthViewModel = ViewModelProvider(this, CurrentViewModelFactory())
            .get(CurrentMonthViewModel::class.java)

        // Set Current Month and Year
        binding.mtvMilkExpenseMonthYear.text = DateUtils.getToday()

    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        // Launch Coroutine
        uiScope.launch {
            loadItems(getCurrentRateFromPrefs())
        }
    }

    private fun getCurrentRateFromPrefs(): Int {
        // Init Current rate
        var currentRatePerKilo = 0

        // enter the key from your xml and the default value
        liveSharedPreferences
            .getString(KEY_RATE_PER_KILO, "-1")
            .observe(this, {
                currentRatePerKilo = it?.toInt()!!
                // Launch Coroutine
                uiScope.launch {
                    loadItems(currentRatePerKilo)
                }
            })
        return currentRatePerKilo

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
            R.id.action_history -> {
                findNavController(R.id.nav_host_fragment_content_main)
                    .safeNavigate(CurrentMonthFragmentDirections.actionCurrentFragmentToHistoryFragment())

                return true
            }
            R.id.action_settings -> {
                findNavController(R.id.nav_host_fragment_content_main)
                    .safeNavigate(CurrentMonthFragmentDirections.actionCurrentFragmentToSettingsFragment())
                return true
            }
            R.id.action_about -> {
                startActivity(Intent(this@MainActivity, AboutActivity::class.java))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun NavController.safeNavigate(direction: NavDirections) {
        currentDestination?.getAction(direction.actionId)?.run {
            navigate(direction)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private suspend fun loadItems(currentRatePerKilo: Int) {
        currentMonthViewModel.getAllItemsRealTime().collect {
            when(it){
                is State.Loading -> {
                    // showToast(rootView, "Loading")
                }
                is State.Success -> {
                    var totalMonthWeightInPaos = 0
                    for (item in it.data){
                        totalMonthWeightInPaos += item.purchaseWeight
                    }

                    if (currentRatePerKilo >= 0){
                        // Set value of Paos as Int
                        binding.mtvMilkMonthTotalPaos.text = getString(R.string.total_month_paos, totalMonthWeightInPaos)

                        val totalMonthWeightInKgs = (totalMonthWeightInPaos/4.0)
                        val totalMonthExpenseValue = totalMonthWeightInKgs * currentRatePerKilo
                        // Set Values to views
                        binding.mtvMilkTotalMonthExpense.text = getString(R.string.total_month_expense, totalMonthExpenseValue.toInt())

                        binding.mtvMilkMonthTotalKilos.text = getString(R.string.total_month_kgs, totalMonthWeightInKgs)
                    }
                }
                is State.Failed -> {
                    // showToast(rootView, "Failed!")
                }
            }
        }

    }


    private fun showInputDialog() {
        // Through bindings
        val binding = InputPurchaseItemBinding.inflate(LayoutInflater.from(this))

        // Get View from binding
        val inputAlertDialogView = binding.root

        MaterialAlertDialogBuilder(this)
            .setCancelable(false)
            .setTitle(resources.getString(R.string.new_item))
            .setView(inputAlertDialogView)
            .setPositiveButton(resources.getString(R.string.save))
            { /*dialog*/ _ , /*which*/ _ ->
                uiScope.launch {
                    currentMonthViewModel.addNewItem(
                        PurchaseItem(
                            UUID.randomUUID().toString(),
                            Date().time,
                            binding.tilDialogItemWeightInPaos.text.toString().toInt(),
                            binding.tilDialogItemDescription.text.toString(),
                        )
                    ).collect{
                        when(it){
                            is State.Loading -> {

                            }
                            is State.Success -> {
                                Snackbar.make(rootView, "New Item Saved", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show()
                            }
                            is State.Failed -> {

                            }
                        }
                    }
                }

            }
            .setNegativeButton(resources.getString(R.string.cancel))
            {  /*dialog*/ _ , /*which*/ _ ->
                // Just Placeholder
            }
            .show()
    }

}