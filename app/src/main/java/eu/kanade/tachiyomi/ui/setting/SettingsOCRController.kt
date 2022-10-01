package eu.kanade.tachiyomi.ui.setting

import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.preference.PreferenceScreen
import eu.kanade.tachiyomi.R

class SettingsOCRController : SettingsController() {

    override fun setupPreferenceScreen(screen: PreferenceScreen) = screen.apply {
        titleRes = R.string.pref_category_ocr

        if (context.checkSelfPermission("com.getraid.tachiyomiocr.permission.READ_WRITE_DATABASE") == PERMISSION_GRANTED) {
        }
        // preferenceCategory {
        //     titleRes = R.string.anki
        //     //replace in ContextExtensions.kt i think?
        //     if (context.checkSelfPermission("com.getraid.tachiyomiocr.permission.READ_WRITE_DATABASE") == PERMISSION_GRANTED) {
        //         val api = AddContentApi(context)

        //         listPreference {
        //             key = Keys.ankiDeckName
        //             titleRes = R.string.pref_anki_deck_name
        //             entries = api.deckList.values.toTypedArray()
        //             entryValues = api.deckList.values.toTypedArray()
        //             summary = "%s"
        //         }

        //         listPreference {
        //             key = Keys.ankiModelName
        //             titleRes = R.string.pref_anki_model_name
        //             entries = api.modelList.values.toTypedArray()
        //             entryValues = api.modelList.values.toTypedArray()
        //             summary = "%s"
        //         }

        //         preferenceCategory {
        //             titleRes = R.string.anki_fields_routing

        //             multiSelectListPreference {
        //                 key = Keys.ankiSentenceExportFields
        //                 titleRes = R.string.anki_field_sentence
        //                 entries = arrayOf()
        //                 entryValues = arrayOf()
        //                 setSummaryProvider { values.joinToString() }
        //                 preferences.ankiModelName().asImmediateFlow { modelName ->
        //                     if (modelName.isNotEmpty()) {
        //                         val modelId = api.modelList.toList().first { it.second == modelName }.first
        //                         val fields = api.getFieldList(modelId)
        //                         entries = fields
        //                         entryValues = fields
        //                     }
        //                 }.launchIn(viewScope)
        //             }

        //             multiSelectListPreference {
        //                 key = Keys.ankiWordExportFields
        //                 titleRes = R.string.anki_field_word
        //                 entries = arrayOf()
        //                 entryValues = arrayOf()
        //                 setSummaryProvider { values.joinToString() }
        //                 preferences.ankiModelName().asImmediateFlow { modelName ->
        //                     if (modelName.isNotEmpty()) {
        //                         val modelId = api.modelList.toList().first { it.second == modelName }.first
        //                         val fields = api.getFieldList(modelId)
        //                         entries = fields
        //                         entryValues = fields
        //                     }
        //                 }.launchIn(viewScope)
        //             }

        //             multiSelectListPreference {
        //                 key = Keys.ankiReadingExportFields
        //                 titleRes = R.string.anki_field_reading
        //                 entries = arrayOf()
        //                 entryValues = arrayOf()
        //                 setSummaryProvider { values.joinToString() }
        //                 preferences.ankiModelName().asImmediateFlow { modelName ->
        //                     if (modelName.isNotEmpty()) {
        //                         val modelId = api.modelList.toList().first { it.second == modelName }.first
        //                         val fields = api.getFieldList(modelId)
        //                         entries = fields
        //                         entryValues = fields
        //                     }
        //                 }.launchIn(viewScope)
        //             }

        //             multiSelectListPreference {
        //                 key = Keys.ankiMeaningExportFields
        //                 titleRes = R.string.anki_field_meaning
        //                 entries = arrayOf()
        //                 entryValues = arrayOf()
        //                 setSummaryProvider { values.joinToString() }
        //                 preferences.ankiModelName().asImmediateFlow { modelName ->
        //                     if (modelName.isNotEmpty()) {
        //                         val modelId = api.modelList.toList().first { it.second == modelName }.first
        //                         val fields = api.getFieldList(modelId)
        //                         entries = fields
        //                         entryValues = fields
        //                     }
        //                 }.launchIn(viewScope)
        //             }
        //         }
        //     }
        // }
    }
}
