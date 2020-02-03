package eu.kanade.tachiyomi.ui.setting

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceScreen
import com.afollestad.materialdialogs.MaterialDialog
import eu.kanade.tachiyomi.BuildConfig
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.data.updater.UpdateChecker
import eu.kanade.tachiyomi.data.updater.UpdateResult
import eu.kanade.tachiyomi.data.updater.UpdaterService
import eu.kanade.tachiyomi.ui.base.controller.DialogController
import eu.kanade.tachiyomi.ui.main.ChangelogDialogController
import eu.kanade.tachiyomi.util.system.toast
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class SettingsAboutController : SettingsController() {

    /**
     * Checks for new releases
     */
    private val updateChecker by lazy { UpdateChecker.getUpdateChecker() }

    /**
     * The subscribtion service of the obtained release object
     */
    private var releaseSubscription: Subscription? = null

    private val isUpdaterEnabled = BuildConfig.INCLUDE_UPDATER

    override fun setupPreferenceScreen(screen: PreferenceScreen) = with(screen) {
        titleRes = R.string.pref_category_about

        switchPreference {
            key = "acra.enable"
            titleRes = R.string.pref_enable_acra
            summaryRes = R.string.pref_acra_summary
            defaultValue = true
        }
        preference {
            title = "Discord"
            val url = "https://discord.gg/tachiyomi"
            summary = url
            onClick {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
        preference {
            title = "Github"
            val url = "https://github.com/Jays2Kings/tachiyomiJ2K"
            summary = url
            onClick {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
        preference {
            titleRes = R.string.version
            summary = if (BuildConfig.DEBUG)
                "r" + BuildConfig.COMMIT_COUNT
            else
                BuildConfig.VERSION_NAME

            if (isUpdaterEnabled) {
                onClick { checkVersion() }
            }
        }
        preference {
            titleRes = R.string.build_time
            summary = getFormattedBuildTime()

            onClick {
                ChangelogDialogController().showDialog(router)
            }
        }
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        releaseSubscription?.unsubscribe()
        releaseSubscription = null
    }

    /**
     * Checks version and shows a user prompt if an update is available.
     */
    private fun checkVersion() {
        if (activity == null) return

        activity?.toast(R.string.update_check_look_for_updates)
        releaseSubscription?.unsubscribe()
        releaseSubscription = updateChecker.checkForUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    when (result) {
                        is UpdateResult.NewUpdate<*> -> {
                            val body = result.release.info
                            val url = result.release.downloadLink

                            // Create confirmation window
                            NewUpdateDialogController(body, url).showDialog(router)
                        }
                        is UpdateResult.NoNewUpdate -> {
                            activity?.toast(R.string.update_check_no_new_updates)
                        }
                    }
                }, { error ->
                    activity?.toast(error.message)
                    Timber.e(error)
                })
    }

    class NewUpdateDialogController(bundle: Bundle? = null) : DialogController(bundle) {

        constructor(body: String, url: String) : this(Bundle().apply {
            putString(BODY_KEY, body)
            putString(URL_KEY, url)
        })

        override fun onCreateDialog(savedViewState: Bundle?): Dialog {
            return MaterialDialog(activity!!)
                    .title(R.string.update_check_title)
                    .message(text = args.getString(BODY_KEY) ?: "")
                    .positiveButton(R.string.update_check_confirm)  {
                        val appContext = applicationContext
                        if (appContext != null) {
                            // Start download
                            val url = args.getString(URL_KEY) ?: ""
                            UpdaterService.downloadUpdate(appContext, url)
                        }
                    }
                    .negativeButton(R.string.update_check_ignore)
        }

        private companion object {
            const val BODY_KEY = "NewUpdateDialogController.body"
            const val URL_KEY = "NewUpdateDialogController.key"
        }
    }

    private fun getFormattedBuildTime(): String {
        try {
            val inputDf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.US)
            inputDf.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputDf.parse(BuildConfig.BUILD_TIME) ?: return BuildConfig.BUILD_TIME

            val outputDf = DateFormat.getDateTimeInstance(
                    DateFormat.MEDIUM, DateFormat.SHORT, Locale.getDefault())
            outputDf.timeZone = TimeZone.getDefault()

            return outputDf.format(date)
        } catch (e: ParseException) {
            return BuildConfig.BUILD_TIME
        }
    }
}
