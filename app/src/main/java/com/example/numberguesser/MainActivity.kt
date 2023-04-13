package com.example.numberguesser

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Button
import android.widget.SimpleAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import com.example.numberguesser.databinding.ActivityMainBinding
import com.example.numberguesser.util.LocaleHelper
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var preferences: SharedPreferences
    private lateinit var options: Options


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        options = savedInstanceState?.getParcelable(KEY_OPTIONS) ?: Options.DEFAULT
        binding.openMenuButton.setOnClickListener {
            binding.drawer.openDrawer(GravityCompat.END)
        }

        setMenu()
        restoreFromSharedPreferences()
    }

    override fun onBackPressed() {
        if (binding.fragmentContainer.findNavController().backQueue.size == 2) {
            val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
            val view = LayoutInflater.from(this).inflate(
                R.layout.exit_dialog_card, this.findViewById(R.id.dialog_card)
            )
            builder.setView(view)

            val alertDialog = builder.create()

            if (alertDialog.window != null) {
                alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            (view.findViewById(R.id.dialog_yes_button) as Button).setOnClickListener {
                alertDialog.dismiss()
                super.onBackPressed()
            }
            (view.findViewById(R.id.dialog_no_button) as Button).setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        } else super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
    }

    fun restoreFromSharedPreferences() {
        if (preferences.getBoolean(PREF_DARK_MODE, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        options.maxNumber =
            preferences.getFloat(PREF_SLIDER_VAL, options.maxNumber.toFloat()).toInt()
    }

    private fun setMenu() {
        val switchView =
            binding.settingsMenu.menu.findItem(R.id.switch_item).actionView as SwitchMaterial

        switchView.isChecked = preferences.getBoolean(PREF_DARK_MODE, false)
        switchView.setOnCheckedChangeListener { _, b ->
            if (b) {
                preferences.edit().putBoolean(PREF_DARK_MODE, true).apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                preferences.edit().putBoolean(PREF_DARK_MODE, false).apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val spinner = binding.settingsMenu.menu.findItem(R.id.spinner_item).actionView as Spinner
        val data = listOf(
            mapOf(
                KEY_TITLE to getString(R.string.eng_lang),
                KEY_ICON to R.drawable.eng_lang_icon
            ),
            mapOf(
                KEY_TITLE to getString(R.string.es_lang),
                KEY_ICON to R.drawable.es_lang_icon
            ),
            mapOf(
                KEY_TITLE to getString(R.string.rus_lang),
                KEY_ICON to R.drawable.ru_lang_icon
            ),
            mapOf(
                KEY_TITLE to getString(R.string.zh_lang),
                KEY_ICON to R.drawable.zh_lang_icon
            )
        )


        val adapter = SimpleAdapter(
            this,
            data,
            R.layout.spinner_row,
            arrayOf(KEY_TITLE, KEY_ICON),
            intArrayOf(R.id.text_lang, R.id.icon_lang)
        )
        spinner.adapter = adapter
        spinner.setSelection(
            when (resources.configuration.locales[0].language) {
                "en" -> 0
                "es" -> 1
                "ru" -> 2
                "zh" -> 3
                else -> 0
            }, false
        )
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val curLang = resources.configuration.locales[0].language
                if (p1 == null) return
                val langCode = when (p2) {
                    0 -> "en"
                    1 -> "es"
                    2 -> "ru"
                    3 -> "zh"
                    else -> "en"
                }

                Log.i("spinner", "item selected $p2 $curLang -> $langCode ")

                if (langCode != curLang) {
                    LocaleHelper().setLocale(this@MainActivity, langCode)
                    recreate()
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.i("spinner", "nothing selected")
            }

        }
    }

    override fun attachBaseContext(base: Context) {
        LocaleHelper().setLocale(base, LocaleHelper().getLanguage(base))
        super.attachBaseContext(LocaleHelper().onAttach(base))
    }

    companion object {
        private const val KEY_OPTIONS = "OPTIONS"
        private const val APP_PREFERENCES = "APP_PREFERENCES"
        private const val PREF_DARK_MODE = "PREF_DARK_MODE"
        const val PREF_SLIDER_VAL = "PREF_SLIDER_VAL"
        private const val KEY_TITLE = "LANGUAGE"
        private const val KEY_ICON = "ICON"
    }
}