package mobi.largemind.payoneer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import dagger.hilt.android.AndroidEntryPoint;
import mobi.largemind.payoneer.databinding.ActivityMainBinding;
import mobi.largemind.payoneer.viewmodel.PaymentMethodViewModel;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private PaymentMethodViewModel viewModel;
    private ActivityMainBinding binding;
    private PaymentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(R.string.payment_methods);

        adapter = new PaymentsAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(PaymentMethodViewModel.class);
        viewModel.getUiState().observe(this, uiState -> {
            Log.d("MainActivity", "Current state: " + uiState);
            switch (uiState.getState()) {
                case LOADING:
                    binding.progress.setVisibility(View.VISIBLE);
                    break;
                case LOADED:
                    binding.progress.setVisibility(View.GONE);
                    adapter.submitList(uiState.getData());
                    break;
                case ERROR:
                    Toast.makeText(this, "Error loading payment methods", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}