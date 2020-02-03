package eu.kanade.tachiyomi.ui.manga.track

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.customview.customView
import com.jakewharton.rxbinding.widget.itemClicks
import com.jakewharton.rxbinding.widget.textChanges
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.data.database.models.Track
import eu.kanade.tachiyomi.data.track.TrackManager
import eu.kanade.tachiyomi.data.track.TrackService
import eu.kanade.tachiyomi.data.track.model.TrackSearch
import eu.kanade.tachiyomi.ui.base.controller.DialogController
import kotlinx.android.synthetic.main.track_controller.*
import eu.kanade.tachiyomi.util.lang.plusAssign
import kotlinx.android.synthetic.main.track_search_dialog.view.progress
import kotlinx.android.synthetic.main.track_search_dialog.view.track_search
import kotlinx.android.synthetic.main.track_search_dialog.view.track_search_list
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.concurrent.TimeUnit

class TrackSearchDialog : DialogController {

    private var dialogView: View? = null

    private var adapter: TrackSearchAdapter? = null

    private var selectedItem: Track? = null

    private val service: TrackService

    private var subscriptions = CompositeSubscription()

    private var searchTextSubscription: Subscription? = null

    private val trackController
        get() = targetController as TrackController

    private var wasPreviouslyTracked:Boolean = false

    constructor(target: TrackController, service: TrackService, wasTracked:Boolean) : super(Bundle()
        .apply {
        putInt(KEY_SERVICE, service.id)
    }) {
        wasPreviouslyTracked = wasTracked
        targetController = target
        this.service = service
    }

    @Suppress("unused")
    constructor(bundle: Bundle) : super(bundle) {
        service = Injekt.get<TrackManager>().getService(bundle.getInt(KEY_SERVICE))!!
    }

    override fun onCreateDialog(savedViewState: Bundle?): Dialog {
        val dialog = MaterialDialog(activity!!).apply {
            customView(viewRes = R.layout.track_search_dialog, scrollable = false)
            negativeButton(android.R.string.cancel)
            positiveButton(
                if (wasPreviouslyTracked) R.string.action_clear
                else R.string.action_track){ onPositiveButtonClick() }
            setActionButtonEnabled(WhichButton.POSITIVE, wasPreviouslyTracked)
        }

        if (subscriptions.isUnsubscribed) {
            subscriptions = CompositeSubscription()
        }

        dialogView = dialog.view
        onViewCreated(dialog.view, savedViewState)

        return dialog
    }

    fun onViewCreated(view: View, savedState: Bundle?) {
        // Create adapter
        val adapter = TrackSearchAdapter(view.context)
        this.adapter = adapter
        view.track_search_list.adapter = adapter

        // Set listeners
        selectedItem = null

        subscriptions += view.track_search_list.itemClicks().subscribe { position ->
            selectedItem = adapter.getItem(position)
            (dialog as? MaterialDialog)?.positiveButton(R.string.action_track)
            (dialog as? MaterialDialog)?.setActionButtonEnabled(WhichButton.POSITIVE, true)
        }

        // Do an initial search based on the manga's title
        if (savedState == null) {
            val title = trackController.presenter.manga.originalTitle()
            view.track_search.append(title)
            search(title)
        }
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        subscriptions.unsubscribe()
        dialogView = null
        adapter = null
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        searchTextSubscription = dialogView!!.track_search.textChanges()
                .skip(1)
                .debounce(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .map { it.toString() }
                .filter(String::isNotBlank)
                .subscribe { search(it) }
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        searchTextSubscription?.unsubscribe()
    }

    private fun search(query: String) {
        val view = dialogView ?: return
        view.progress.visibility = View.VISIBLE
        view.track_search_list.visibility = View.INVISIBLE
        trackController.presenter.search(query, service)
    }

    fun onSearchResults(results: List<TrackSearch>) {
        selectedItem = null
        val view = dialogView ?: return
        view.progress.visibility = View.INVISIBLE
        view.track_search_list.visibility = View.VISIBLE
        adapter?.setItems(results)
        if (results.size == 1 && !wasPreviouslyTracked) {
            selectedItem = adapter?.getItem(0)
            (dialog as? MaterialDialog)?.positiveButton(R.string.action_track)
            (dialog as? MaterialDialog)?.setActionButtonEnabled(WhichButton.POSITIVE, true)
        }
    }

    fun onSearchResultsError() {
        val view = dialogView ?: return
        view.progress.visibility = View.VISIBLE
        view.track_search_list.visibility = View.INVISIBLE
        adapter?.setItems(emptyList())
    }

    private fun onPositiveButtonClick() {
        trackController.swipe_refresh.isRefreshing = true
        trackController.presenter.registerTracking(selectedItem, service)
    }

    private companion object {
        const val KEY_SERVICE = "service_id"
    }

}
