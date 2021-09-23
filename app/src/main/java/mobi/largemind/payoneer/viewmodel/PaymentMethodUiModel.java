package mobi.largemind.payoneer.viewmodel;

import java.util.Objects;

public class PaymentMethodUiModel {
    private final String code;
    private final String label;
    private final String logo;

    public PaymentMethodUiModel(String code, String label, String logo) {
        this.code = code;
        this.label = label;
        this.logo = logo;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public String getLogo() {
        return logo;
    }

    @Override
    public String toString() {
        return "PaymentMethodUiModel{" +
                "code='" + code + '\'' +
                ", label='" + label + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentMethodUiModel that = (PaymentMethodUiModel) o;
        return Objects.equals(code, that.code) && Objects.equals(label, that.label) && Objects.equals(logo, that.logo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, label, logo);
    }
}
