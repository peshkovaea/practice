package ci.nsu.mobile.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ci.nsu.mobile.main.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: CounterViewModel
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация ViewModel
        viewModel = ViewModelProvider(this)[CounterViewModel::class.java]

        // Настройка RecyclerView
        setupRecyclerView()

        // Наблюдение за изменениями состояния
        observeUiState()

        // Установка обработчиков кнопок
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter()
        binding.historyRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = historyAdapter
        }
    }

    private fun observeUiState() {
        viewModel.uiState.observe(this) { uiState ->
            // Обновление счетчика
            binding.countTextView.text = uiState.count.toString()

            // Обновление истории
            historyAdapter.submitList(uiState.history)
        }
    }

    private fun setupClickListeners() {
        binding.incrementButton.setOnClickListener {
            viewModel.increment()
        }

        binding.decrementButton.setOnClickListener {
            viewModel.decrement()
        }

        binding.resetButton.setOnClickListener {
            viewModel.reset()
        }
    }
}