package mobi.largemind.payoneer;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import mobi.largemind.payoneer.databinding.PaymentItemBinding;
import mobi.largemind.payoneer.viewmodel.PaymentMethodUiModel;

public class PaymentsAdapter extends ListAdapter<PaymentMethodUiModel, PaymentsAdapter.PaymentViewHolder> {

    protected PaymentsAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PaymentItemBinding binding = PaymentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PaymentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static final DiffUtil.ItemCallback<PaymentMethodUiModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<PaymentMethodUiModel>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull PaymentMethodUiModel oldModel, @NonNull PaymentMethodUiModel newModel) {
                    return oldModel.getCode().equals(newModel.getCode());
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull PaymentMethodUiModel oldModel, @NonNull PaymentMethodUiModel newModel) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldModel.equals(newModel);
                }
            };

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {
        private final PaymentItemBinding binding;

        public PaymentViewHolder(@NonNull PaymentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(PaymentMethodUiModel model) {
            binding.label.setText(model.getLabel());
            Picasso.get().load(model.getLogo()).into(binding.logo);
        }
    }
}
