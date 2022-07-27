package com.example.jetpackglanceapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MyWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    @Composable
    override fun Content() {
        val prefs = currentState<Preferences>()
        val dataList: List<CustomData> = Gson().fromJson(prefs[WIDGET_DATA_KEY].toString(), object : TypeToken<List<CustomData>>() {
        }.type)

        //LazyColumn shows only the first set of data. All the next updates wil be ignored.
        LazyColumn(GlanceModifier.background(Color.White)) {
            itemsIndexed(dataList) { _, data ->
                Text(modifier = GlanceModifier.padding(R.dimen.widget_padding), text = data.name)
            }
        }

        //Column shows data for all updates
        /*Column(GlanceModifier.background(Color.White)) {
            dataList.forEach { data ->
                Text(modifier = GlanceModifier.padding(R.dimen.widget_padding), text = data.name)
            }

        }*/
    }

    companion object {
        val WIDGET_DATA_KEY = stringPreferencesKey("widget_data_key")
    }
}
