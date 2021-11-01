package com.example.tipjar.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.tipjar.R
import com.example.tipjar.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * View activity that handles navigation and setting toolbar views/status
 *
 * When AddTipFragment is visible - show logo and history menu, hide saved payments title, appbar elevation is 0
 * When ViewTipsFragment is visible - show saved payments title, hide show logo and history menu, appbar elevation is 8px
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private lateinit var appBarConfig: AppBarConfiguration
  private var isBackButtonVisible = false
  private var isMenuVisible = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)

    // Setup navigation for the two fragments
    val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    navHost.navController.apply {
      appBarConfig = AppBarConfiguration(graph)

      addOnDestinationChangedListener { _, destination, _ ->
        isBackButtonVisible = when (destination.id) {
          R.id.addTipFragment -> {
            binding.tipjarLogoImageView.visibility = View.VISIBLE
            binding.titleTextView.visibility = View.GONE
            isMenuVisible = true
            invalidateOptionsMenu()
            binding.appbar.elevation = 0f
            false
          }
          R.id.viewTipsFragment -> {
            binding.tipjarLogoImageView.visibility = View.GONE
            binding.titleTextView.apply {
              visibility = View.VISIBLE
              text = resources.getString(R.string.saved_payments)
            }
            isMenuVisible = false
            invalidateOptionsMenu()
            binding.appbar.elevation = 8.0f
            true
          }
          else -> false
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(isBackButtonVisible)
      }
    }

    setSupportActionBar(binding.toolbar)

    if (savedInstanceState != null) {
      isBackButtonVisible = savedInstanceState.getBoolean(BACK_BUTTON_STATE)
      supportActionBar?.setDisplayHomeAsUpEnabled(isBackButtonVisible)
    }

    supportActionBar?.run {
      title = null
      setHomeAsUpIndicator(R.drawable.ic_back_button)
    }
  }

  override fun onSupportNavigateUp(): Boolean {
    return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfig)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.toolbar_menu, menu)
    return true
  }

  override fun onPrepareOptionsMenu(menu: Menu): Boolean {
    val history = menu.findItem(R.id.view_tips_history)
    history.isVisible = isMenuVisible
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.view_tips_history -> {
        findNavController(R.id.nav_host_fragment)
          .navigate(AddTipFragmentDirections.actionListToDetail())
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    outState.putBoolean(BACK_BUTTON_STATE, isBackButtonVisible)
    super.onSaveInstanceState(outState)
  }

  companion object {
    private const val BACK_BUTTON_STATE = "com.example.tipjar.backButtonState"
  }
}
