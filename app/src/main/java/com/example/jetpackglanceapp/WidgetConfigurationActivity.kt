package com.example.jetpackglanceapp


import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class WidgetConfigurationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_configuration)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val selectedButton = when (getSelectedPosition()) {
            1 -> R.id.radioButton1
            2 -> R.id.radioButton2
            3 -> R.id.radioButton3
            else -> throw Exception()

        }
        radioGroup.check(selectedButton)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            updateWidgetData(checkedId)
        }
    }

    private fun updateWidgetData(checkedId: Int) {
        val selectedPsition = when (checkedId) {
            R.id.radioButton1 -> 1
            R.id.radioButton2 -> 2
            R.id.radioButton3 -> 3
            else -> throw Exception()
        }
        setSelectedPosition(selectedPsition)

        val dataList = List(10) { CustomData("Some Text $selectedPsition") }

        GlobalScope.launch {
            val glanceId = getGlanceId()
            glanceId?.let {
                updateAppWidgetState(
                    applicationContext,
                    PreferencesGlanceStateDefinition,
                    glanceId
                ) { preferences ->
                    preferences.toMutablePreferences().apply {
                        set(MyWidget.WIDGET_DATA_KEY, Gson().toJson(dataList))
                    }
                }
                MyWidget().update(applicationContext, glanceId)
            }
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun getSelectedPosition() = getPreferences(MODE_PRIVATE).getInt(POSITION_KEY, 1)

    private fun setSelectedPosition(position: Int) {
        getPreferences(MODE_PRIVATE).edit().putInt(POSITION_KEY, position).apply()
    }

    private suspend fun getGlanceId() = GlanceAppWidgetManager(this).getGlanceIds(MyWidget::class.java).firstOrNull()

    companion object {
        private const val POSITION_KEY = "POSITION_KEY"
    }
}