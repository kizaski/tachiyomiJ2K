package eu.kanade.tachiyomi.ui.source.browse

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.databinding.SourceFilterSheetBinding
import eu.kanade.tachiyomi.util.system.dpToPx
import eu.kanade.tachiyomi.util.view.collapse
import eu.kanade.tachiyomi.util.view.setEdgeToEdge
import eu.kanade.tachiyomi.util.view.updateLayoutParams
import eu.kanade.tachiyomi.util.view.updatePaddingRelative

class SourceFilterSheet(val activity: Activity) :
    BottomSheetDialog(activity, R.style.BottomSheetDialogTheme) {

    private val sheetBehavior: BottomSheetBehavior<*>

    private var filterChanged = true

    val adapter: FlexibleAdapter<IFlexible<*>> = FlexibleAdapter<IFlexible<*>>(null)
        .setDisplayHeadersAtStartUp(true)

    var onSearchClicked = {}

    var onResetClicked = {}

    private val binding = SourceFilterSheetBinding.inflate(activity.layoutInflater)
    init {
        setContentView(binding.root)
        binding.searchBtn.setOnClickListener { dismiss() }
        binding.resetBtn.setOnClickListener { onResetClicked() }

        sheetBehavior = BottomSheetBehavior.from(binding.root.parent as ViewGroup)
        sheetBehavior.peekHeight = 450.dpToPx
        sheetBehavior.collapse()
        setEdgeToEdge(activity, binding.root)

        binding.titleLayout.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.cardView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    val fullHeight = activity.window.decorView.height
                    val insets = activity.window.decorView.rootWindowInsets
                    matchConstraintMaxHeight =
                        fullHeight - (insets?.systemWindowInsetTop ?: 0) -
                        binding.titleLayout.height - 75.dpToPx
                }
                if (binding.titleLayout.height > 0) {
                    binding.titleLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })

        (binding.root.parent.parent as? View)?.viewTreeObserver?.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                updateBottomButtons()
                if (sheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
                    (binding.root.parent.parent as? View)?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                }
            }
        })

        setOnShowListener {
            updateBottomButtons()
        }

        binding.filtersRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        binding.filtersRecycler.clipToPadding = false
        binding.filtersRecycler.adapter = adapter
        binding.filtersRecycler.setHasFixedSize(true)

        sheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, progress: Float) {
                    updateBottomButtons()
                }

                override fun onStateChanged(p0: View, state: Int) {
                    updateBottomButtons()
                }
            }
        )

        binding.filtersRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    sheetBehavior.isDraggable = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.canScrollVertically(-1)) {
                    sheetBehavior.isDraggable = false
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        sheetBehavior.collapse()
        updateBottomButtons()
        binding.root.post {
            updateBottomButtons()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val attrsArray = intArrayOf(android.R.attr.actionBarSize)
        val array = context.obtainStyledAttributes(attrsArray)
        val headerHeight = array.getDimensionPixelSize(0, 0)
        binding.titleLayout.updatePaddingRelative(
            bottom = activity.window.decorView.rootWindowInsets.systemWindowInsetBottom
        )

        binding.titleLayout.updateLayoutParams<ConstraintLayout.LayoutParams> {
            height = headerHeight + binding.titleLayout.paddingBottom
        }
        array.recycle()
    }

    private fun updateBottomButtons() {
        val bottomSheet = binding.root.parent as View
        val bottomSheetVisibleHeight = -bottomSheet.top + (activity.window.decorView.height - bottomSheet.height)

        binding.titleLayout.translationY = bottomSheetVisibleHeight.toFloat()
    }

    override fun dismiss() {
        super.dismiss()
        if (filterChanged) {
            onSearchClicked()
        }
    }

    fun setFilters(items: List<IFlexible<*>>) {
        adapter.updateDataSet(items)
    }
}
