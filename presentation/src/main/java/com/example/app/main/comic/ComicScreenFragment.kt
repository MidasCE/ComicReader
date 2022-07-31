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
import com.example.app.core.EventObserver
import com.example.app.core.SchedulerFactory
import com.example.app.databinding.FragmentComicScreenBinding
import com.example.app.main.comic.details.ComicExplanationActivity
import com.example.app.main.comic.viewmodel.ComicScreenViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.BackpressureStrategy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class ComicScreenFragment : Fragment() {

    @Inject
    lateinit var viewModel : ComicScreenViewModel

    @Inject
    lateinit var schedulerFactory : SchedulerFactory

    private val viewDisposable = CompositeDisposable()
    private val _searchInput = BehaviorSubject.create<String>()
    private val searchInput = _searchInput.toFlowable(BackpressureStrategy.LATEST)

    private var _binding: FragmentComicScreenBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentComicScreenBinding.inflate(inflater, container, false)
        return binding.root
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

        binding.explanationButton.setOnClickListener {
            viewModel.onTapExplanation()
        }

        binding.randomButton.setOnClickListener {
            viewModel.getRandomComic()
        }

        binding.nextButton.setOnClickListener {
            viewModel.getNextComic()
        }

        binding.previousButton.setOnClickListener {
            viewModel.getPreviousComic()
        }

        binding.searchEditText.addTextChangedListener(
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
            binding.progressBar.visibility = View.VISIBLE
        } else{
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private val mainImageObserver = Observer<String> { url ->
        if (!url.isNullOrEmpty()){
            Glide.with(this).load(url).into(binding.mainImageView)
        }
    }

    private val titleObserver = Observer<String> { title ->
        if (!title.isNullOrEmpty()){
            binding.titleTextView.text = title
        }
    }

    private val descriptionObserver = Observer<String> { description ->
        if (!description.isNullOrEmpty()){
            binding.altTextView.text = description
        }
    }

    private val errorObserver = Observer<String> { errorMessage ->
        if (!errorMessage.isNullOrEmpty()){
            Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        viewDisposable.clear()
        viewModel.onViewDestroy()
    }
}
