package com.glebalekseevjk.premierleaguefixtures.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.glebalekseevjk.premierleaguefixtures.R
import com.glebalekseevjk.premierleaguefixtures.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fcv) as NavHostFragment
        navController = navHostFragment.navController
        val onDestinationChangedListener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
            title = when (destination.id) {
                R.id.listMatchesFragment -> resources.getString(R.string.app_name)
                R.id.matchDetailFragment -> "PLF Match"
                else -> ""
            }
            controller.currentDestination?.label = title
        }
        navController.addOnDestinationChangedListener(onDestinationChangedListener)

        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)



    }
}