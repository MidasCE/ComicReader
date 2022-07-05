package com.example.app.main.comic

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.app.R
import com.example.app.core.EventObserver
import com.example.app.core.SchedulerFactory
import com.example.app.main.comic.details.ComicExplanationActivity
import com.example.app.main.comic.viewmodel.ComicScreenViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import io.reactivex.BackpressureStrategy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ComicScreenFragment : Fragment() {

    @Inject
    lateinit var viewModel : ComicScreenViewModel

    @Inject
    lateinit var schedulerFactory : SchedulerFactory

    private lateinit var loadingView: ProgressBar
    private lateinit var rootView: View
    private lateinit var imageView: ImageView
    private lateinit var searchEditText: EditText
    private lateinit var titleTextView: TextView
    private lateinit var altTextView: TextView
    private lateinit var explainationButton: Button
    private lateinit var randomButton: Button
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button

    private val viewDisposable = CompositeDisposable()
    private val _searchInput = BehaviorSubject.create<String>()
    private val searchInput = _searchInput.toFlowable(BackpressureStrategy.LATEST)

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_comic_screen, container, false)
        rootView = view
        loadingView = view.findViewById(R.id.progressBar)
        imageView = view.findViewById(R.id.mainImageView)
        searchEditText = view.findViewById(R.id.searchEditText)
        titleTextView = view.findViewById(R.id.titleTextView)
        altTextView = view.findViewById(R.id.altTextView)
        explainationButton = view.findViewById(R.id.explanationButton)
        randomButton = view.findViewById(R.id.randomButton)
        nextButton = view.findViewById(R.id.nextButton)
        previousButton = view.findViewById(R.id.previousButton)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Start observing the targets
        this.viewModel.loadingLiveData.observe(viewLifecycleOwner, this.loadingObserver)
        this.viewModel.mainComicUrlLiveData.observe(viewLifecycleOwner, this.mainImageObserver)
        this.viewModel.comicTitleLiveData.observe(viewLifecycleOwner, this.titleObserver)
        this.viewModel.comicDescriptionLiveData.observe(viewLifecycleOwner, this.descriptionObserver)
        this.viewModel.errorLiveData.observe(viewLifecycleOwner, this.errorObserver)
        this.viewModel.openDetailsEvent.observe(this, EventObserver { url ->
            val i = Intent(context, ComicExplanationActivity::class.java)
            i.putExtra("url", url)
            startActivity(i)
        })

        explainationButton.setOnClickListener {
            viewModel.onTapExplanation()
        }

        randomButton.setOnClickListener {
            viewModel.getRandomComic()
        }

        nextButton.setOnClickListener {
            viewModel.getNextComic()
        }

        previousButton.setOnClickListener {
            viewModel.getPreviousComic()
        }

        searchEditText.addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(editable: Editable) {
                    _searchInput.onNext(editable.toString())
                }
            }
        )
        viewDisposable.add(
            searchInput
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(schedulerFactory.io())
                .observeOn(schedulerFactory.main())
                .subscribe{ input ->
                    viewModel.getComic(input.toString())
                }
        )
        viewModel.getLatestComic()
    }

    private val loadingObserver = Observer<Boolean> { visible ->
        // Show hide loading view
        if (visible){
            loadingView.visibility = View.VISIBLE
        } else{
            loadingView.visibility = View.INVISIBLE
        }
    }

    private val mainImageObserver = Observer<String> { url ->
        if (!url.isNullOrEmpty()){
            Glide.with(this).load(url).into(imageView)
        }
    }

    private val titleObserver = Observer<String> { title ->
        if (!title.isNullOrEmpty()){
            titleTextView.text = title
        }
    }

    private val descriptionObserver = Observer<String> { description ->
        if (!description.isNullOrEmpty()){
            altTextView.text = description
        }
    }

    private val errorObserver = Observer<String> { errorMessage ->
        if (!errorMessage.isNullOrEmpty()){
            Snackbar.make(rootView, errorMessage, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewDisposable.clear()
        viewModel.onViewDestroy()
    }
}
