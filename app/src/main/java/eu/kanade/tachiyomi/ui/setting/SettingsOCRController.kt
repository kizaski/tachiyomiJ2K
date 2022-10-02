package eu.kanade.tachiyomi.ui.setting

import androidx.preference.PreferenceScreen
import com.ichi2.anki.api.AddContentApi
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.data.preference.*
import kotlinx.coroutines.flow.launchIn
import eu.kanade.tachiyomi.data.preference.PreferenceKeys as Keys

class SettingsOCRController : SettingsController() {

    override fun setupPreferenceScreen(screen: PreferenceScreen) = screen.apply {
        titleRes = R.string.pref_category_ocr

        preferenceCategory {
            titleRes = R.string.anki
            // replace in ContextExtensions.kt i think?

            val api = AddContentApi(context)

            listPreference(activity) {
                key = Keys.ankiDeckName
                titleRes = R.string.pref_anki_deck_name
                entries = api.deckList.values.toTypedArray().toList()
                entryValues = api.deckList.values.toTypedArray().toList()
            }

            listPreference(activity) {
                key = Keys.ankiModelName
                titleRes = R.string.pref_anki_model_name
                entries = api.modelList.values.toTypedArray().toList()
                entryValues = api.modelList.values.toTypedArray().toList()
            }

            preferenceCategory {
                titleRes = R.string.anki_fields_routing

                multiSelectListPreferenceMat(activity) {
                    titleRes = R.string.library_update_restriction
                    entriesRes = arrayOf(R.string.wifi, R.string.charging, R.string.battery_not_low)
                    entryValues = listOf(DEVICE_ONLY_ON_WIFI, DEVICE_CHARGING, DEVICE_BATTERY_NOT_LOW)
                    preSummaryRes = R.string.restrictions_
                    noSelectionRes = R.string.none

                    preferences.libraryUpdateInterval().asImmediateFlowIn(viewScope) {
                        isVisible = it > 0
                    }
                }

                multiSelectListPreferenceMat(activity) {
                    key = Keys.ankiSentenceExportFields
                    titleRes = R.string.anki_field_sentence
                    entries = emptyList()
                    entryValues = emptyList()
                    // TODO: don't know how to fix setSummaryProvider yet
                    // setSummaryProvider { values.joinToString()}

                    preferences.ankiModelName().asImmediateFlow { modelName ->
                        if (modelName.isNotEmpty()) {
                            val modelId = api.modelList.toList().first { it.second == modelName }.first
                            val fields = api.getFieldList(modelId)
                            this.entries = fields.toList()
                            this.entryValues = fields.toList()
                        }
                    }.launchIn(viewScope)
                }

                multiSelectListPreferenceMat(activity) {
                    key = Keys.ankiWordExportFields
                    titleRes = R.string.anki_field_word
                    entries = emptyList()
                    entryValues = emptyList()
                    // TODO: don't know how to fix setSummaryProvider yet
                    // setSummaryProvider { values.joinToString() }
                    preferences.ankiModelName().asImmediateFlow { modelName ->
                        if (modelName.isNotEmpty()) {
                            val modelId = api.modelList.toList().first { it.second == modelName }.first
                            val fields = api.getFieldList(modelId)
                            entries = fields.toList()
                            entryValues = fields.toList()
                        }
                    }.launchIn(viewScope)
                }

                multiSelectListPreferenceMat(activity) {
                    key = Keys.ankiReadingExportFields
                    titleRes = R.string.anki_field_reading
                    entries = emptyList()
                    entryValues = emptyList()
                    // TODO: don't know how to fix setSummaryProvider yet
                    // setSummaryProvider { values.joinToString() }
                    preferences.ankiModelName().asImmediateFlow { modelName ->
                        if (modelName.isNotEmpty()) {
                            val modelId = api.modelList.toList().first { it.second == modelName }.first
                            val fields = api.getFieldList(modelId)
                            entries = fields.toList()
                            entryValues = fields.toList()
                        }
                    }.launchIn(viewScope)
                }

                multiSelectListPreferenceMat(activity) {
                    key = Keys.ankiMeaningExportFields
                    titleRes = R.string.anki_field_meaning
                    entries = emptyList()
                    entryValues = emptyList()
                    // TODO: don't know how to fix setSummaryProvider yet
                    // setSummaryProvider { values.joinToString() }
                    preferences.ankiModelName().asImmediateFlow { modelName ->
                        if (modelName.isNotEmpty()) {
                            val modelId = api.modelList.toList().first { it.second == modelName }.first
                            val fields = api.getFieldList(modelId)
                            entries = fields.toList()
                            entryValues = fields.toList()
                        }
                    }.launchIn(viewScope)
                }
            }
        }
    }
}
